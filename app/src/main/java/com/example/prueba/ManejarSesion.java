package com.example.prueba;

import android.content.Context;
import android.content.SharedPreferences;

public class ManejarSesion {

    // Nombre del archivo de SharedPreferences donde se guarda todo
    private static final String PREF_NAME = "sesion_sie";

    // Llaves (keys) para cada dato guardado
    private static final String KEY_TOKEN = "access_token";
    private static final String KEY_UUID = "uuid_usuario";
    private static final String KEY_NOMBRE = "nombre_usuario";
    private static final String KEY_ID_ROL = "id_rol";

    // Guarda los datos de la sesión después de un login exitoso
    public static void guardarSesion(Context context, String token, String uuid, String nombre, int idRol) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_UUID, uuid);
        editor.putString(KEY_NOMBRE, nombre);
        editor.putInt(KEY_ID_ROL, idRol);

        editor.apply();
    }

    // Revisa si hay una sesión guardada
    public static boolean haySesionActiva(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);
        return token != null && !token.isEmpty();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static String getUuid(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_UUID, null);
    }

    public static String getNombre(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_NOMBRE, null);
    }

    public static int getIdRol(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_ID_ROL, -1);
    }

    // Borra todos los datos de la sesión
    public static void cerrarSesion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        // Limpia también la sesión en memoria
        Sesion.idUsuario = "";
        Sesion.nombreUsuario = "";
    }
}