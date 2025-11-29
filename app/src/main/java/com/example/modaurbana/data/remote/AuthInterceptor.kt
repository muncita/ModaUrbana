package com.example.modaurbana.data.remote

import com.example.modaurbana.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val session: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath



        if (path.endsWith("/auth/login") || path.endsWith("/auth/signup")) {
            println("Ruta pública: $path (sin token)")
            return chain.proceed(request)
        }
        val token = runBlocking { session.getAuthToken() }

        return if (!token.isNullOrEmpty()) {
            println("Token agregado a $path")
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            println("No hay token guardado")
            chain.proceed(request)
        }
    }
}
