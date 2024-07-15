package com.rodcollab.mymarvelcomics.core.network

import android.content.Context
import java.io.IOException

class AndroidFileReader(
    private val context: Context,
) {
    private val classLoader = context.javaClass.classLoader

    fun getSuccessResponse(fileName: String): String {
        val inputStream = classLoader?.getResourceAsStream("response/$fileName")
            ?: throw IOException("File not found: response/$fileName")
        return inputStream.bufferedReader().use { it.readText() }
    }
}