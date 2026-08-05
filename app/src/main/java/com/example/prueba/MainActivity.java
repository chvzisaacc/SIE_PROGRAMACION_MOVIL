package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Abrir directamente el módulo de Inventario
        Intent intent = new Intent(MainActivity.this, InventarioActivity.class);
        startActivity(intent);

        finish();
    }
}