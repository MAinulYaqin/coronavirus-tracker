package com.gabutproject.coronavirus_tracking.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabutproject.coronavirus_tracking.network.*
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class RequestStatus { LOADING, DONE, ERROR }

class OverviewViewModel : ViewModel() {

    // thread
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    // Live data
    // total country cases LiveData
    private val _totalCountryCases = MutableLiveData<TotalCountryCasesProperty>()
    val totalCountryCases: LiveData<TotalCountryCasesProperty> get() = _totalCountryCases

    // country map data, since i don't know how to call it
    private val _countryCases = MutableLiveData<MutableList<Entry>>()
    val countryCases: LiveData<MutableList<Entry>> get() = _countryCases

    private val _requestState = MutableLiveData<StatusProperty>()
    val requestState: LiveData<StatusProperty> get() = _requestState

    private val countryCasesData = mutableListOf<Entry>()

    private fun getLatestCovid19Data() {
        // lunch on UI thread, since the fetch is already on the IO thread
        uiScope.launch {
            try {
                _requestState.value = StatusProperty(RequestStatus.LOADING)
                val totalGlobalCases = Covid19Api.retrofitService.getLatestCovidData()
                val countryMapData = Covid19Api.retrofitService.getCovidMapData()
                _requestState.value = StatusProperty(RequestStatus.DONE)

                setData(totalGlobalCases, countryMapData)
                _countryCases.value = countryCasesData
            } catch (e: Exception) {
                _requestState.value = StatusProperty(RequestStatus.ERROR, "${e.message}")
            }
        }
    }

    private suspend fun setData(
        totalGlobalCases: SummaryProperty,
        countryMapData: List<CountryCasesProperty>
    ) {
        // set values
        totalGlobalCases.Countries.forEach { item ->
            if (item.CountryCode == "ID") _totalCountryCases.value = item
        }

        var date = countryMapData[0].Date.substring(8, 10).toInt()
        countryMapData.forEach { item ->
            countryCasesData.add(Entry(date.toFloat(), item.Deaths.toFloat()))
            date++
        }
    }

    init {
        getLatestCovid19Data()
    }

    // getter function to call from xml
    fun retryConnection() {
        getLatestCovid19Data()
    }

    /**
     * Default data class StatusProperty,
     *
     * @param status Enum
     * @param message String
     *  error/success message to send to the fragment
     */
    data class StatusProperty(val status: RequestStatus, val message: String? = null)
}