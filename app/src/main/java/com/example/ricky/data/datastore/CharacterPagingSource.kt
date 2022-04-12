package com.example.ricky.data.datastore

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ricky.api.RetrofitService
import com.example.ricky.models.CharacterData

class CharacterPagingSource (private val retrofitService: RetrofitService) : PagingSource<Int, CharacterData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterData> {
        return try {
            val nextPage : Int = params.key ?: 1
            val response = retrofitService.getCharacters(nextPage)
            var nextPageNumber : Int? = null

            if (response.info.next != null){
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }

            LoadResult.Page(data = response.results, prevKey = null, nextKey = nextPageNumber)

        }catch(e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterData>): Int? {
        return state.anchorPosition
    }
}