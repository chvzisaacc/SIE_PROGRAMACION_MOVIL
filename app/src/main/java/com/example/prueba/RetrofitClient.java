package com.example.prueba;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://azcebnuwnwkeklopckzb.supabase.co/";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImF6Y2VibnV3bndrZWtsb3Bja3piIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODU0NDIyMTMsImV4cCI6MjEwMTAxODIxM30.4Vjkxy3qdIG3vDnyc8AWnaK64ssvnQaTJQ1KL-UoVU8";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // Creamos un interceptor para añadir los Headers obligatorios de Supabase
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            Request newRequest = originalRequest.newBuilder()
                                    .header("apikey", SUPABASE_ANON_KEY)
                                    .header("Authorization", "Bearer " + SUPABASE_ANON_KEY)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }).build();

            // Construimos Retrofit asociándole el OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient) // <-- Vinculamos las cabeceras aquí
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}