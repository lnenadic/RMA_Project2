package com.nenadic.cdapp.domian.utils.constants

import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val DATE_FORMATE = "dd/MM/yyyy"
const val CD_ID = "cd_id"
const val DATE = "cd_date"
const val IMAGE = "cd_image"
const val ARTIST_NAME = "cd_artist_name"
const val ALBUM_NAME = "cd_album_name"
const val ORIGIN = "cd_origin"


fun getDateInMilliseconds(etDate: EditText): Long {
    val dateString = etDate.text.toString()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    return try {
        val date = dateFormat.parse(dateString)
        date?.time ?: -1
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}

fun formatDateFromLong(dateInLong: Long): String {
    val dateFormat = SimpleDateFormat(DATE_FORMATE, Locale.ENGLISH)
    val date = Date(dateInLong)
    return dateFormat.format(date)
}


fun dateStringToLong(dateString: String, dateFormat: String): Long {
    return try {
        val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        val date = sdf.parse(dateString)
        date?.time ?: -1
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}



