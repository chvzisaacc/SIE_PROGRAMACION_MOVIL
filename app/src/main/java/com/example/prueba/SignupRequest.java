package com.example.prueba;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Representa la solicitud de registro en Supabase Auth, permitiendo incluir metadatos del usuario.
 */
public class SignupRequest {
    @SerializedName("email")
    private String email;
    
    @SerializedName("password")
    private String password;
    
    // Almacena datos adicionales (nombre, apellido, rol) que el Trigger de BD utilizará
    @SerializedName("data")
    private Map<String, Object> data;

    public SignupRequest(String email, String password, Map<String, Object> data) {
        this.email = email;
        this.password = password;
        this.data = data;
    }
}