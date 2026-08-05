package com.example.prueba;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriaApi {

    @GET("rest/v1/categorias")
    Call<List<Categoria>> getCategorias();

}