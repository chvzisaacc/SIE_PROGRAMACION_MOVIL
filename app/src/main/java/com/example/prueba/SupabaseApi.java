package com.example.prueba;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SupabaseApi {

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
}