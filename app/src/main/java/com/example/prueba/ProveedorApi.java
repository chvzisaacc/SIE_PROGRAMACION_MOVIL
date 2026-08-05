package com.example.prueba;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProveedorApi {

    @GET("rest/v1/proveedores")
    Call<List<Proveedor>> getProveedores();

}