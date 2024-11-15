package br.fecap.pi.walletwiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import br.fecap.walletwiz.R;

public class perfil extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ImageView icon = findViewById(R.id.icon);
        icon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            // Verificando o item selecionado e iniciando a activity correspondente
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(perfil.this, session.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_acount) {
                startActivity(new Intent(perfil.this, perfil.class));
                return true;
            } else if (item.getItemId() == R.id.nav_academy) {
                startActivity(new Intent(perfil.this, academy.class));
                return true;
            } else if (item.getItemId() == R.id.nav_simulador_aquisicoes) {
                startActivity(new Intent(perfil.this, simulador_aquisicoes.class));
                return true;
            }else if (item.getItemId() == R.id.nav_extract) {
                startActivity(new Intent(perfil.this, extrato.class));
                return true;
            }

            return false;
        });
    }
}