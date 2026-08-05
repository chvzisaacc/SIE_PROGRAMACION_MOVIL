package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView rvUsuarios;
    private Button btnAgregarUsuario;
    private SupabaseApi apiService;
    private UsuarioAdapter adapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Control de acceso por rol (Solo administradores = ID 1)
        if (ManejarSesion.getIdRol(this) != 1) {
            Toast.makeText(this, "Acceso denegado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_usuarios);

        // Inicialización de componentes UI
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Usuarios");
        }

        // Configuración nativa del DrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Manejo del evento OnBackPressed moderno (reemplaza onBackPressed obsoleto)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });

        // Configuración del menú de navegación lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            drawerLayout.closeDrawer(GravityCompat.START);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (id == R.id.nav_usuario) {
                    // Pantalla actual
                } else if (id == R.id.nav_productos) {
                    abrirPantalla(ProductosActivity.class);
                } else if (id == R.id.nav_inventario) {
                    abrirPantalla(InventarioActivity.class);
                } else if (id == R.id.nav_categorias) {
                    abrirPantalla(CategoriaActivity.class);
                } else if (id == R.id.nav_almacenes) {
                    abrirPantalla(AlmacenActivity.class);
                } else if (id == R.id.nav_proveedores) {
                    abrirPantalla(ProveedoresActivity.class);
                } else if (id == R.id.nav_reportes) {
                    abrirPantalla(ReportesActivity.class);
                } else if (id == R.id.nav_cerrar_sesion) {
                    ManejarSesion.cerrarSesion(this);
                    Intent intent = new Intent(this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }, 200);

            return true;
        });

        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);

        apiService = RetrofitClient.getClient().create(SupabaseApi.class);

        btnAgregarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(UsuariosActivity.this, CrearUsuarioActivity.class);
            startActivity(intent);
        });
    }

    private void abrirPantalla(Class<?> claseDestino) {
        Intent intent = new Intent(this, claseDestino);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new UsuarioAdapter(response.body(), (usuario, nuevoEstado) -> {
                        actualizarEstadoUsuario(usuario.getId(), nuevoEstado);
                    });
                    rvUsuarios.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(UsuariosActivity.this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarEstadoUsuario(String uuid, boolean nuevoEstado) {
        Map<String, Boolean> updateMap = new HashMap<>();
        updateMap.put("estado", nuevoEstado);

        apiService.cambiarEstadoUsuario("eq." + uuid, updateMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cargarUsuarios();
                } else {
                    Toast.makeText(UsuariosActivity.this, "Error al cambiar estado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UsuariosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}