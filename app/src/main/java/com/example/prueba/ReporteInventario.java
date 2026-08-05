package com.example.prueba;

import com.google.gson.annotations.SerializedName;

public class ReporteInventario {
    @SerializedName("id_producto") private Integer idProducto;
    @SerializedName("nombre_producto") private String nombreProducto;
    @SerializedName("nombre_almacen") private String nombreAlmacen;
    @SerializedName("cantidad_actual") private int cantidadActual;
    @SerializedName("precio_unitario") private double precioUnitario;
    @SerializedName("valor_total") private double valorTotal;

    public Integer getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public String getNombreAlmacen() { return nombreAlmacen; }
    public int getCantidadActual() { return cantidadActual; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getValorTotal() { return valorTotal; }
}