package com.gabutproject.coronavirus_tracking.overview

import android.os.Bundle
import android.view.*
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

        // setup overflow menu
        setHasOptionsMenu(true)
        // return view
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overview_overflow_menu, menu)
    }

    // update data listener
    private fun updateLiveData() {
        // request state handler
        viewModel.requestState.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.status == RequestStatus.ERROR) {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}