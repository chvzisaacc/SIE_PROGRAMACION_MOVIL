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

    // ==========================================
    //            MÓDULO PROVEEDORES
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

    @GET("rest/v1/inventario?select=*")
    Call<List<Inventario>> getInventario();

    @GET("rest/v1/movimientos?select=*")
    Call<List<Movimiento>> getMovimientos();
}