package com.example.prueba;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarProductoActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtDescripcion;
    private EditText txtPrecio;
    private EditText txtUnidad;
    private EditText txtStock;

    private Spinner spCategoria;
    private Spinner spProveedor;

    private Button btnGuardar;

    private ProductoApi api;
    private InventarioApi inventarioApi;
    private CategoriaApi categoriaApi;
    private ProveedorApi proveedorApi;

    private List<Categoria> listaCategorias = new ArrayList<>();
    private List<Proveedor> listaProveedores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtUnidad = findViewById(R.id.txtUnidad);
        txtStock = findViewById(R.id.txtStock);

        spCategoria = findViewById(R.id.spCategoria);
        spProveedor = findViewById(R.id.spProveedor);

        btnGuardar = findViewById(R.id.btnGuardar);

        api = RetrofitClient.getClient().create(ProductoApi.class);
        inventarioApi = RetrofitClient.getClient().create(InventarioApi.class);
        categoriaApi = RetrofitClient.getClient().create(CategoriaApi.class);
        proveedorApi = RetrofitClient.getClient().create(ProveedorApi.class);

        cargarCategorias();
        cargarProveedores();

        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    private void cargarCategorias() {

        categoriaApi.getCategorias().enqueue(new Callback<List<Categoria>>() {

            @Override
            public void onResponse(Call<List<Categoria>> call,
                                   Response<List<Categoria>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    listaCategorias = response.body();

                    List<String> nombres = new ArrayList<>();

                    for (Categoria categoria : listaCategorias) {
                        nombres.add(categoria.getNombre_categoria());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(
                                    AgregarProductoActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    nombres
                            );

                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    spCategoria.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<List<Categoria>> call,
                                  Throwable t) {

                Toast.makeText(
                        AgregarProductoActivity.this,
                        "No se pudieron cargar las categorías",
                        Toast.LENGTH_SHORT
                ).show();

            }

        });

    }

    private void cargarProveedores() {

        proveedorApi.getProveedores().enqueue(new Callback<List<Proveedor>>() {

            @Override
            public void onResponse(Call<List<Proveedor>> call,
                                   Response<List<Proveedor>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    listaProveedores = response.body();

                    List<String> nombres = new ArrayList<>();

                    for (Proveedor proveedor : listaProveedores) {
                        nombres.add(proveedor.getNombre_proveedor());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(
                                    AgregarProductoActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    nombres
                            );

                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    spProveedor.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<List<Proveedor>> call,
                                  Throwable t) {

                Toast.makeText(
                        AgregarProductoActivity.this,
                        "No se pudieron cargar los proveedores",
                        Toast.LENGTH_SHORT
                ).show();

            }

        });

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

        int stockInicial = Integer.parseInt(txtStock.getText().toString());

        Categoria categoriaSeleccionada =
                listaCategorias.get(spCategoria.getSelectedItemPosition());

        Proveedor proveedorSeleccionado =
                listaProveedores.get(spProveedor.getSelectedItemPosition());

        ProductoRequest producto = new ProductoRequest(
                categoriaSeleccionada.getId_categoria(),
                proveedorSeleccionado.getId_proveedor(),
                txtNombre.getText().toString().trim(),
                txtDescripcion.getText().toString().trim(),
                Double.parseDouble(txtPrecio.getText().toString()),
                txtUnidad.getText().toString().trim(),
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
                            2,
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
                                                "Producto guardado, pero no se pudo crear el inventario",
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
                                            "Producto guardado, pero ocurrió un error al crear el inventario",
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