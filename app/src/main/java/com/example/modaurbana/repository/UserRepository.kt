package com.example.modaurbana.repository

import android.content.Context
import com.example.modaurbana.data.remote.ApiService
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.UserDto

class UserRepository(context: Context) {

    private val apiService: ApiService = RetrofitClient
        .create(context)
        .create(ApiService::class.java)

    // EP3 recomienda /me; mientras lo conectas, mantenemos fallback a ID
    suspend fun fetchUser(id: Int = 1): Result<UserDto> = try {
        val user = apiService.getUserById(id)
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Cuando tengas /me funcional, usa esto:
    // suspend fun fetchMe(): Result<UserDto> = try {
    //     val me = apiService.getCurrentUser()
    //     Result.success(me)
    // } catch (e: Exception) {
    //     Result.failure(e)
    // }
}
