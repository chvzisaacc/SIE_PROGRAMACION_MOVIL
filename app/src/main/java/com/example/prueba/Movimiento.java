package com.example.prueba;

public class Movimiento {
    private int id_movimiento;
    private int id_producto;
    private int id_almacen;
    private String id_usuario;
    private String tipo_movimiento;
    private int cantidad;
    private String fecha_movimiento;
    private String observaciones;

    public int getId_movimiento() { return id_movimiento; }
    public int getId_producto() { return id_producto; }
    public int getId_almacen() { return id_almacen; }
    public String getId_usuario() { return id_usuario; }
    public String getTipo_movimiento() { return tipo_movimiento; }
    public int getCantidad() { return cantidad; }
    public String getFecha_movimiento() { return fecha_movimiento; }
    public String getObservaciones() { return observaciones; }
}