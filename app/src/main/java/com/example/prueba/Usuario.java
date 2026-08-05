package com.example.prueba;

public class Usuario {
    private String id; // uuid en BD
    private int id_rol;
    private String nombre;
    private String apellido;
    private boolean estado;
    private String fecha_registro;
    private String telefono;
    private String correo;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getId_rol() { return id_rol; }
    public void setId_rol(int id_rol) { this.id_rol = id_rol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public boolean getEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(String fecha_registro) { this.fecha_registro = fecha_registro; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}