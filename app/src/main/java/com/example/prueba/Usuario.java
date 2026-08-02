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
}