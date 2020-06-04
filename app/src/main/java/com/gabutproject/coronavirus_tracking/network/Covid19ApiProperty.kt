package com.gabutproject.coronavirus_tracking.network

data class GlobalCasesProperty(
    val NewConfirmed: Int,
    val TotalConfirmed: Int,
    val NewDeaths: Int,
    val TotalDeaths: Int,
    val NewRecovered: Int,
    val TotalRecovered: Int
)

data class CountryCasesProperty(
    val Country: String,
    val CountryCode: String,
    val NewConfirmed: Int,
    val TotalConfirmed: Int,
    val NewDeaths: Int,
    val TotalDeaths: Int,
    val NewRecovered: Int,
    val TotalRecovered: Int,
    val Date: String
)

data class Covid19ApiProperty(
    val Global: GlobalCasesProperty,
    val Countries: List<CountryCasesProperty>
)