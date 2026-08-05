package com.example.prueba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovimientoActivity extends AppCompatActivity {

    private TextView txtTipo;
    private EditText txtCantidad;
    private EditText txtObservaciones;
    private Button btnGuardarMovimiento;

    private InventarioApi inventarioApi;
    private MovimientoApi movimientoApi;

    private String tipo;

    private int idInventario;
    private int idProducto;
    private int idAlmacen;
    private int cantidadActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento);

        txtTipo = findViewById(R.id.txtTipo);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtObservaciones = findViewById(R.id.txtObservaciones);
        btnGuardarMovimiento = findViewById(R.id.btnGuardarMovimiento);

        inventarioApi = RetrofitClient.getClient().create(InventarioApi.class);
        movimientoApi = RetrofitClient.getClient().create(MovimientoApi.class);

        tipo = getIntent().getStringExtra("tipo");

        idInventario = getIntent().getIntExtra("idInventario", 0);
        idProducto = getIntent().getIntExtra("idProducto", 0);
        idAlmacen = getIntent().getIntExtra("idAlmacen", 0);
        cantidadActual = getIntent().getIntExtra("cantidadActual", 0);

        txtTipo.setText(tipo);

        btnGuardarMovimiento.setOnClickListener(v -> guardarMovimiento());
    }

    private void guardarMovimiento() {

        if (txtCantidad.getText().toString().trim().isEmpty()) {
            txtCantidad.setError("Ingrese una cantidad");
            return;
        }

        int cantidad = Integer.parseInt(txtCantidad.getText().toString());

        if (cantidad <= 0) {
            txtCantidad.setError("La cantidad debe ser mayor que cero");
            return;
        }

        int nuevoStock;

        if (tipo.equals("ENTRADA")) {

            nuevoStock = cantidadActual + cantidad;

        } else {

            nuevoStock = cantidadActual - cantidad;

            if (nuevoStock < 0) {

                Toast.makeText(
                        this,
                        "No hay suficiente inventario",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }
        }

        InventarioRequest inventarioRequest = new InventarioRequest(
                idProducto,
                idAlmacen,
                nuevoStock
        );

        inventarioApi.actualizarInventario(
                "eq." + idInventario,
                inventarioRequest
        ).enqueue(new Callback<List<Inventario>>() {

            @Override
            public void onResponse(Call<List<Inventario>> call,
                                   Response<List<Inventario>> response) {

                if (response.isSuccessful()) {

                    registrarMovimiento(cantidad);

                } else {

                    try {

                        Toast.makeText(
                                MovimientoActivity.this,
                                response.errorBody().string(),
                                Toast.LENGTH_LONG
                        ).show();

                    } catch (Exception e) {

                        Toast.makeText(
                                MovimientoActivity.this,
                                response.message(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                }

            }

            @Override
            public void onFailure(Call<List<Inventario>> call,
                                  Throwable t) {

                Toast.makeText(
                        MovimientoActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

            }

        });

    }

    private void registrarMovimiento(int cantidad) {

        // El Login deberá asignar:
        // Sesion.idUsuario = usuario.getId();
        // Sesion.nombreUsuario = usuario.getNombre();

        if (Sesion.idUsuario.isEmpty()) {

            Toast.makeText(
                    this,
                    "No hay un usuario autenticado. El Login debe asignar Sesion.idUsuario.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        MovimientoRequest movimiento = new MovimientoRequest(
                idProducto,
                idAlmacen,
                Sesion.idUsuario,
                tipo.toLowerCase(),
                cantidad,
                txtObservaciones.getText().toString().trim()
        );

        movimientoApi.insertarMovimiento(movimiento)
                .enqueue(new Callback<List<Movimiento>>() {

                    @Override
                    public void onResponse(Call<List<Movimiento>> call,
                                           Response<List<Movimiento>> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    MovimientoActivity.this,
                                    "Movimiento registrado correctamente",
                                    Toast.LENGTH_LONG
                            ).show();

                            finish();

                        } else {

                            try {

                                Toast.makeText(
                                        MovimientoActivity.this,
                                        response.errorBody().string(),
                                        Toast.LENGTH_LONG
                                ).show();

                            } catch (Exception e) {

                                Toast.makeText(
                                        MovimientoActivity.this,
                                        response.message(),
                                        Toast.LENGTH_LONG
                                ).show();

                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<List<Movimiento>> call,
                                          Throwable t) {

                        Toast.makeText(
                                MovimientoActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

    }

}