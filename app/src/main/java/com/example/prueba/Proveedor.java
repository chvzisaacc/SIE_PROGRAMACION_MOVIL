package com.example.prueba;

import com.google.gson.annotations.SerializedName;

public class Proveedor {

    @SerializedName("id_proveedor")
    private Integer id_proveedor;

    @SerializedName("nombre_proveedor")
    private String nombre_proveedor;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("correo")
    private String correo;

    @SerializedName("direccion")
    private String direccion;

    // transient evita que Gson/Retrofit envíe este campo en el JSON hacia Supabase
    private transient boolean estado = true;

    public Proveedor() {
    }

    public Integer getId_proveedor() { return id_proveedor; }
    public void setId_proveedor(Integer id_proveedor) { this.id_proveedor = id_proveedor; }

    public String getNombre_proveedor() { return nombre_proveedor; }
    public void setNombre_proveedor(String nombre_proveedor) { this.nombre_proveedor = nombre_proveedor; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}