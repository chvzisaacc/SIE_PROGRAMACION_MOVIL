package com.example.prueba;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MovimientoApi {

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/movimientos")
    Call<List<Movimiento>> insertarMovimiento(
            @Body MovimientoRequest movimiento
    );

}