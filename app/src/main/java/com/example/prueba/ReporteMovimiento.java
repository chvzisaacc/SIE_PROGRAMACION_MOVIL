package com.example.prueba;

import com.google.gson.annotations.SerializedName;

public class ReporteMovimiento {
    @SerializedName("id_movimiento") private Integer idMovimiento;
    @SerializedName("nombre_producto") private String nombreProducto;
    @SerializedName("nombre_almacen") private String nombreAlmacen;
    @SerializedName("tipo_movimiento") private String tipoMovimiento;
    @SerializedName("cantidad") private int cantidad;
    @SerializedName("fecha_movimiento") private String fechaMovimiento;
    @SerializedName("observaciones") private String observaciones;

    public Integer getIdMovimiento() { return idMovimiento; }
    public String getNombreProducto() { return nombreProducto; }
    public String getNombreAlmacen() { return nombreAlmacen; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public int getCantidad() { return cantidad; }
    public String getFechaMovimiento() { return fechaMovimiento; }
    public String getObservaciones() { return observaciones; }
}