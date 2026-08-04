package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class nuevaContra extends AppCompatActivity {

    // Campo para el código y campo para la nueva contraseña
    private TextInputEditText inputCodigo, inputPassword;
    // Botones de acción
    private MaterialButton btnGuardar;
    private ImageButton btnVolver;
    // Círculo de carga mientras espera respuesta del servidor
    private ProgressBar progressBar;
    // Interfaz de Retrofit para hablar con Supabase
    private SupabaseApi apiService;
    // Correo que llegó desde RecuperarContra
    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_contra);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = RetrofitClient.getClient().create(SupabaseApi.class);
        // Recupera el correo que mandó la pantalla anterior
        correo = getIntent().getStringExtra("correo");

        inputCodigo = findViewById(R.id.codigo);
        inputPassword = findViewById(R.id.nuevaPassword);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);
        progressBar = findViewById(R.id.progressBar);

        // Evento para regresar a la pantalla anterior
        btnVolver.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        btnGuardar.setOnClickListener(v -> verificarYGuardar());
    }

    private void verificarYGuardar() {
        String codigo = inputCodigo.getText().toString().trim();
        String nuevaPass = inputPassword.getText().toString().trim();

        if (codigo.isEmpty() || nuevaPass.isEmpty()) {
            Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnGuardar.setEnabled(false);

        //verifica el código de recuperacion de contraseña que envio Supabase
        EmailRequest verifyRequest = new EmailRequest(correo, codigo);
        apiService.verificarCodigo(verifyRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //Supabase devuelve un access_token temporal
                    String accessToken = response.body().getAccessToken();
                    //actualiza la contraseña usando ese token
                    guardarNuevaPassword(accessToken, nuevaPass);
                } else {
                    progressBar.setVisibility(View.GONE);
                    btnGuardar.setEnabled(true);
                    Toast.makeText(nuevaContra.this, "Código incorrecto o vencido", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnGuardar.setEnabled(true);
                Toast.makeText(nuevaContra.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarNuevaPassword(String accessToken, String nuevaPass) {
        UpdatePass request = new UpdatePass(nuevaPass);

        apiService.actualizarPassword("Bearer " + accessToken, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                btnGuardar.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(nuevaContra.this, "Contraseña actualizada", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(nuevaContra.this, Login.class));
                    finish();
                } else {
                    Toast.makeText(nuevaContra.this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnGuardar.setEnabled(true);
                Toast.makeText(nuevaContra.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}