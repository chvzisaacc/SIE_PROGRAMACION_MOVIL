package com.example.prueba;

public class Producto {
    private int id_producto;
    private int id_categoria;
    private int id_proveedor;
    private String nombre_producto;
    private String descripcion;
    private double precio_unitario;
    private String unidad_medida;
    private int stock_minimo;

    public int getId_producto() { return id_producto; }
    public int getId_categoria() { return id_categoria; }
    public int getId_proveedor() { return id_proveedor; }
    public String getNombre_producto() { return nombre_producto; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio_unitario() { return precio_unitario; }
    public String getUnidad_medida() { return unidad_medida; }
    public int getStock_minimo() { return stock_minimo; }
}