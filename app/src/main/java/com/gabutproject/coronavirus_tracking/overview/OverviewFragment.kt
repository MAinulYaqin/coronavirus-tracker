package com.gabutproject.coronavirus_tracking.overview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gabutproject.coronavirus_tracking.R
import com.gabutproject.coronavirus_tracking.databinding.OverviewFragmentBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.error_message.view.*

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

        // update current application state
        updateLiveData()
        // setup overflow menu
        setHasOptionsMenu(true)

        // error handler
        binding.errorView.retryButton.setOnClickListener {
            viewModel.retryConnection()
        }

        testChartInit()
        testChart2()

        // return view
        return binding.root
    }


    private fun testChartInit() {
        val chartView = binding.chartBar
        val entries1: List<BarEntry> =
            listOf(BarEntry(0f, 4f), BarEntry(1f, 1f), BarEntry(2f, 3f))
        val entries2: List<BarEntry> =
            listOf(BarEntry(0f, 4f), BarEntry(1f, 5f), BarEntry(2f, 5f))

        val group1 = BarDataSet(entries1, "batman")
        val group2 = BarDataSet(entries2, "madman")

        group2.color = Color.GRAY

        val data = BarData(group1, group2)

        chartView.animateXY(2000, 2000)

        val groupSpace = 0.06f
        val barSpace = 0.02f
        val barWith = 0.45f

        data.barWidth = barWith
        chartView.data = data
        chartView.groupBars(-0.5f, groupSpace, barSpace)
        // (0.02 + 0.45) * 2 = 94 + 6
    }

    private fun testChart2() {
        val chartView = binding.chartLine

        val entries: List<Entry> = listOf(Entry(0f, 1f), Entry(1f, 2f), Entry(2f, 3f))
        val lineDataSet = LineDataSet(entries, "Sembuh")

        chartView.animateXY(2000, 2000)

        chartView.data = LineData(lineDataSet)
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
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}