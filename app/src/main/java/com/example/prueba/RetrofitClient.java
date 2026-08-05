package com.example.prueba;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://azcebnuwnwkeklopckzb.supabase.co/";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImF6Y2VibnV3bndrZWtsb3Bja3piIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODU0NDIyMTMsImV4cCI6MjEwMTAxODIxM30.4Vjkxy3qdIG3vDnyc8AWnaK64ssvnQaTJQ1KL-UoVU8";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // Interceptor que añade apikey a todas las peticiones
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder()
                            .header("apikey", SUPABASE_ANON_KEY);

                    // Solo agrega Authorization con el anon key SI la petición
                    // no trae ya su propio header Authorization.
                    if (originalRequest.header("Authorization") == null) {
                        builder.header("Authorization", "Bearer " + SUPABASE_ANON_KEY);
                    }

                    return chain.proceed(builder.build());
                }
            };

            //imprime en Logcat el request y response completos por si llegamos a tener un error
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}