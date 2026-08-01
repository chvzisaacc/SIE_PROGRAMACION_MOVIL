package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView rvUsuarios;
    private FloatingActionButton fabAgregarUsuario;
    private SupabaseApi apiService;
    private UsuarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ManejarSesion.getIdRol(this) != 1) {
            Toast.makeText(this, "Acceso denegado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_usuarios);

        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        fabAgregarUsuario = findViewById(R.id.fabAgregarUsuario);

        apiService = RetrofitClient.getClient().create(SupabaseApi.class);

        fabAgregarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(UsuariosActivity.this, CrearUsuarioActivity.class);
            startActivity(intent);
        });
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
                        actualizarEstadoUsuario(usuario.getIdentificacion(), nuevoEstado);
                    });
                    rvUsuarios.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {}
        });
    }

    private void actualizarEstadoUsuario(String uuid, boolean nuevoEstado) {
        Map<String, Boolean> updateMap = new HashMap<>();
        updateMap.put("estado", nuevoEstado);

        // Asegúrate de que el formato "eq." coincida con tu base de datos
        apiService.cambiarEstadoUsuario("eq." + uuid, updateMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cargarUsuarios();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}