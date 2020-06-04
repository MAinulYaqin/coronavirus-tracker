package com.gabutproject.coronavirus_tracking.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabutproject.coronavirus_tracking.network.Covid19Api
import com.gabutproject.coronavirus_tracking.network.GlobalCasesProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {

    // thread
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    // Live data
    private val _globalCases = MutableLiveData<GlobalCasesProperty>()
    val globalCases: LiveData<GlobalCasesProperty> get() = _globalCases

    private val _errorRequest = MutableLiveData<ErrorProperty>()
    val errorRequest: LiveData<ErrorProperty> get() = _errorRequest

    private fun getLatestCovid19Data() {
        uiScope.launch {
            try {
                val result = Covid19Api.retrofitService.getLatestCovidData()
                _globalCases.value = result.Global
            } catch (e: Exception) {
                _errorRequest.value = ErrorProperty(true, e.message.toString())
            }
        }
    }

    init {
        getLatestCovid19Data()
    }

    fun errorRequestCompleted() {
        _errorRequest.value = null
    }

    data class ErrorProperty(val status: Boolean, val message: String)
}