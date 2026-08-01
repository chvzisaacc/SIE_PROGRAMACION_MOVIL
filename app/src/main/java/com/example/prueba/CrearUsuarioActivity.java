package com.example.prueba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearUsuarioActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCorreo;
    private RadioButton rbAdmin;
    private Button btnGuardar;
    private SupabaseApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        etNombre = findViewById(R.id.etNuevoNombre);
        etApellido = findViewById(R.id.etNuevoApellido);
        etCorreo = findViewById(R.id.etNuevoCorreo);
        rbAdmin = findViewById(R.id.rbAdmin);
        btnGuardar = findViewById(R.id.btnGuardarUsuario);

        apiService = RetrofitClient.getClient().create(SupabaseApi.class);
        btnGuardar.setOnClickListener(v -> guardarNuevoUsuario());
    }

    private void guardarNuevoUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setIdentificacion(UUID.randomUUID().toString());
        nuevoUsuario.setId_rol(rbAdmin.isChecked() ? 1 : 2);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setEstado(true);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setTelefono(""); // Opcional según tu BD

        apiService.crearUsuarioEnTabla(nuevoUsuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearUsuarioActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}
