package com.nenadic.cdapp.domian.repository

import com.nenadic.cdapp.domian.model.CD
import kotlinx.coroutines.flow.Flow

interface CDRepository {
    suspend fun insertCD(cd: CD)

    suspend fun deleteCD(cd: CD)

    fun getAllCD(): Flow<List<CD>>
}