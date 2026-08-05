package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.adapters.ProductoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos;
    private FloatingActionButton btnAgregar;

    private ProductoAdapter adapter;
    private List<Producto> listaProductos;

    private ProductoApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        btnAgregar = findViewById(R.id.btnAgregar);

        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = new ArrayList<>();
        adapter = new ProductoAdapter(this, listaProductos);
        recyclerProductos.setAdapter(adapter);

        api = RetrofitClient.getClient().create(ProductoApi.class);

        cargarProductos();

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this,
                    AgregarProductoActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }

    private void cargarProductos() {

        api.getProductos().enqueue(new Callback<List<Producto>>() {

            @Override
            public void onResponse(Call<List<Producto>> call,
                                   Response<List<Producto>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    listaProductos.clear();
                    listaProductos.addAll(response.body());

                    adapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(ProductosActivity.this,
                            "No se pudieron cargar los productos",
                            Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<List<Producto>> call,
                                  Throwable t) {

                Toast.makeText(ProductosActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();

            }

        });

    }

}