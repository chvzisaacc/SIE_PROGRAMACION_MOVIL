package com.example.prueba;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductoApi {

    @GET("rest/v1/productos")
    Call<List<Producto>> getProductos();

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/productos")
    Call<List<Producto>> insertarProducto(@Body ProductoRequest producto);

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @PATCH("rest/v1/productos")
    Call<List<Producto>> actualizarProducto(
            @Query("id_producto") String idProducto,
            @Body ProductoRequest producto
    );

}