package com.example.prueba;

import com.google.gson.annotations.SerializedName;

public class Categoria {

    @SerializedName("id_categoria")
    private Integer id_categoria;

    @SerializedName("nombre_categoria")
    private String nombre_categoria;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("estado")
    private boolean estado = true;

    // Constructor vacío
    public Categoria() {
    }

    // Getters y Setters
    public Integer getId_categoria() { return id_categoria; }
    public void setId_categoria(Integer id_categoria) { this.id_categoria = id_categoria; }

    public String getNombre_categoria() { return nombre_categoria; }
    public void setNombre_categoria(String nombre_categoria) { this.nombre_categoria = nombre_categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}