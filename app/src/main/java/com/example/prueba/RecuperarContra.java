package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarContra extends AppCompatActivity {

    // Campo donde el usuario escribe su correo
    private TextInputEditText inputEmail;
    // Botón para enviar la solicitud y botón para regresar al login
    private MaterialButton btnEnviar, btnVolver;
    // Círculo de carga mientras espera respuesta del servidor
    private ProgressBar progressBar;
    // Interfaz de Retrofit para hablar con Supabase
    private SupabaseApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_contra);

        // Ajusta el padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Instancia el servicio de Retrofit
        apiService = RetrofitClient.getClient().create(SupabaseApi.class);

        // Vincula controles a las variables
        inputEmail = findViewById(R.id.email);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnVolver = findViewById(R.id.btnVolver);
        progressBar = findViewById(R.id.progressBar);

        // "Volver" cierra y regresa al Login
        btnVolver.setOnClickListener(v -> finish());
        // "Enviar" envia la peticion a supabase
        btnEnviar.setOnClickListener(v -> enviarRecuperacion());
    }

    private void enviarRecuperacion() {
        // Lee y limpia los espacios del correo
        String correo = inputEmail.getText().toString().trim();

        // Valida que no esté vacío antes de llamar a supabase
        if (correo.isEmpty()) {
            inputEmail.setError("Ingresa tu correo");
            return;
        }

        // Muestra la progressbar y bloque el boton para evitar un doble envio
        progressBar.setVisibility(View.VISIBLE);
        btnEnviar.setEnabled(false);

        // Arma el request y lo envía a Supabase
        PedirCodigo request = new PedirCodigo(correo);
        apiService.RecuperarContra(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Oculta las progressbar y reactiva el botón
                progressBar.setVisibility(View.GONE);
                btnEnviar.setEnabled(true);

                if (response.isSuccessful()) {
                    //Supabase mandó el código al correo y pasa a la pantalla siguiente
                    Toast.makeText(RecuperarContra.this, "Revisa tu correo para el código", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RecuperarContra.this, nuevaContra.class);
                    // le pasa el correo a la siguiente pantalla
                    intent.putExtra("correo", correo);
                    startActivity(intent);
                } else {
                    // Si supabase responde pero con un error
                    Toast.makeText(RecuperarContra.this, "No se pudo enviar el correo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Falló la conexión
                progressBar.setVisibility(View.GONE);
                btnEnviar.setEnabled(true);
                Toast.makeText(RecuperarContra.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}