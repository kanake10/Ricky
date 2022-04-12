package com.example.ricky.api

import com.example.ricky.core.Constants.END_POINT
import com.example.ricky.models.RickyMortyCharacter
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET(END_POINT)
    suspend fun getCharacters(
        @Query
            ("page") query: Int) : RickyMortyCharacter
}