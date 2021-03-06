package com.example.ricky.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ricky.api.RickyApi
import com.example.ricky.data.datastore.CharacterPagingSource
import com.example.ricky.models.CharacterData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RickyRepository @Inject constructor
    (private val rickyApi: RickyApi) {

    fun getCharacter() : Flow<PagingData<CharacterData>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 30),
            pagingSourceFactory = {
                CharacterPagingSource(rickyApi)
            }
        ).flow
    }
}