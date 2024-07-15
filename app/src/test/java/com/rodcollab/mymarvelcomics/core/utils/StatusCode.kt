package com.rodcollab.mymarvelcomics.core.utils

enum class StatusCode(val code: Int, val message: String) {
    OK(code = 200, "Ok"),
    UNAUTHORIZED(code = 401, message = "Unauthorized"),
}