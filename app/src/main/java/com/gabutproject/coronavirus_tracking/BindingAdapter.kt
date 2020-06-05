package com.gabutproject.coronavirus_tracking

import android.view.View
import androidx.databinding.BindingAdapter
import com.gabutproject.coronavirus_tracking.overview.RequestStatus

@BindingAdapter("requestStatus")
fun requestStatus(view: View, status: RequestStatus?) {
    status?.let {
        when (status) {
            RequestStatus.ERROR -> {
                if (view.id == R.id.cards_group) {
                    view.visibility = View.GONE
                } else {
                    view.visibility = View.VISIBLE
                }
            }

            RequestStatus.DONE -> {
                if (view.id == R.id.error_view) {
                    view.visibility = View.GONE
                } else {
                    view.visibility = View.VISIBLE
                }
            }

            RequestStatus.LOADING -> {
                if (view.id == R.id.cards_group || view.id == R.id.error_view) {
                    view.visibility = View.GONE
                } else {
                    view.visibility = View.VISIBLE
                }
            }
        }
    }
}