package com.rodcollab.mymarvelcomics.core.ui

data class UiState<T>(
    val isLoading: Boolean = false,
    val model: T? = null,
    val errorMsg: String? = null,
    val askFirst: String? = null,
    val confirm: Boolean = false
)
