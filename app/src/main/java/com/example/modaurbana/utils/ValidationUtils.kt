package com.example.modaurbana.utils

object ValidationUtils {
    fun isValidEmail(s: String) = s.contains("@") && s.length >= 6
    fun isValidPassword(s: String) = s.length >= 6
    fun isNotBlank(s: String) = s.trim().isNotEmpty()
}
