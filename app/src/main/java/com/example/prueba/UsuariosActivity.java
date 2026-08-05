package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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

    // Componentes de la interfaz para el listado y navegación
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

        // Restringe el acceso: solo usuarios con ID de rol 1 (Administrador) pueden ver esta pantalla
        if (ManejarSesion.getIdRol(this) != 1) {
            Toast.makeText(this, "Acceso denegado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_usuarios);

        // Configuración de la barra de herramientas y el menú lateral (Drawer)
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        // Sincroniza el icono de "hamburguesa" con el estado del panel lateral
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Manejo de la navegación entre los módulos administrativos
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_usuario) {
                // Ya estamos en la pantalla actual
            } else if (id == R.id.nav_productos) {
                startActivity(new Intent(this, ProductosActivity.class));
            } else if (id == R.id.nav_inventario) {
                startActivity(new Intent(this, InventarioActivity.class));
            } else if (id == R.id.nav_almacenes) {
                startActivity(new Intent(this, AlmacenActivity.class));
            } else if (id == R.id.nav_proveedores) {
                startActivity(new Intent(this, ProveedoresActivity.class));
            } else if (id == R.id.nav_cerrar_sesion) {
                // Limpia datos locales y redirige al Login
                ManejarSesion.cerrarSesion(this);
                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // Configuración del listado de usuarios (RecyclerView)
        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);

        apiService = RetrofitClient.getClient().create(SupabaseApi.class);

        btnAgregarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(UsuariosActivity.this, CrearUsuarioActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresca la lista cada vez que se vuelve a la pantalla
        cargarUsuarios();
    }

    /**
     * Obtiene la lista de usuarios desde la API de Supabase y configura el adaptador
     */
    private void cargarUsuarios() {
        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new UsuarioAdapter(response.body(), (usuario, nuevoEstado) -> {
                        // Callback cuando se presiona el botón de Activar/Desactivar en la lista
                        actualizarEstadoUsuario(usuario.getId(), nuevoEstado);
                    });
                    rvUsuarios.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {}
        });
    }

    /**
     * Actualiza el borrado lógico (campo 'estado') del usuario en la base de datos
     */
    private void actualizarEstadoUsuario(String uuid, boolean nuevoEstado) {
        Map<String, Boolean> updateMap = new HashMap<>();
        updateMap.put("estado", nuevoEstado);

        // Se usa el prefijo "eq." requerido por PostgREST para filtrar por ID
        apiService.cambiarEstadoUsuario("eq." + uuid, updateMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cargarUsuarios(); // Recarga la lista para reflejar el cambio visualmente
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}
