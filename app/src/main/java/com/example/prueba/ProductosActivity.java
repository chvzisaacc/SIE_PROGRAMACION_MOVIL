package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.adapters.ProductoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos;
    private FloatingActionButton btnAgregar;
    private SearchView searchProductos;

    private ProductoAdapter adapter;

    // Lista que se muestra en pantalla
    private List<Producto> listaProductos;

    // Lista completa para realizar búsquedas
    private List<Producto> listaCompleta;

    private ProductoApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        btnAgregar = findViewById(R.id.btnAgregar);
        searchProductos = findViewById(R.id.searchProductos);

        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = new ArrayList<>();
        listaCompleta = new ArrayList<>();

        adapter = new ProductoAdapter(this, listaProductos);
        recyclerProductos.setAdapter(adapter);

        api = RetrofitClient.getClient().create(ProductoApi.class);

        cargarProductos();

        // El usuario debe presionar Buscar para filtrar
        searchProductos.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchProductos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                buscarProducto(query);

                // Oculta el teclado
                searchProductos.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // Si borra todo el texto, vuelve a mostrar todos
                if (newText.trim().isEmpty()) {

                    listaProductos.clear();
                    listaProductos.addAll(listaCompleta);
                    adapter.notifyDataSetChanged();
                }

                return false;
            }
        });

        btnAgregar.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProductosActivity.this,
                    AgregarProductoActivity.class
            );

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

                    listaCompleta.clear();
                    listaCompleta.addAll(response.body());

                    listaProductos.clear();
                    listaProductos.addAll(listaCompleta);

                    adapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(
                            ProductosActivity.this,
                            "No se pudieron cargar los productos",
                            Toast.LENGTH_SHORT
                    ).show();

                }

            }

            @Override
            public void onFailure(Call<List<Producto>> call,
                                  Throwable t) {

                Toast.makeText(
                        ProductosActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

            }

        });

    }

    // Busca productos por nombre
    private void buscarProducto(String texto) {

        listaProductos.clear();

        if (texto.trim().isEmpty()) {

            listaProductos.addAll(listaCompleta);

        } else {

            for (Producto producto : listaCompleta) {

                if (producto.getNombre_producto()
                        .toLowerCase(Locale.getDefault())
                        .contains(texto.toLowerCase(Locale.getDefault()))) {

                    listaProductos.add(producto);
                }

            }

        }

        adapter.notifyDataSetChanged();

    }

}