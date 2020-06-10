package com.gabutproject.coronavirus_tracking.overview

import android.util.Log
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
    private val _countryCases = MutableLiveData<MutableList<List<Entry>>>()
    val countryCases: LiveData<MutableList<List<Entry>>> get() = _countryCases

    private val _requestState = MutableLiveData<StatusProperty>()
    val requestState: LiveData<StatusProperty> get() = _requestState

    // list of Entries to pass into the chart
    private val countryCasesData = mutableListOf<Entry>()
    private val countryDeathData = mutableListOf<Entry>()
    private val countryRecoveredData = mutableListOf<Entry>()

    private fun getLatestCovid19Data() {
        // lunch on UI thread, since the fetch is already on the IO thread
        uiScope.launch {
            try {
                _requestState.value = StatusProperty(RequestStatus.LOADING)
                val totalGlobalCases = Covid19Api.retrofitService.getLatestCovidData()
                val countryMapData = Covid19Api.retrofitService.getCovidMapData()
                _requestState.value = StatusProperty(RequestStatus.DONE)

                setData(totalGlobalCases, countryMapData)
            } catch (e: Exception) {
                _requestState.value = StatusProperty(RequestStatus.ERROR, "${e.message}")
            }
        }
    }

    private fun setData(
        totalGlobalCases: SummaryProperty,
        countryMapData: List<CountryCasesProperty>
    ) {
        // set values
        totalGlobalCases.Countries.forEach { item ->
            if (item.CountryCode == "ID") _totalCountryCases.value = item
        }
        // chart only accept 1..30, 31, 32 and reformat it to date with initial month
        // so init date here and update it with increased value
        var date = countryMapData[0].Date.substring(8, 10).toInt()
        countryMapData.forEachIndexed { index, item ->
            // prevData default value is 0 to prevent outOfBound index if defined as index - 1
            // if index == 0 & index - 1 it will cause error
            var prevCases = 0
            var prevRecovered = 0
            var prevDeath = 0

            if (index != 0) {
                // update data with the prevData,
                val prevData = countryMapData[index - 1]
                prevCases = prevData.Confirmed
                prevRecovered = prevData.Recovered
                prevDeath = prevData.Deaths
            }

            // prevData contains total of all of the previous data, and so the currentData
            // the last, minus currentData with prevData to get daily change
            val currentDate = date.toFloat()
            val currentCases = item.Confirmed.minus(prevCases).toFloat()
            val currentRecovered = item.Recovered.minus(prevRecovered).toFloat()
            val currentDeath = item.Deaths.minus(prevDeath).toFloat()

            countryCasesData.add(Entry(currentDate, currentCases))
            countryRecoveredData.add(Entry(currentDate, currentRecovered))
            countryDeathData.add(Entry(currentDate, currentDeath))
            date++
        }

        _countryCases.value =
            mutableListOf(countryCasesData, countryRecoveredData, countryDeathData)
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