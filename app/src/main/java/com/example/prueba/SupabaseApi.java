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

    @GET("rest/v1/proveedores?select=*")
    Call<List<Proveedor>> getProveedores();

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/proveedores")
    Call<Void> insertarProveedor(@Body Proveedor proveedor);

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @PATCH("rest/v1/proveedores")
    Call<Void> actualizarProveedor(
            @Query("id_proveedor") String idFilter,
            @Body Proveedor proveedor
    );
    @GET("rest/v1/almacenes?select=*")
    Call<List<Almacen>> getAlmacenes();

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/almacenes")
    Call<Void> insertarAlmacen(@Body Almacen almacen);

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @PATCH("rest/v1/almacenes")
    Call<Void> actualizarAlmacen(
            @Query("id_almacen") String idFilter,
            @Body Almacen almacen
    );
    @GET("rest/v1/categorias?select=*")
    Call<List<Categoria>> getCategorias();

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/categorias")
    Call<Void> insertarCategoria(@Body Categoria categoria);

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @PATCH("rest/v1/categorias")
    Call<Void> actualizarCategoria(
            @Query("id_categoria") String idFilter,
            @Body Categoria categoria
    );

    @GET("rest/v1/productos?select=*")
    Call<List<Producto>> getProductos();

    @GET("rest/v1/roles?select=*")
    Call<List<Rol>> getRoles();

    @GET("rest/v1/usuarios?select=*")
    Call<List<Usuario>> getUsuarios();

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/usuarios")
    Call<Void> insertarUsuario(@Body Usuario usuario);

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

    // Autenticación Supabase
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("auth/v1/signup")
    Call<LoginResponse> registrarEnAuth(@Body SignupRequest request);

    @GET("rest/v1/usuarios")
    Call<List<Usuario>> obtenerUsuarioPorId(@Query("id") String uuid);

    @POST("auth/v1/recover")
    Call<Void> RecuperarContra(@Body PedirCodigo request);

    @POST("auth/v1/verify")
    Call<LoginResponse> verificarCodigo(@Body EmailRequest request);

    @PUT("auth/v1/user")
    Call<Void> actualizarPassword(
            @Header("Authorization") String bearerToken,
            @Body UpdatePass request
    );

    //Reporte de Inventario Disponible
    @Headers({"Content-Type: application/json"})
    @GET("rest/v1/vista_reporte_inventario?select=*")
    Call<List<ReporteInventario>> getReporteInventario();

    //Reporte de Stock Crítico
    @Headers({"Content-Type: application/json"})
    @GET("rest/v1/vista_productos_criticos?select=*")
    Call<List<ReporteCritico>> getProductosCriticos();

    //Reporte de Movimientos por Periodo
    @Headers({"Content-Type: application/json"})
    @POST("rest/v1/rpc/reporte_movimientos_periodo")
    Call<List<ReporteMovimiento>> getMovimientosPorPeriodo(
            @Body Map<String, String> rangoFechas
    );
}