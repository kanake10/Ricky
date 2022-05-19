package com.example.ricky.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.ricky.data.RickyRepository
import com.example.ricky.models.CharacterData

@HiltViewModel
class RickyViewModel @Inject constructor
    (private val rickyRepository: RickyRepository) : ViewModel() {

    fun getListData(): Flow<PagingData<CharacterData>> {
        return rickyRepository.getCharacter().cachedIn(viewModelScope)
    }
}