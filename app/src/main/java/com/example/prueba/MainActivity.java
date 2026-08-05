package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- CÓDIGO BASE DEL EQUIPO (Prueba de Supabase) ---
        SupabaseApi api = RetrofitClient.getClient().create(SupabaseApi.class);
        // Abrir directamente el módulo de Inventario
        Intent intent = new Intent(MainActivity.this, InventarioActivity.class);
        startActivity(intent);

        api.getProveedores().enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("SUPABASE_OK", "¡Conectado con éxito! Recibidos: " + response.body().size() + " productos.");
                    }
                } else {
                    try {
                        Log.e("SUPABASE_ERROR", response.code() + " - " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("SUPABASE_ERROR", response.code() + " - " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {
                Log.e("SUPABASE_FAIL", "Error de red: " + t.getMessage());
            }
        });

        // --- NAVEGACIÓN A TUS MÓDULOS ---
        Button btnIrProveedores = findViewById(R.id.btnIrProveedores);
        Button btnIrAlmacen = findViewById(R.id.btnIrAlmacen);

        if (btnIrProveedores != null) {
            btnIrProveedores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProveedoresActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (btnIrAlmacen != null) {
            btnIrAlmacen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AlmacenActivity.class);
                    startActivity(intent);
                }
            });
        }
        finish();
    }
}