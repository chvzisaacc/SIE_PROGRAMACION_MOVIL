package com.example.prueba;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

public class ProveedoresActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etCorreo, etDireccion;
    private Button btnGuardar, btnLimpiar;
    private RecyclerView rvProveedores;
    private SupabaseApi api;
    private ProveedorAdapter adapter;
    private List<Proveedor> listaProveedores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Proveedores");
        }

        etNombre = findViewById(R.id.etNombreProveedor);
        etTelefono = findViewById(R.id.etTelefonoProveedor);
        etCorreo = findViewById(R.id.etCorreoProveedor);
        etDireccion = findViewById(R.id.etDireccionProveedor);
        btnGuardar = findViewById(R.id.btnGuardarProveedor);
        btnLimpiar = findViewById(R.id.btnLimpiarProveedor);
        rvProveedores = findViewById(R.id.rvProveedores);

        rvProveedores.setLayoutManager(new LinearLayoutManager(this));
        api = RetrofitClient.getClient().create(SupabaseApi.class);

        btnGuardar.setOnClickListener(v -> guardarProveedor());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());

        cargarProveedores();
    }

    private void cargarProveedores() {
        api.getProveedores().enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Filtrar solo los activos (estado == true)
                    listaProveedores.clear();
                    for (Proveedor p : response.body()) {
                        if (p.isEstado()) listaProveedores.add(p);
                    }
                    adapter = new ProveedorAdapter(listaProveedores, proveedor -> deshabilitarProveedor(proveedor));
                    rvProveedores.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {
                Toast.makeText(ProveedoresActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarProveedor() {
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();

        // Validaciones obligatorias
        if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Ingrese un correo válido");
            return;
        }

        // Creación del objeto mediante setters explícitos
        Proveedor nuevo = new Proveedor();
        nuevo.setNombre_proveedor(nombre);
        nuevo.setTelefono(telefono);
        nuevo.setCorreo(correo);
        nuevo.setDireccion(direccion);

        Toast.makeText(this, "Guardando proveedor...", Toast.LENGTH_SHORT).show();

        api.insertarProveedor(nuevo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProveedoresActivity.this, "¡Proveedor guardado con éxito!", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarProveedores();
                } else {
                    try {
                        String errorDetalle = response.errorBody() != null ? response.errorBody().string() : "Sin detalle";
                        Toast.makeText(ProveedoresActivity.this, "Error " + response.code() + ": " + errorDetalle, Toast.LENGTH_LONG).show();
                        Log.e("PROVEEDOR_ERROR_DETALLE", errorDetalle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProveedoresActivity.this, "Error al guardar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("PROVEEDOR_FAIL", t.getMessage(), t);
            }
        });
    }

    private void deshabilitarProveedor(Proveedor p) {
        p.setEstado(false); // Borrado Lógico
        api.actualizarProveedor("eq." + p.getId_proveedor(), p).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProveedoresActivity.this, "Proveedor deshabilitado", Toast.LENGTH_SHORT).show();
                    cargarProveedores();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProveedoresActivity.this, "Error al deshabilitar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etTelefono.setText("");
        etCorreo.setText("");
        etDireccion.setText("");
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