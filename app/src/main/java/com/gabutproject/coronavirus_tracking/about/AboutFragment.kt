package com.gabutproject.coronavirus_tracking.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gabutproject.coronavirus_tracking.R
import com.gabutproject.coronavirus_tracking.databinding.AboutFragmentBinding

class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AboutFragmentBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.about)

        return binding.root
    }
}