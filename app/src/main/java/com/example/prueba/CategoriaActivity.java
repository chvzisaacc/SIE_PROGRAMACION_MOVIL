package com.example.prueba;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriaActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion;
    private Button btnGuardar, btnLimpiar;
    private RecyclerView rvCategorias;
    private SupabaseApi api;
    private CategoriaAdapter adapter;
    private List<Categoria> listaCategorias = new ArrayList<>();

    private Integer idSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Categorías");
        }

        etNombre = findViewById(R.id.etNombreCategoria);
        etDescripcion = findViewById(R.id.etDescripcionCategoria);
        btnGuardar = findViewById(R.id.btnGuardarCategoria);
        btnLimpiar = findViewById(R.id.btnLimpiarCategoria);
        rvCategorias = findViewById(R.id.rvCategorias);

        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        api = RetrofitClient.getClient().create(SupabaseApi.class);

        adapter = new CategoriaAdapter(listaCategorias, new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Categoria categoria) {
                etNombre.setText(categoria.getNombre_categoria());
                etDescripcion.setText(categoria.getDescripcion());
                idSeleccionado = categoria.getId_categoria();
                btnGuardar.setText("Actualizar");
            }

            @Override
            public void onDeshabilitar(Categoria categoria) {
                deshabilitarCategoria(categoria);
            }
        });

        rvCategorias.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            if (idSeleccionado == null) {
                guardarCategoria();
            } else {
                actualizarCategoria(idSeleccionado);
            }
        });

        btnLimpiar.setOnClickListener(v -> limpiarCampos());

        cargarCategorias();
    }

    private void cargarCategorias() {
        api.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCategorias.clear();
                    for (Categoria c : response.body()) {
                        if (c.isEstado()) listaCategorias.add(c);
                    }
                    adapter.setLista(listaCategorias);
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(CategoriaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCategoria() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Categoria nueva = new Categoria();
        nueva.setNombre_categoria(nombre);
        nueva.setDescripcion(descripcion);

        api.insertarCategoria(nueva).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CategoriaActivity.this, "¡Categoría guardada!", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarCategorias();
                } else {
                    mostrarError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CategoriaActivity.this, "Error al guardar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarCategoria(int id) {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Categoria actualizada = new Categoria();
        actualizada.setNombre_categoria(nombre);
        actualizada.setDescripcion(descripcion);

        api.actualizarCategoria("eq." + id, actualizada).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CategoriaActivity.this, "¡Categoría actualizada!", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarCategorias();
                } else {
                    mostrarError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CategoriaActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deshabilitarCategoria(Categoria c) {
        c.setEstado(false);
        api.actualizarCategoria("eq." + c.getId_categoria(), c).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CategoriaActivity.this, "Categoría deshabilitada", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarCategorias();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CategoriaActivity.this, "Error al deshabilitar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etDescripcion.setText("");
        idSeleccionado = null;
        btnGuardar.setText("Guardar");
        etNombre.requestFocus();
    }

    private void mostrarError(Response<Void> response) {
        try {
            String errorDetalle = response.errorBody() != null ? response.errorBody().string() : "Sin detalle";
            Toast.makeText(this, "Error " + response.code() + ": " + errorDetalle, Toast.LENGTH_LONG).show();
            Log.e("CATEGORIA_ERROR", errorDetalle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}