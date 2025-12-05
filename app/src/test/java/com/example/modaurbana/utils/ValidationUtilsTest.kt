package com.example.modaurbana.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun `isValidEmail devuelve true para email valido`() {
        val result = ValidationUtils.isValidEmail("user@test.com")
        assertTrue(result)
    }

    @Test
    fun `isValidEmail devuelve false para email sin arroba o muy corto`() {
        val sinArroba = ValidationUtils.isValidEmail("usertest.com")
        val muyCorto = ValidationUtils.isValidEmail("a@b.c")

        assertFalse(sinArroba)
        assertFalse(muyCorto)
    }

    @Test
    fun `isValidPassword e isNotBlank funcionan correctamente`() {
        assertTrue(ValidationUtils.isValidPassword("123456"))
        assertFalse(ValidationUtils.isValidPassword("123"))
        assertTrue(ValidationUtils.isNotBlank(" hola "))
        assertFalse(ValidationUtils.isNotBlank("   "))
    }
}

