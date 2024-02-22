package com.nenadic.cdapp.presentation.main_actvity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.nenadic.cdapp.domian.model.CD
import com.nenadic.cdapp.domian.repository.CDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cdRepository: CDRepository
) : ViewModel() {
val maState = cdRepository.getAllCD().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun deleteCD(cd: CD) {
        viewModelScope.launch {
            cdRepository.deleteCD(cd)
        }
    }
}