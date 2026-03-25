package com.mibancolombia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mibancolombia.R;
import com.mibancolombia.fragments.HomeFragment;
import com.mibancolombia.fragments.ProductsFragment;
import com.mibancolombia.fragments.MovementsFragment;
import com.mibancolombia.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos de la UI
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        titleTextView = findViewById(R.id.title_text_view);

        // Configurar listener para la navegación inferior
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        titleTextView.setText("Inicio");
                        selectedFragment = HomeFragment.newInstance();
                        break;
                    case R.id.nav_products:
                        titleTextView.setText("Mis productos");
                        selectedFragment = ProductsFragment.newInstance();
                        break;
                    case R.id.nav_movements:
                        titleTextView.setText("Mis movimientos");
                        selectedFragment = MovementsFragment.newInstance();
                        break;
                    case R.id.nav_profile:
                        titleTextView.setText("Mi perfil");
                        selectedFragment = ProfileFragment.newInstance();
                        break;
                }

                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();
                }

                return true;
            }
        });

        // Establecer el fragmento inicial
        titleTextView.setText("Inicio");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, HomeFragment.newInstance());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notifications) {
            // Implementar notificaciones
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}