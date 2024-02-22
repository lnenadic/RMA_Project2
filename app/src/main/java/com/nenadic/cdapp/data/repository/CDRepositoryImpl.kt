package com.nenadic.cdapp.data.repository

import com.nenadic.cdapp.data.data_source.CDDao
import com.nenadic.cdapp.domian.model.CD
import com.nenadic.cdapp.domian.repository.CDRepository
import kotlinx.coroutines.flow.Flow

class CDRepositoryImpl(private val cdDao: CDDao) : CDRepository {
    override suspend fun insertCD(cd: CD) {
        cdDao.insertCD(cd)
    }

    override suspend fun deleteCD(cd: CD) {
        cdDao.deleteCD(cd)
    }


    override fun getAllCD(): Flow<List<CD>> {
        return cdDao.getAllCD()
    }
}