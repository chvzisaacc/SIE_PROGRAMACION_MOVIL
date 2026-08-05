package com.example.prueba;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.adapters.InventarioAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventarioActivity extends AppCompatActivity {

    private RecyclerView recyclerInventario;

    private InventarioAdapter adapter;
    private List<Inventario> listaInventario;

    private InventarioApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        recyclerInventario = findViewById(R.id.recyclerInventario);


        recyclerInventario.setLayoutManager(new LinearLayoutManager(this));

        listaInventario = new ArrayList<>();

        adapter = new InventarioAdapter(listaInventario);

        recyclerInventario.setAdapter(adapter);

        api = RetrofitClient.getClient().create(InventarioApi.class);

        cargarInventario();


    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarInventario();
    }

    private void cargarInventario() {

        api.getInventario().enqueue(new Callback<List<Inventario>>() {

            @Override
            public void onResponse(Call<List<Inventario>> call,
                                   Response<List<Inventario>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    listaInventario.clear();

                    listaInventario.addAll(response.body());

                    adapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(
                            InventarioActivity.this,
                            "No se pudo cargar el inventario",
                            Toast.LENGTH_SHORT
                    ).show();

                }

            }

            @Override
            public void onFailure(Call<List<Inventario>> call,
                                  Throwable t) {

                Toast.makeText(
                        InventarioActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

            }

        });

    }

}