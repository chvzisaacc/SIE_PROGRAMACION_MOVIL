package com.example.prueba;

public class ProductoRequest {

    private int id_categoria;
    private int id_proveedor;
    private String nombre_producto;
    private String descripcion;
    private double precio_unitario;
    private String unidad_medida;
    private int stock_minimo;

    public ProductoRequest(int id_categoria,
                           int id_proveedor,
                           String nombre_producto,
                           String descripcion,
                           double precio_unitario,
                           String unidad_medida,
                           int stock_minimo) {

        this.id_categoria = id_categoria;
        this.id_proveedor = id_proveedor;
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.precio_unitario = precio_unitario;
        this.unidad_medida = unidad_medida;
        this.stock_minimo = stock_minimo;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public int getStock_minimo() {
        return stock_minimo;
    }
}