package com.gabutproject.coronavirus_tracking.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Api Urls
private const val BASE_URL = "https://api.covid19api.com/"
private const val SUMMARY = "summary"
private const val COUNTRY_MAP = "total/dayone/country/" // :param country-slug, e.g. /country/indonesia

// Http Request handler
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Method
interface Covid19ApiService {
    @GET(SUMMARY)
    suspend fun getLatestCovidData(): SummaryProperty

    @GET("$COUNTRY_MAP/indonesia")
    suspend fun getCovidMapData(): List<CountryCasesProperty>
}

// Global Object
object Covid19Api {
    val retrofitService: Covid19ApiService by lazy {
        retrofit.create(Covid19ApiService::class.java)
    }
}