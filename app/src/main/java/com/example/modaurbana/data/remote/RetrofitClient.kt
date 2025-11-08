package com.example.modaurbana.data.remote

import com.example.modaurbana.data.local.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit centralizado.
 * - Configura el interceptor de autenticación (envía el token JWT automáticamente).
 * - Usa la URL base del backend (Xano).
 */
object RetrofitClient {

    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW/"

    // Creamos el cliente HTTP con el interceptor
    private val okHttpClient: OkHttpClient by lazy {
        val context = SessionManager.appContext
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(SessionManager(context)))
            .build()
    }

    // Retrofit configurado con el cliente autenticado
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
