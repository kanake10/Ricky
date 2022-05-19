package com.example.ricky.di

import com.example.ricky.api.RickyApi
import com.example.ricky.core.Constants.BASE_URL
import com.example.ricky.data.RickyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)

@Module
object AppModule{

    @Provides
    fun providesHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun providesOkhttpClient(interceptor : HttpLoggingInterceptor) : OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(50, TimeUnit.SECONDS)
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
        return okHttpClient.build()
    }

    @Provides
    fun providesConverterFactory() : Converter.Factory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun providesRetrofit(baseUrl: String, converterFactory: Converter.Factory, client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
    }

    @Provides
    fun providesRetrofitService(retrofit: Retrofit) : RickyApi {
        return retrofit.create(RickyApi::class.java)
    }

    @Provides
    fun providesRickyRepository(rickyApi: RickyApi) : RickyRepository {
        return RickyRepository(rickyApi)
    }

    @Provides
    fun providesBaseUrl() : String{
        return BASE_URL
    }
}