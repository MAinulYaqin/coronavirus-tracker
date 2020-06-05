package com.gabutproject.coronavirus_tracking.overview

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabutproject.coronavirus_tracking.network.CountryCasesProperty
import com.gabutproject.coronavirus_tracking.network.Covid19Api
import com.gabutproject.coronavirus_tracking.network.GlobalCasesProperty
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
    // total global cases around the world
    private val _totalGlobalCases = MutableLiveData<GlobalCasesProperty>()
    val totalGlobalCases: LiveData<GlobalCasesProperty> get() = _totalGlobalCases

    // total country cases LiveData
    private val _totalCountryCases = MutableLiveData<List<CountryCasesProperty>>()
    val totalCountryCases: LiveData<List<CountryCasesProperty>> get() = _totalCountryCases

    private val _requestState = MutableLiveData<StatusProperty>()
    val requestState: LiveData<StatusProperty> get() = _requestState

    private fun getLatestCovid19Data() {
        // lunch on UI thread, since the fetch is already on the IO thread
        uiScope.launch {
            try {
                _requestState.value = StatusProperty(RequestStatus.LOADING)
                val result = Covid19Api.retrofitService.getLatestCovidData()
                _requestState.value = StatusProperty(RequestStatus.DONE)

                _totalGlobalCases.value = result.Global
                _totalCountryCases.value = result.Countries
            } catch (e: Exception) {
                _requestState.value = StatusProperty(RequestStatus.ERROR, "Error: ${e.message}")
                _totalCountryCases.value = listOf()
            }
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