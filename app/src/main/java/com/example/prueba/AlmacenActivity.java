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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Almacenes");
        }

        etNombre = findViewById(R.id.etNombreAlmacen);
        etUbicacion = findViewById(R.id.etUbicacionAlmacen);
        etResponsable = findViewById(R.id.etResponsableAlmacen);
        btnGuardar = findViewById(R.id.btnGuardarAlmacen);
        btnLimpiar = findViewById(R.id.btnLimpiarAlmacen);
        rvAlmacenes = findViewById(R.id.rvAlmacenes);

        rvAlmacenes.setLayoutManager(new LinearLayoutManager(this));
        api = RetrofitClient.getClient().create(SupabaseApi.class);

        btnGuardar.setOnClickListener(v -> guardarAlmacen());
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
                    adapter = new AlmacenAdapter(listaAlmacenes, almacen -> deshabilitarAlmacen(almacen));
                    rvAlmacenes.setAdapter(adapter);
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

        if (nombre.isEmpty()) { etNombre.setError("Ingrese el nombre"); return; }
        if (ubicacion.isEmpty()) { etUbicacion.setError("Ingrese la ubicación"); return; }
        if (responsable.isEmpty()) { etResponsable.setError("Ingrese el responsable"); return; }

        Almacen nuevoAlmacen = new Almacen();
        nuevoAlmacen.setNombre_almacen(nombre);
        nuevoAlmacen.setUbicacion(ubicacion);
        nuevoAlmacen.setResponsable(responsable);

        Toast.makeText(this, "Guardando almacén...", Toast.LENGTH_SHORT).show();

        api.insertarAlmacen(nuevoAlmacen).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AlmacenActivity.this, "¡Almacén guardado con éxito!", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                    cargarAlmacenes();
                } else {
                    try {
                        // Lee el cuerpo de error que manda Supabase en JSON
                        String errorDetalle = response.errorBody() != null ? response.errorBody().string() : "Sin detalle";
                        Toast.makeText(AlmacenActivity.this, "Error " + response.code() + ": " + errorDetalle, Toast.LENGTH_LONG).show();
                        Log.e("ALMACEN_ERROR_DETALLE", errorDetalle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AlmacenActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ALMACEN_FAIL", t.getMessage(), t);
            }
        });
    }

    private void deshabilitarAlmacen(Almacen a) {
        a.setEstado(false); // Borrado Lógico
        api.actualizarAlmacen("eq." + a.getId_almacen(), a).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AlmacenActivity.this, "Almacén deshabilitado", Toast.LENGTH_SHORT).show();
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
        etNombre.requestFocus();
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