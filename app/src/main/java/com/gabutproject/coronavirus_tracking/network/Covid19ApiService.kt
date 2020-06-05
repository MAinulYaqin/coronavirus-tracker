package com.gabutproject.coronavirus_tracking.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.covid19api.com/"
private const val SUMMARY = "summary"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface Covid19ApiService {
    @GET(SUMMARY)
    suspend fun getLatestCovidData(): Covid19ApiProperty
}

object Covid19Api {
    val retrofitService: Covid19ApiService by lazy {
        retrofit.create(Covid19ApiService::class.java)
    }
}