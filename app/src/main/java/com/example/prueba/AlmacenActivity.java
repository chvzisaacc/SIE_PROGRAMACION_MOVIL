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

public class AlmacenActivity extends AppCompatActivity {

    private EditText etNombre, etUbicacion, etResponsable;
    private Button btnGuardar, btnLimpiar;
    private RecyclerView rvAlmacenes;
    private SupabaseApi api;
    private AlmacenAdapter adapter;
    private List<Almacen> listaAlmacenes = new ArrayList<>();

    // Variable global para controlar edición o creación
    private Integer idSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Almacenes");
        }

        //Inicializar controles
        etNombre = findViewById(R.id.etNombreAlmacen);
        etUbicacion = findViewById(R.id.etUbicacionAlmacen);
        etResponsable = findViewById(R.id.etResponsableAlmacen);
        btnGuardar = findViewById(R.id.btnGuardarAlmacen);
        btnLimpiar = findViewById(R.id.btnLimpiarAlmacen);
        rvAlmacenes = findViewById(R.id.rvAlmacenes);

        rvAlmacenes.setLayoutManager(new LinearLayoutManager(this));
        api = RetrofitClient.getClient().create(SupabaseApi.class);

        //Configurar Adaptador
        adapter = new AlmacenAdapter(listaAlmacenes, new AlmacenAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Almacen almacen) {
                // Rellenar EditTexts al seleccionar un elemento
                etNombre.setText(almacen.getNombre_almacen());
                etUbicacion.setText(almacen.getUbicacion());
                etResponsable.setText(almacen.getResponsable());

                // Guardar ID y cambiar texto del botón
                idSeleccionado = almacen.getId_almacen();
                btnGuardar.setText("Actualizar");
            }

            @Override
            public void onDeshabilitar(Almacen almacen) {
                deshabilitarAlmacen(almacen);
            }
        });

        rvAlmacenes.setAdapter(adapter);

        // 3. Evaluar acción del botón Guardar/Actualizar
        btnGuardar.setOnClickListener(v -> {
            if (idSeleccionado == null) {
                guardarAlmacen();
            } else {
                actualizarAlmacen(idSeleccionado);
            }
        });

        btnLimpiar.setOnClickListener(v -> limpiarCampos());

        cargarAlmacenes();
    }

    private void cargarAlmacenes() {
        api.getAlmacenes().enqueue(new Callback<List<Almacen>>() {
            @Override
            public void onResponse(Call<List<Almacen>> call, Response<List<Almacen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaAlmacenes.clear();
                    for (Almacen a : response.body()) {
                        if (a.isEstado()) listaAlmacenes.add(a);
                    }
                    adapter.setLista(listaAlmacenes);
                }
            }

            @Override
            public void onFailure(Call<List<Almacen>> call, Throwable t) {
                Toast.makeText(AlmacenActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarAlmacen() {
        String nombre = etNombre.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String responsable = etResponsable.getText().toString().trim();

        if (nombre.isEmpty() || ubicacion.isEmpty() || responsable.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Almacen nuevo = new Almacen();
        nuevo.setNombre_almacen(nombre);
        nuevo.setUbicacion(ubicacion);
        nuevo.setResponsable(responsable);

        api.insertarAlmacen(nuevo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AlmacenActivity.this, "¡Almacén guardado con éxito!", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarAlmacenes();
                } else {
                    mostrarError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AlmacenActivity.this, "Error al guardar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarAlmacen(int id) {
        String nombre = etNombre.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String responsable = etResponsable.getText().toString().trim();

        if (nombre.isEmpty() || ubicacion.isEmpty() || responsable.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Almacen actualizado = new Almacen();
        actualizado.setNombre_almacen(nombre);
        actualizado.setUbicacion(ubicacion);
        actualizado.setResponsable(responsable);

        api.actualizarAlmacen("eq." + id, actualizado).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AlmacenActivity.this, "¡Almacén actualizado con éxito!", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarAlmacenes();
                } else {
                    mostrarError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AlmacenActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deshabilitarAlmacen(Almacen a) {
        a.setEstado(false); // Borrado lógico
        api.actualizarAlmacen("eq." + a.getId_almacen(), a).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AlmacenActivity.this, "Almacén deshabilitado", Toast.LENGTH_SHORT).show();

                    limpiarCampos(); // Limpia formularios y resetea el botón
                    cargarAlmacenes();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AlmacenActivity.this, "Error al deshabilitar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etUbicacion.setText("");
        etResponsable.setText("");
        idSeleccionado = null;
        btnGuardar.setText("Guardar");
        etNombre.requestFocus();
    }

    private void mostrarError(Response<Void> response) {
        try {
            String errorDetalle = response.errorBody() != null ? response.errorBody().string() : "Sin detalle";
            Toast.makeText(this, "Error " + response.code() + ": " + errorDetalle, Toast.LENGTH_LONG).show();
            Log.e("ALMACEN_ERROR_DETALLE", errorDetalle);
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