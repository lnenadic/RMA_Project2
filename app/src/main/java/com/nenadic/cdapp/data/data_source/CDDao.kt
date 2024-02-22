package com.nenadic.cdapp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nenadic.cdapp.domian.model.CD
import kotlinx.coroutines.flow.Flow

@Dao
interface CDDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCD(cd: CD)

    @Delete
    suspend fun deleteCD(cd: CD)

    @Query("SELECT * FROM CD WHERE id=:id")
    fun getCDById(id: Int): CD

    @Query("SELECT * FROM CD")
    fun getAllCD(): Flow<List<CD>>


}