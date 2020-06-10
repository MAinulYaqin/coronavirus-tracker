package com.gabutproject.coronavirus_tracking.overview

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gabutproject.coronavirus_tracking.R
import com.gabutproject.coronavirus_tracking.databinding.OverviewFragmentBinding
import com.gabutproject.coronavirus_tracking.utils.DayAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter

class OverviewFragment : Fragment() {
    private lateinit var binding: OverviewFragmentBinding
    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // define binding & viewModel
        binding = OverviewFragmentBinding.inflate(inflater)
        viewModel = OverviewViewModel()

        // define viewModel in the xml & set lifeCycleOwner to this fragment
        // so the fragment can keep an eye whenever data changed
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // update current application state
        updateLiveData()
        // setup overflow menu
        setHasOptionsMenu(true)

        // error handler, since the error message used include file
        // we can't define any method from viewModel there
        binding.errorView.retryButton.setOnClickListener {
            viewModel.retryConnection()
        }

        // init totalCasesChart
        totalCasesChart()

        // return view
        return binding.root
    }

    private fun totalCasesChart(
        casesEntries: List<Entry> = listOf(),
        recoveredEntries: List<Entry> = listOf(),
        deathEntries: List<Entry> = listOf()
    ) {
        val chartView = binding.chartLine
        val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(chartView)
        val xAxis = chartView.xAxis
        val casesDataSet = LineDataSet(casesEntries, "Kasus")
        val deathDataSet = LineDataSet(deathEntries, "Meninggal")
        val recoveredDataSet = LineDataSet(recoveredEntries, "Sembuh")

        val data = chartView.data
        val lineData = LineData(casesDataSet, recoveredDataSet, deathDataSet)

        // some adjustments
        xAxis.granularity = 1f
        xAxis.labelCount = 5
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = xAxisFormatter
        // make the right YAxis gone,
        chartView.axisRight.isEnabled = false
        // width
        casesDataSet.lineWidth = 4f
        recoveredDataSet.lineWidth = 4f
        deathDataSet.lineWidth = 4f
        // draw circles
        casesDataSet.setDrawCircles(false)
        recoveredDataSet.setDrawCircles(false)
        deathDataSet.setDrawCircles(false)
        // color
        casesDataSet.color = ContextCompat.getColor(requireContext(), R.color.pink)
        recoveredDataSet.color = ContextCompat.getColor(requireContext(), R.color.green)
        deathDataSet.color = ContextCompat.getColor(requireContext(), R.color.grey)

        if (chartView.data == null) {
            chartView.data = lineData
        } else {
            // line styling
            lineData.dataSets.forEach { line -> line.setDrawValues(false) }

            // update dataSet
            // tell the chart if the data has changed
            chartView.data = lineData
            data.notifyDataChanged()
            chartView.notifyDataSetChanged()
            chartView.invalidate()

            chartView.animateXY(2000, 2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overview_overflow_menu, menu)
    }

    // update data listener
    private fun updateLiveData() {
        // request state listener
        viewModel.requestState.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.status == RequestStatus.ERROR) {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // chart data listener
        viewModel.countryCases.observe(viewLifecycleOwner, Observer {
            it?.let {
                totalCasesChart(it[0], it[1], it[2])
            }
        })
    }
}