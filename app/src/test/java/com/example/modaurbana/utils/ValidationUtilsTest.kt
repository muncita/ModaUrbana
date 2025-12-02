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
        // Password válido
        assertTrue(ValidationUtils.isValidPassword("123456"))

        // Password muy corto
        assertFalse(ValidationUtils.isValidPassword("123"))

        // isNotBlank
        assertTrue(ValidationUtils.isNotBlank(" hola "))
        assertFalse(ValidationUtils.isNotBlank("   "))
    }
}

