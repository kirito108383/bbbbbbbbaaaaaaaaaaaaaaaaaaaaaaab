package com.example.core.network

import com.example.core.models.Device
import com.example.core.models.User
import com.example.core.models.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun authenticateByIP(ipAddress: String, role: UserRole): Result<User> {
        return try {
            val userId = "${ipAddress}_${System.currentTimeMillis()}"
            
            val user = User(
                uid = userId,
                role = role,
                ipAddress = ipAddress,
                createdAt = System.currentTimeMillis(),
                lastActive = System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(userId)
                .set(user)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerDevice(userId: String, device: Device): Result<Device> {
        return try {
            val deviceId = device.deviceId
            
            firestore.collection("devices")
                .document(deviceId)
                .set(device)
                .await()

            Result.success(device)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTutorDevices(userId: String): Result<List<Device>> {
        return try {
            val snapshot = firestore.collection("devices")
                .whereEqualTo("tutorId", userId)
                .get()
                .await()

            val devices = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Device::class.java)
            }

            Result.success(devices)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChildDevices(userId: String): Result<List<Device>> {
        return try {
            val snapshot = firestore.collection("devices")
                .whereEqualTo("childId", userId)
                .get()
                .await()

            val devices = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Device::class.java)
            }

            Result.success(devices)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateDeviceLocation(deviceId: String, latitude: Double, longitude: Double): Result<Unit> {
        return try {
            firestore.collection("devices")
                .document(deviceId)
                .update(
                    mapOf(
                        "lastLocation" to mapOf(
                            "latitude" to latitude,
                            "longitude" to longitude,
                            "timestamp" to System.currentTimeMillis()
                        )
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logAppUsage(deviceId: String, appName: String, packageName: String): Result<Unit> {
        return try {
            firestore.collection("devices")
                .document(deviceId)
                .collection("app_usage")
                .add(
                    mapOf(
                        "appName" to appName,
                        "packageName" to packageName,
                        "timestamp" to System.currentTimeMillis()
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun enableScreenStreaming(deviceId: String): Result<Unit> {
        return try {
            firestore.collection("devices")
                .document(deviceId)
                .update("screenStreamingEnabled", true)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun disableScreenStreaming(deviceId: String): Result<Unit> {
        return try {
            firestore.collection("devices")
                .document(deviceId)
                .update("screenStreamingEnabled", false)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun blockApp(deviceId: String, packageName: String): Result<Unit> {
        return try {
            firestore.collection("devices")
                .document(deviceId)
                .collection("blocked_apps")
                .add(mapOf("packageName" to packageName, "timestamp" to System.currentTimeMillis()))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unblockApp(deviceId: String, packageName: String): Result<Unit> {
        return try {
            val snapshot = firestore.collection("devices")
                .document(deviceId)
                .collection("blocked_apps")
                .whereEqualTo("packageName", packageName)
                .get()
                .await()

            for (doc in snapshot.documents) {
                firestore.collection("devices")
                    .document(deviceId)
                    .collection("blocked_apps")
                    .document(doc.id)
                    .delete()
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
