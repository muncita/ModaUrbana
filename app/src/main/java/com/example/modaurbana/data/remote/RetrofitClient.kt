package com.example.modaurbana.data.remote

import com.example.modaurbana.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // IMPORTANTE: debe terminar en "/"
    private const val BASE_URL = "https://modaurbanaapp-api.onrender.com/api/"

    // Interceptor para ver las peticiones/respuestas en Logcat
    private val logging by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Construye un OkHttpClient.
     * Si le pasas un SessionManager, puede agregar también el AuthInterceptor.
     */
    private fun buildOkHttp(sessionManager: SessionManager? = null): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(logging)

        // Si quieres que las peticiones lleven el token automáticamente:
        // descomenta esta parte y asegúrate de pasar el SessionManager
        // en RetrofitClient.create(sessionManager)
        //
        // if (sessionManager != null) {
        //     builder.addInterceptor(AuthInterceptor(sessionManager))
        // }

        return builder.build()
    }

    /**
     * Crea una instancia de ApiService.
     * - Si no pasas sessionManager, crea un cliente "normal" (sin AuthInterceptor).
     * - Si pasas sessionManager, puedes usar AuthInterceptor para agregar el token.
     */
    fun create(sessionManager: SessionManager? = null): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildOkHttp(sessionManager))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    /**
     * Instancia por defecto sin AuthInterceptor.
     * Esto mantiene compatible el código que ya usa RetrofitClient.instance
     */
    val instance: ApiService by lazy {
        create(null)
    }
}
