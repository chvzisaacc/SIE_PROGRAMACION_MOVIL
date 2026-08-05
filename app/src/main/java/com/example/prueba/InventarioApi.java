package com.example.prueba;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InventarioApi {

    @GET("rest/v1/inventario?select=*,productos(id_producto,nombre_producto)")
    Call<List<Inventario>> getInventario();

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/inventario")
    Call<List<Inventario>> insertarInventario(@Body InventarioRequest inventario);

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @PATCH("rest/v1/inventario")
    Call<List<Inventario>> actualizarInventario(
            @Query("id_inventario") String filtro,
            @Body InventarioRequest inventario
    );

}