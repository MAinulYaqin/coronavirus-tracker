package com.gabutproject.coronavirus_tracking

import android.view.View
import androidx.databinding.BindingAdapter
import com.gabutproject.coronavirus_tracking.overview.RequestStatus

@BindingAdapter("requestStatus")
fun requestStatus(view: View, status: RequestStatus?) {
    status?.let {
        when (status) {
            RequestStatus.ERROR -> {

            }
        }
    }
}