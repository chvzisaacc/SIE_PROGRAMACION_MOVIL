package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText inputCorreo, inputContrasena;
    private Button botonIngresar;
    private SupabaseApi apiService;
    private TextView olvidastetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // revisa si ya hay una sesión guardada antes de armar la pantalla de login.
        // Si la hay, salta directo al menú correspondiente y no deja que el usuario
        // vea el formulario de login otra vez.
        if (ManejarSesion.haySesionActiva(this)) {
            int idRol = ManejarSesion.getIdRol(this);
            Intent intent;
            if (idRol == 1) {
                intent = new Intent(Login.this, MenuHamburguesaAdmin.class);
            } else {
                intent = new Intent(Login.this, MenuHamburguesaOperario.class);
            }
            startActivity(intent);
            finish();
            return; // corta aquí, no sigue armando la pantalla de login
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Instanciamos la clase retrofitc
        apiService = RetrofitClient.getClient().create(SupabaseApi.class);

        //Vincular componentes
        inputCorreo = findViewById(R.id.correo);
        inputContrasena = findViewById(R.id.contrasena);
        botonIngresar = findViewById(R.id.ingresar);
        olvidastetxt = findViewById(R.id.olvidaste);

        //Olvidaste tu contraseña
        olvidastetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RecuperarContra.class);
                startActivity(intent);
            }
        });

        // botón Ingresar
        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = inputCorreo.getText().toString().trim();
                String clave = inputContrasena.getText().toString().trim();

                if (correo.isEmpty() || clave.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Petición de login mediante interfaz de Retrofit
                LoginRequest request = new LoginRequest(correo, clave);
                apiService.iniciarSesion(request).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String uuidUsuario = response.body().getUser().getId();
                            // NUEVO: guardamos el token ya mismo, antes de revisar el rol
                            String accessToken = response.body().getAccessToken();
                            revisarRolyAbrirLayout(uuidUsuario, accessToken);
                        } else {
                            Toast.makeText(Login.this, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(Login.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //Buscar el Rol en la tabla usuarios filtrando por el UUID
    private void revisarRolyAbrirLayout(String uuid, String accessToken) {
        apiService.obtenerUsuarioPorId("eq." + uuid).enqueue(new Callback<List<UsuarioModelo>>() {
            @Override
            public void onResponse(Call<List<UsuarioModelo>> call, Response<List<UsuarioModelo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    UsuarioModelo usuario = response.body().get(0);

                    // Validamos si la cuenta está activa
                    if (!usuario.isEstado()) {
                        Toast.makeText(Login.this, "Tu cuenta está desactivada.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    //guarda la sesión completa (token, uuid, nombre, rol)
                    ManejarSesion.guardarSesion(
                            Login.this,
                            accessToken,
                            uuid,
                            usuario.getNombre(),
                            usuario.getId_rol()
                    );

                    Intent intent;
                    // Valida el id_rol 1 = Admin, 2 = Operario
                    if (usuario.getId_rol() == 1) {
                        Toast.makeText(Login.this, "¡Bienvenido Administrador " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(Login.this, MenuHamburguesaAdmin.class);
                    } else {
                        Toast.makeText(Login.this, "¡Bienvenido Operario " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(Login.this, MenuHamburguesaOperario.class);
                    }

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Error al obtener los permisos del usuario", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioModelo>> call, Throwable t) {
                Toast.makeText(Login.this, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}