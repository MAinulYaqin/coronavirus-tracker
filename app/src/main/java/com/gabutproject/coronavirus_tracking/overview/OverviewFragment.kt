package com.gabutproject.coronavirus_tracking.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gabutproject.coronavirus_tracking.R
import com.gabutproject.coronavirus_tracking.databinding.OverviewFragmentBinding

class OverviewFragment : Fragment() {
    private lateinit var binding: OverviewFragmentBinding
    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OverviewFragmentBinding.inflate(inflater)

        viewModel = OverviewViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        updateLiveData()

        return binding.root
    }

    private fun updateLiveData() {
        viewModel.errorRequest.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                viewModel.errorRequestCompleted()
            }
        })
    }
}