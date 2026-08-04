package com.example.prueba;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseApi {
    //            MÓDULO PROVEEDORES

    // Consulta general de proveedores
    @GET("rest/v1/proveedores")
    Call<List<Proveedor>> getProveedores();

    // Insertar un nuevo proveedor
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/proveedores")
    Call<Void> insertarProveedor(@Body Proveedor proveedor);

    // Actualizar proveedor / Borrado Lógico (cambiar estado)
    @Headers({
            "Prefer: return=minimal"
    })
    @PATCH("rest/v1/proveedores")
    Call<Void> actualizarProveedor(@Query("id_proveedor") String idFilter, @Body Proveedor proveedor);

    //             MÓDULO ALMACENES

    // Consulta general de almacenes
    @GET("rest/v1/almacenes")
    Call<List<Almacen>> getAlmacenes();

    // Insertar un nuevo almacén
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/almacenes")
    Call<Void> insertarAlmacen(@Body Almacen almacen);

    // Actualizar almacén / Borrado Lógico (cambiar estado)
    @Headers({
            "Prefer: return=minimal"
    })
    @PATCH("rest/v1/almacenes")
    Call<Void> actualizarAlmacen(@Query("id_almacen") String idFilter, @Body Almacen almacen);

    //          MÓDULOS DEL RESTO DEL EQUIPO

    @GET("rest/v1/categorias")
    Call<List<Categoria>> getCategorias();

    @GET("rest/v1/productos")
    Call<List<Producto>> getProductos();

    @GET("rest/v1/roles")
    Call<List<Rol>> getRoles();

    @GET("rest/v1/usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("rest/v1/inventario")
    Call<List<Inventario>> getInventario();

    @GET("rest/v1/movimientos")
    Call<List<Movimiento>> getMovimientos();
}