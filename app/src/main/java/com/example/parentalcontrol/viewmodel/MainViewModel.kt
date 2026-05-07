package com.example.parentalcontrol.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.models.Device
import com.example.core.models.UserRole
import com.example.core.network.FirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.provider.Settings.Secure
import java.net.InetAddress
import java.net.NetworkInterface

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseService: FirebaseService,
    private val context: Context
) : ViewModel() {

    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole: StateFlow<UserRole?> = _userRole.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    private val _deviceId = MutableStateFlow("")
    val deviceId: StateFlow<String> = _deviceId.asStateFlow()

    private val _ipAddress = MutableStateFlow("")
    val ipAddress: StateFlow<String> = _ipAddress.asStateFlow()

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        initializeDevice()
    }

    private fun initializeDevice() {
        viewModelScope.launch {
            try {
                val androidId = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
                _deviceId.value = androidId

                val ip = getIPAddress()
                _ipAddress.value = ip

                val uid = "${ip}_${androidId}"
                _userId.value = uid
            } catch (e: Exception) {
                _error.value = "Error inicializando dispositivo: ${e.message}"
            }
        }
    }

    fun setUserRole(role: UserRole) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _userRole.value = role

                val result = firebaseService.authenticateByIP(_ipAddress.value, role)

                result.onSuccess {
                    _userId.value = it.uid
                    loadUserDevices()
                    _error.value = null
                }.onFailure {
                    _error.value = "Error en autenticación: ${it.message}"
                }

                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                _loading.value = false
            }
        }
    }

    private fun loadUserDevices() {
        viewModelScope.launch {
            try {
                val result = firebaseService.getTutorDevices(_userId.value)
                result.onSuccess {
                    _devices.value = it
                }.onFailure {
                    _error.value = "Error cargando dispositivos: ${it.message}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

    private fun getIPAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        if (!hostAddress.contains(":")) {
                            return hostAddress
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "0.0.0.0"
    }
}
