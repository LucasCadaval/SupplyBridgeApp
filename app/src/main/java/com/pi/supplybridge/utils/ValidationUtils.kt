package com.pi.supplybridge.utils

import br.com.caelum.stella.validation.CNPJValidator
import br.com.caelum.stella.validation.InvalidStateException


object ValidationUtils {
    fun isValidCNPJ(cnpj: String): Boolean {
        return try {
            val validator = CNPJValidator()
            validator.assertValid(cnpj)
            true
        } catch (e: InvalidStateException) {
            false
        }
    }
}