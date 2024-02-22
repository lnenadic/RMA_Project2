package com.nenadic.cdapp.domian.interfaces

import com.nenadic.cdapp.domian.model.CD

interface OnItemClick {
    fun onItemClick(cd: CD){}
    fun onDeleteClick(cd: CD){}
    fun onEditClick(cd: CD){}
}