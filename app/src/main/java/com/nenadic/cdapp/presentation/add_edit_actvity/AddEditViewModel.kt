package com.nenadic.cdapp.presentation.add_edit_actvity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nenadic.cdapp.domian.model.CD
import com.nenadic.cdapp.domian.repository.CDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val cdRepository: CDRepository) : ViewModel() {

    fun insertCD(cd: CD) {
        viewModelScope.launch {
            cdRepository.insertCD(cd)
        }
    }
}