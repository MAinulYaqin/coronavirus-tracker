package com.gabutproject.coronavirus_tracking.network

data class TotalGlobalCasesProperty(
    val NewConfirmed: Int,
    val TotalConfirmed: Int,
    val NewDeaths: Int,
    val TotalDeaths: Int,
    val NewRecovered: Int,
    val TotalRecovered: Int
)

data class TotalCountryCasesProperty(
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

data class CountryCasesProperty(
    val Country: String,
    val CountryCode: String,
    val Province: String,
    val City: String,
    val CityCode: String,
    val Lat: String,
    val Lon: String,
    val Confirmed: Int,
    val Deaths: Int,
    val Recovered: Int,
    val Active: Int,
    val Date: String
)

data class SummaryProperty(
    val Global: TotalGlobalCasesProperty,
    val Countries: List<TotalCountryCasesProperty>
)