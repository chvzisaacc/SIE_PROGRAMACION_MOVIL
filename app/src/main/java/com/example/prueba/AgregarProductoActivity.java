package com.example.prueba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarProductoActivity extends AppCompatActivity {

    // Controles del formulario
    private EditText txtNombre;
    private EditText txtDescripcion;
    private EditText txtPrecio;
    private EditText txtUnidad;
    private EditText txtStock;
    private EditText txtCategoria;
    private EditText txtProveedor;

    private Button btnGuardar;

    // Servicios de la API
    private ProductoApi api;
    private InventarioApi inventarioApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        // Referencias a los controles
        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtUnidad = findViewById(R.id.txtUnidad);
        txtStock = findViewById(R.id.txtStock);
        txtCategoria = findViewById(R.id.txtCategoria);
        txtProveedor = findViewById(R.id.txtProveedor);

        btnGuardar = findViewById(R.id.btnGuardar);

        // Inicializar APIs
        api = RetrofitClient.getClient().create(ProductoApi.class);
        inventarioApi = RetrofitClient.getClient().create(InventarioApi.class);

        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    private void guardarProducto() {

        if (txtNombre.getText().toString().trim().isEmpty()) {
            txtNombre.setError("Ingrese el nombre");
            return;
        }

        if (txtDescripcion.getText().toString().trim().isEmpty()) {
            txtDescripcion.setError("Ingrese la descripción");
            return;
        }

        if (txtPrecio.getText().toString().trim().isEmpty()) {
            txtPrecio.setError("Ingrese el precio");
            return;
        }

        if (txtUnidad.getText().toString().trim().isEmpty()) {
            txtUnidad.setError("Ingrese la unidad");
            return;
        }

        if (txtStock.getText().toString().trim().isEmpty()) {
            txtStock.setError("Ingrese el stock");
            return;
        }

        if (txtCategoria.getText().toString().trim().isEmpty()) {
            txtCategoria.setError("Ingrese el ID de la categoría");
            return;
        }

        if (txtProveedor.getText().toString().trim().isEmpty()) {
            txtProveedor.setError("Ingrese el ID del proveedor");
            return;
        }

        int stockInicial = Integer.parseInt(txtStock.getText().toString());

        ProductoRequest producto = new ProductoRequest(
                Integer.parseInt(txtCategoria.getText().toString()),
                Integer.parseInt(txtProveedor.getText().toString()),
                txtNombre.getText().toString(),
                txtDescripcion.getText().toString(),
                Double.parseDouble(txtPrecio.getText().toString()),
                txtUnidad.getText().toString(),
                stockInicial
        );

        api.insertarProducto(producto).enqueue(new Callback<List<Producto>>() {

            @Override
            public void onResponse(Call<List<Producto>> call,
                                   Response<List<Producto>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && !response.body().isEmpty()) {

                    Producto productoCreado = response.body().get(0);

                    InventarioRequest inventario = new InventarioRequest(
                            productoCreado.getId_producto(),
                            2, // ID del almacén
                            stockInicial
                    );

                    inventarioApi.insertarInventario(inventario)
                            .enqueue(new Callback<List<Inventario>>() {

                                @Override
                                public void onResponse(Call<List<Inventario>> call,
                                                       Response<List<Inventario>> response) {

                                    if (response.isSuccessful()) {

                                        Toast.makeText(
                                                AgregarProductoActivity.this,
                                                "Producto e inventario guardados correctamente",
                                                Toast.LENGTH_LONG
                                        ).show();

                                    } else {

                                        Toast.makeText(
                                                AgregarProductoActivity.this,
                                                "Producto guardado, pero ocurrió un error al crear el inventario",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }

                                    finish();
                                }

                                @Override
                                public void onFailure(Call<List<Inventario>> call,
                                                      Throwable t) {

                                    Toast.makeText(
                                            AgregarProductoActivity.this,
                                            "Producto guardado, pero no se pudo crear el inventario",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    finish();
                                }
                            });

                } else {

                    try {

                        String error = response.errorBody().string();

                        Toast.makeText(
                                AgregarProductoActivity.this,
                                error,
                                Toast.LENGTH_LONG
                        ).show();

                    } catch (Exception e) {

                        Toast.makeText(
                                AgregarProductoActivity.this,
                                "Código: " + response.code() + "\n" + response.message(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call,
                                  Throwable t) {

                Toast.makeText(
                        AgregarProductoActivity.this,
                        "Error de conexión:\n" + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}