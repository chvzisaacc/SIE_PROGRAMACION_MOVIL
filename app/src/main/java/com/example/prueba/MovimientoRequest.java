package com.example.prueba;

public class MovimientoRequest {

    private int id_producto;
    private int id_almacen;
    private String id_usuario;
    private String tipo_movimiento;
    private int cantidad;
    private String observaciones;

    public MovimientoRequest(int id_producto,
                             int id_almacen,
                             String id_usuario,
                             String tipo_movimiento,
                             int cantidad,
                             String observaciones) {

        this.id_producto = id_producto;
        this.id_almacen = id_almacen;
        this.id_usuario = id_usuario;
        this.tipo_movimiento = tipo_movimiento;
        this.cantidad = cantidad;
        this.observaciones = observaciones;
    }

    public int getId_producto() {
        return id_producto;
    }

    public int getId_almacen() {
        return id_almacen;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

}