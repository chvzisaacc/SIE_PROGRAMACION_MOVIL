package com.example.prueba;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.content.Intent;

public class MenuHamburguesaAdmin extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView contenido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_admin);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        contenido = findViewById(R.id.txtContenido);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(v ->{
            drawerLayout.openDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(item ->{
            int id = item.getItemId();

            if(id == R.id.nav_usuario)
            {
                contenido.setText("Usuario");
            }
            else if (id == R.id.nav_productos)
            {
                Intent intent = new Intent(MenuHamburguesaAdmin.this, ProductosActivity.class);
                startActivity(intent);
            }
            else if(id == R.id.nav_inventario)
            {
                Intent intent = new Intent(MenuHamburguesaAdmin.this, InventarioActivity.class);
                startActivity(intent);
            }
            else if(id == R.id.nav_categorias)
            {
                contenido.setText("Categorías");
            }
            else if(id == R.id.nav_almacenes)
            {
                contenido.setText("Almacenes");
            }
            else if(id == R.id.nav_reportes)
            {
                contenido.setText("Reportes");
            }
            else if(id == R.id.nav_cerrar_sesion)
            {
                // Borra el token y todos los datos guardados de la sesión
                ManejarSesion.cerrarSesion(MenuHamburguesaAdmin.this);

                // Limpia toda las actividades y manda al Login como pantalla inicial
                Intent intent = new Intent(MenuHamburguesaAdmin.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

    }
}