package com.rodcollab.mymarvelcomics.core.database

import androidx.room.withTransaction

class DefaultTransactionProvider(
    private val db: AppDatabase,
) : TransactionProvider {
    override suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }
}

interface TransactionProvider {
    suspend fun <R> runAsTransaction(block: suspend () -> R): R
}