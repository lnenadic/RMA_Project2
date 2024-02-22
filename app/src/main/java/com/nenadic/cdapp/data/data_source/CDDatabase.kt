package com.nenadic.cdapp.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nenadic.cdapp.domian.model.CD


@Database(entities = [CD::class], version = 1, exportSchema = false)
abstract class CDDatabase:RoomDatabase(){
  abstract val cdDao:CDDao
    companion object {
        val DB_NAME = "cd_db"
    }
}