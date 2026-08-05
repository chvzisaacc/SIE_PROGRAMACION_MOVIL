package com.example.prueba;

public class InventarioRequest {

    private int id_producto;
    private int id_almacen;
    private int cantidad_actual;

    public InventarioRequest(int id_producto,
                             int id_almacen,
                             int cantidad_actual) {

        this.id_producto = id_producto;
        this.id_almacen = id_almacen;
        this.cantidad_actual = cantidad_actual;
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
}