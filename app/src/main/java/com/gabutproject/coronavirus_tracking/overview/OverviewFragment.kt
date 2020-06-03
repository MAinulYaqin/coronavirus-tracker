package com.gabutproject.coronavirus_tracking.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gabutproject.coronavirus_tracking.databinding.OverviewFragmentBinding

class OverviewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = OverviewFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
}