package com.nenadic.cdapp.domian.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CD(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    val picture:String="",
    val albumName:String = "",
    val artistName:String = "",
    val releaseDate:Long = 0L,
    val labelOrigin:String = ""
)
