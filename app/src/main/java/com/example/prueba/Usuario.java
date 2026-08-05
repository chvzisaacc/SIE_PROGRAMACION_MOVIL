package com.example.prueba;

public class Usuario {
    private String identificacion; // uuid
    private int id_rol;
    private String nombre;
    private String apellido;
    private boolean estado;
    private String fecha_registro;
    private String telefono;
    private String correo;

    public String getIdentificacion() { return identificacion; }
    public int getId_rol() { return id_rol; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public boolean getEstado() { return estado; }
    public String getFecha_registro() { return fecha_registro; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }

    //setters
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public void setId_rol(int id_rol) { this.id_rol = id_rol; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
}