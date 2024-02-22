package com.nenadic.cdapp.domian.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.nenadic.cdapp.R
import com.nenadic.cdapp.databinding.CdItemListBinding
import com.nenadic.cdapp.domian.interfaces.OnItemClick
import com.nenadic.cdapp.domian.model.CD
import com.nenadic.cdapp.domian.utils.constants.formatDateFromLong

class CDAdapter(private val dataSet: List<CD>, private val mContext: Context) :
    ArrayAdapter<CD>(mContext, R.layout.cd_item_list, dataSet), View.OnClickListener {
    private var listner: OnItemClick? = null

    private class ViewHolder {
        lateinit var tvAlbumName: TextView
        lateinit var tvArtistName: TextView
        lateinit var tvReleaseDate: TextView
        lateinit var tvLabelOrigin: TextView
        lateinit var ivCd: ImageView
        lateinit var ivEdit: ImageView
        lateinit var ivDelete: ImageView
    }

    fun initConstructor(onItemClick: OnItemClick) {
        listner = onItemClick
    }

    override fun onClick(v: View) {
        val position = v.tag as Int
        val dataModel = getItem(position)
        if (dataModel != null) {
            listner?.onItemClick(dataModel)
        }

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder: ViewHolder
        var convertViewVar = convertView

        if (convertViewVar == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertViewVar = inflater.inflate(R.layout.cd_item_list, parent, false)
            val binder = CdItemListBinding.bind(convertViewVar)

            viewHolder.tvAlbumName = binder.tvAlbumName
            viewHolder.tvArtistName = binder.tvArtistName
            viewHolder.tvReleaseDate = binder.tvReleaseDate
            viewHolder.tvLabelOrigin = binder.tvLabelOrigin
            viewHolder.ivCd = binder.ivCd
            viewHolder.ivEdit = binder.ivEdit
            viewHolder.ivDelete = binder.ivDelete

            convertViewVar.tag = viewHolder
        } else {
            viewHolder = convertViewVar.tag as ViewHolder
        }

        val dataModel = getItem(position)
        val dataSet = dataModel?.releaseDate?.let { formatDateFromLong(it) }
        viewHolder.tvAlbumName.text = buildString {
            append("Album Name: ")
            append(dataModel?.albumName)
        }
        viewHolder.tvArtistName.text = buildString {
            append("Artist Name: ")
            append(dataModel?.artistName)
        }
        viewHolder.tvReleaseDate.text =
            buildString {
                append("Release Date: ")
                append(dataSet)
            }

        viewHolder.tvLabelOrigin.text =
            buildString {
                append("Origin: ")
                append(dataModel?.labelOrigin)
            }

        viewHolder.ivCd.setImageURI(dataModel?.picture?.toUri())
        viewHolder.ivCd.tag = position
        buttonClick(viewHolder, dataModel)
        return convertViewVar!!
    }

    private fun buttonClick(holder: ViewHolder, dataModel: CD?) {
        with(holder) {
            ivEdit.setOnClickListener {
                dataModel?.let {
                    listner?.onEditClick(it)
                }
            }
            ivDelete.setOnClickListener {
                dataModel?.let {
                    listner?.onDeleteClick(it)
                }
            }
        }
    }
}

