package com.example.modaurbana.data.remote

import com.example.modaurbana.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor que agrega el header Authorization solo en endpoints privados.
 * Diferencia: validación más estricta de rutas y logs de depuración.
 */
class AuthInterceptor(private val session: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        // Evitar rutas públicas
        if (path.endsWith("/auth/login") || path.endsWith("/auth/signup")) {
            println("🌐 [AuthInterceptor] Ruta pública: $path (sin token)")
            return chain.proceed(request)
        }

        // Obtener token sincronamente
        val token = runBlocking { session.getAuthToken() }

        return if (!token.isNullOrEmpty()) {
            println("🔑 [AuthInterceptor] Token agregado a $path")
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            println("⚠️ [AuthInterceptor] No hay token guardado")
            chain.proceed(request)
        }
    }
}
