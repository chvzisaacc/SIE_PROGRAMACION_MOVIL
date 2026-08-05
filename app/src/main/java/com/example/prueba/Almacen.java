package com.example.prueba;

import com.google.gson.annotations.SerializedName;

public class Almacen {

    @SerializedName("id_almacen")
    private Integer id_almacen;

    @SerializedName("nombre_almacen")
    private String nombre_almacen;

    @SerializedName("ubicacion")
    private String ubicacion;

    @SerializedName("responsable")
    private String responsable;

    // Se eliminó 'transient' y se agregó @SerializedName para que Retrofit envíe el booleano a Supabase
    @SerializedName("estado")
    private boolean estado = true;

    public Almacen() {
    }

    public Integer getId_almacen() { return id_almacen; }
    public void setId_almacen(Integer id_almacen) { this.id_almacen = id_almacen; }

    public String getNombre_almacen() { return nombre_almacen; }
    public void setNombre_almacen(String nombre_almacen) { this.nombre_almacen = nombre_almacen; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}