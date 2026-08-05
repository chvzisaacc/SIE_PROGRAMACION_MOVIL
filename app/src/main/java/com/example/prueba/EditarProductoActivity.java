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

public class EditarProductoActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtDescripcion;
    private EditText txtPrecio;
    private EditText txtUnidad;
    private EditText txtStock;
    private EditText txtCategoria;
    private EditText txtProveedor;

    private Button btnActualizar;

    private ProductoApi api;

    private int idProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtUnidad = findViewById(R.id.txtUnidad);
        txtStock = findViewById(R.id.txtStock);
        txtCategoria = findViewById(R.id.txtCategoria);
        txtProveedor = findViewById(R.id.txtProveedor);

        btnActualizar = findViewById(R.id.btnActualizar);

        api = RetrofitClient.getClient().create(ProductoApi.class);

        idProducto = getIntent().getIntExtra("id_producto", 0);

        cargarDatos();

        btnActualizar.setOnClickListener(v -> actualizarProducto());
    }

    private void cargarDatos() {

        txtNombre.setText(getIntent().getStringExtra("nombre"));
        txtDescripcion.setText(getIntent().getStringExtra("descripcion"));
        txtPrecio.setText(getIntent().getStringExtra("precio"));
        txtUnidad.setText(getIntent().getStringExtra("unidad"));
        txtStock.setText(getIntent().getStringExtra("stock"));
        txtCategoria.setText(getIntent().getStringExtra("categoria"));
        txtProveedor.setText(getIntent().getStringExtra("proveedor"));
    }

    private void actualizarProducto() {

        ProductoRequest producto = new ProductoRequest(

                Integer.parseInt(txtCategoria.getText().toString()),
                Integer.parseInt(txtProveedor.getText().toString()),
                txtNombre.getText().toString(),
                txtDescripcion.getText().toString(),
                Double.parseDouble(txtPrecio.getText().toString()),
                txtUnidad.getText().toString(),
                Integer.parseInt(txtStock.getText().toString())

        );

        api.actualizarProducto(
                "eq." + idProducto,
                producto
        ).enqueue(new Callback<List<Producto>>() {

            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(
                            EditarProductoActivity.this,
                            "Producto actualizado correctamente",
                            Toast.LENGTH_LONG
                    ).show();

                    finish();

                } else {

                    try {
                        Toast.makeText(
                                EditarProductoActivity.this,
                                response.errorBody().string(),
                                Toast.LENGTH_LONG
                        ).show();
                    } catch (Exception e) {
                        Toast.makeText(
                                EditarProductoActivity.this,
                                "Error al actualizar",
                                Toast.LENGTH_LONG
                        ).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {

                Toast.makeText(
                        EditarProductoActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

            }

        });

    }

}