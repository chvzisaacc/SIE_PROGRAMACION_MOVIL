package com.example.prueba;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportesActivity extends AppCompatActivity {

    private Button btnInventario, btnCriticos, btnMovimientos, btnConsultarMov, btnExportarPDF;
    private EditText etFechaInicio, etFechaFin;
    private LinearLayout layoutFiltroFechas;
    private RecyclerView rvReportes;
    private SupabaseApi api;
    private ReporteAdapter adapter;

    private int reporteActual = 1; // 1: Inventario, 2: Críticos, 3: Movimientos
    private List<ReporteInventario> listaInventario = new ArrayList<>();
    private List<ReporteCritico> listaCriticos = new ArrayList<>();
    private List<ReporteMovimiento> listaMovimientos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reportes de Inventario");
        }

        btnInventario = findViewById(R.id.btnRepInventario);
        btnCriticos = findViewById(R.id.btnRepCriticos);
        btnMovimientos = findViewById(R.id.btnRepMovimientos);
        btnConsultarMov = findViewById(R.id.btnConsultarMov);
        btnExportarPDF = findViewById(R.id.btnExportarPDF);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        layoutFiltroFechas = findViewById(R.id.layoutFiltroFechas);
        rvReportes = findViewById(R.id.rvReportes);

        rvReportes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReporteAdapter();
        rvReportes.setAdapter(adapter);

        api = RetrofitClient.getClient().create(SupabaseApi.class);

        etFechaInicio.setOnClickListener(v -> mostrarDatePicker(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarDatePicker(etFechaFin));

        cargarInventarioDisponible();

        btnInventario.setOnClickListener(v -> {
            reporteActual = 1;
            layoutFiltroFechas.setVisibility(View.GONE);
            limpiarAdaptador();
            cargarInventarioDisponible();
        });

        btnCriticos.setOnClickListener(v -> {
            reporteActual = 2;
            layoutFiltroFechas.setVisibility(View.GONE);
            limpiarAdaptador();
            cargarProductosCriticos();
        });

        btnMovimientos.setOnClickListener(v -> {
            reporteActual = 3;
            layoutFiltroFechas.setVisibility(View.VISIBLE);
            limpiarAdaptador();
        });

        btnConsultarMov.setOnClickListener(v -> {
            String fInicio = etFechaInicio.getText().toString().trim();
            String fFin = etFechaFin.getText().toString().trim();

            if (fInicio.isEmpty() || fFin.isEmpty()) {
                Toast.makeText(this, "Selecciona ambas fechas", Toast.LENGTH_SHORT).show();
                return;
            }

            // CAMBIO AQUÍ: Usar espacio en blanco en lugar de 'T' para compatibilidad TIMESTAMP con Postgres
            cargarMovimientosPorPeriodo(fInicio + " 00:00:00", fFin + " 23:59:59");
        });

        btnExportarPDF.setOnClickListener(v -> {
            if (reporteActual == 1 && listaInventario != null && !listaInventario.isEmpty()) {
                GeneradorPDF.generarPDFInventario(this, listaInventario);
            } else if (reporteActual == 2 && listaCriticos != null && !listaCriticos.isEmpty()) {
                GeneradorPDF.generarPDFCriticos(this, listaCriticos);
            } else if (reporteActual == 3 && listaMovimientos != null && !listaMovimientos.isEmpty()) {
                GeneradorPDF.generarPDFMovimientos(this, listaMovimientos);
            } else {
                Toast.makeText(this, "No hay datos cargados para exportar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarAdaptador() {
        if (adapter != null) {
            adapter.setDatos(new ArrayList<>());
        }
    }

    private void mostrarDatePicker(EditText campoFecha) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String fecha = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth);
                    campoFecha.setText(fecha);
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void cargarInventarioDisponible() {
        api.getReporteInventario().enqueue(new Callback<List<ReporteInventario>>() {
            @Override
            public void onResponse(Call<List<ReporteInventario>> call, Response<List<ReporteInventario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaInventario = response.body();
                    List<String> filas = new ArrayList<>();
                    for (ReporteInventario item : listaInventario) {
                        filas.add(item.getNombreProducto() + " | Stock: " + item.getCantidadActual() + " (L. " + item.getPrecioUnitario() + ")");
                    }
                    adapter.setDatos(filas);
                    Toast.makeText(ReportesActivity.this, "Cargados " + listaInventario.size() + " registros", Toast.LENGTH_SHORT).show();
                } else {
                    mostrarError(response);
                }
            }

            @Override
            public void onFailure(Call<List<ReporteInventario>> call, Throwable t) {
                Toast.makeText(ReportesActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarProductosCriticos() {
        api.getProductosCriticos().enqueue(new Callback<List<ReporteCritico>>() {
            @Override
            public void onResponse(Call<List<ReporteCritico>> call, Response<List<ReporteCritico>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCriticos = response.body();
                    List<String> filas = new ArrayList<>();
                    for (ReporteCritico item : listaCriticos) {
                        filas.add(item.getNombreProducto() + " | Stock Actual: " + item.getCantidadActual() + " / Mínimo: " + item.getStockMinimo());
                    }
                    adapter.setDatos(filas);
                    Toast.makeText(ReportesActivity.this, "Críticos: " + listaCriticos.size(), Toast.LENGTH_SHORT).show();
                } else {
                    mostrarError(response);
                }
            }

            @Override
            public void onFailure(Call<List<ReporteCritico>> call, Throwable t) {
                Toast.makeText(ReportesActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarMovimientosPorPeriodo(String inicio, String fin) {
        Map<String, String> body = new HashMap<>();
        body.put("fecha_inicio", inicio);
        body.put("fecha_fin", fin);

        api.getMovimientosPorPeriodo(body).enqueue(new Callback<List<ReporteMovimiento>>() {
            @Override
            public void onResponse(Call<List<ReporteMovimiento>> call, Response<List<ReporteMovimiento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaMovimientos = response.body();
                    List<String> filas = new ArrayList<>();
                    for (ReporteMovimiento item : listaMovimientos) {
                        filas.add(item.getTipoMovimiento().toUpperCase() + ": " + item.getNombreProducto() + " | Cant: " + item.getCantidad() + " (" + item.getFechaMovimiento() + ")");
                    }
                    adapter.setDatos(filas);
                    Toast.makeText(ReportesActivity.this, "Movimientos: " + listaMovimientos.size(), Toast.LENGTH_SHORT).show();
                } else {
                    mostrarError(response);
                }
            }

            @Override
            public void onFailure(Call<List<ReporteMovimiento>> call, Throwable t) {
                Toast.makeText(ReportesActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarError(Response<?> response) {
        try {
            String error = response.errorBody() != null ? response.errorBody().string() : "Error " + response.code();
            Log.e("REPORTE_ERROR", error);
            Toast.makeText(this, "Error " + response.code() + ": " + error, Toast.LENGTH_LONG).show();
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