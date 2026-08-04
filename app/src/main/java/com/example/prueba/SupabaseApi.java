package com.example.prueba;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SupabaseApi {

    //GET:OBTENER DATOS DESDE EL SERVIDOR
    //POST:INSERTAR DATOS EN EL SERVIDOR
    //PUT:ACTUALIZAR LOS DATOS EXISTENTES EN EL SERVIDOR
    @GET("rest/v1/categorias")
    Call<List<Categoria>> getCategorias();

    @GET("rest/v1/proveedores")
    Call<List<Proveedor>> getProveedores();

    @GET("rest/v1/productos")
    Call<List<Producto>> getProductos();

    @GET("rest/v1/roles")
    Call<List<Rol>> getRoles();

    @GET("rest/v1/usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("rest/v1/almacenes")
    Call<List<Almacen>> getAlmacenes();

    @GET("rest/v1/inventario")
    Call<List<Inventario>> getInventario();

    @GET("rest/v1/movimientos")
    Call<List<Movimiento>> getMovimientos();

    //Endpoint para iniciar sesión en Supabase Auth
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest request);

    //Endpoint(ruta) para buscar el rol del usuario en la tabla usuarios
    @GET("rest/v1/usuarios")
    Call<List<UsuarioModelo>> obtenerUsuarioPorId(@Query("id") String uuid);

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