package com.example.prueba;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SupabaseApi {

    //GET:OBTENER DATOS DESDE EL SERVIDOR
    //POST:INSERTAR DATOS EN EL SERVIDOR
    //PUT:ACTUALIZAR LOS DATOS EXISTENTES EN EL SERVIDOR

    // ==========================================
    //             MÓDULO PROVEEDORES
    // ==========================================

    // Consulta general de proveedores
    @GET("rest/v1/proveedores?select=*")
    Call<List<Proveedor>> getProveedores();

    // Insertar un nuevo proveedor
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/proveedores")
    Call<Void> insertarProveedor(@Body Proveedor proveedor);

    // Actualizar proveedor (Edición de campos / Borrado Lógico)
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @PATCH("rest/v1/proveedores")
    Call<Void> actualizarProveedor(
            @Query("id_proveedor") String idFilter, // Pasa el filtro con sintaxis "eq." + id
            @Body Proveedor proveedor
    );

    // ==========================================
    //             MÓDULO ALMACENES
    // ==========================================

    // Consulta general de almacenes
    @GET("rest/v1/almacenes?select=*")
    Call<List<Almacen>> getAlmacenes();

    // Insertar un nuevo almacén
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/almacenes")
    Call<Void> insertarAlmacen(@Body Almacen almacen);

    // Actualizar almacén (Edición de campos / Borrado Lógico)
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @PATCH("rest/v1/almacenes")
    Call<Void> actualizarAlmacen(
            @Query("id_almacen") String idFilter, // Pasa el filtro con sintaxis "eq." + id
            @Body Almacen almacen
    );

    // ==========================================
    //          MÓDULOS DEL RESTO DEL EQUIPO
    // ==========================================

    @GET("rest/v1/categorias?select=*")
    Call<List<Categoria>> getCategorias();

    @GET("rest/v1/productos?select=*")
    Call<List<Producto>> getProductos();

    @GET("rest/v1/roles?select=*")
    Call<List<Rol>> getRoles();

    @GET("rest/v1/usuarios?select=*")
    Call<List<Usuario>> getUsuarios();

    // Insertar un nuevo usuario
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/usuarios")
    Call<Void> insertarUsuario(@Body Usuario usuario);

    // Actualizar estado de usuario
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @PATCH("rest/v1/usuarios")
    Call<Void> cambiarEstadoUsuario(
            @Query("id") String idFilter,
            @Body Map<String, Boolean> updateMap
    );

    @GET("rest/v1/inventario?select=*")
    Call<List<Inventario>> getInventario();

    @GET("rest/v1/movimientos?select=*")
    Call<List<Movimiento>> getMovimientos();

    // Endpoint para iniciar sesión en Supabase Auth
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest request);

    // Registro de nuevo usuario en Supabase Auth
    @Headers({"Content-Type: application/json"})
    @POST("auth/v1/signup")
    Call<LoginResponse> registrarEnAuth(@Body SignupRequest request);

    // Endpoint(ruta) para buscar el rol del usuario en la tabla usuarios
    @GET("rest/v1/usuarios")
    Call<List<Usuario>> obtenerUsuarioPorId(@Query("id") String uuid);

    // Solicita el envío del código de recuperación al correo del usuario
    @POST("auth/v1/recover")
    Call<Void> RecuperarContra(@Body PedirCodigo request);

    // Valida el código recibido en el correo y devuelve un token temporal
    @POST("auth/v1/verify")
    Call<LoginResponse> verificarCodigo(@Body EmailRequest request);

    // Actualiza la contraseña en Supabase usando el token de autorización
    @PUT("auth/v1/user")
    Call<Void> actualizarPassword(
            @Header("Authorization") String bearerToken,
            @Body UpdatePass request
    );
}