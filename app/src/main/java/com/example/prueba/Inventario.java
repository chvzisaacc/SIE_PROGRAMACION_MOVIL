package com.example.prueba;

import com.google.gson.annotations.SerializedName;

public class Inventario {

    private int id_inventario;
    private int id_producto;
    private int id_almacen;
    private int cantidad_actual;
    private String fecha_actualizacion;

    @SerializedName("productos")
    private Producto producto;

    public int getId_inventario() {
        return id_inventario;
    }

    public int getId_producto() {
        return id_producto;
    }

    public int getId_almacen() {
        return id_almacen;
    }

    public int getCantidad_actual() {
        return cantidad_actual;
    }

    public String getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public Producto getProducto() {
        return producto;
    }
}