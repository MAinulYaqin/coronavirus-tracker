package com.gabutproject.coronavirus_tracking.overview

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gabutproject.coronavirus_tracking.R
import com.gabutproject.coronavirus_tracking.about.AboutActivity
import com.gabutproject.coronavirus_tracking.databinding.MainActivityBinding
import com.gabutproject.coronavirus_tracking.utils.DayAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: OverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        viewModel = OverviewViewModel()

        // define viewModel in the xml & set lifeCycleOwner to this fragment
        // so the fragment can keep an eye whenever data changed
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // update current application state
        updateLiveData()

        // error handler, since the error message used include file
        // we can't define any method from viewModel there
        binding.errorView.retryButton.setOnClickListener {
            viewModel.retryConnection()
        }

        // init totalCasesChart
        totalCasesChart()
    }

    private fun totalCasesChart(
        casesEntries: List<Entry> = listOf(),
        recoveredEntries: List<Entry> = listOf(),
        deathEntries: List<Entry> = listOf()
    ) {
        // TODO: Reduces this shit
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
        casesDataSet.color = ContextCompat.getColor(applicationContext, R.color.pink)
        recoveredDataSet.color = ContextCompat.getColor(applicationContext, R.color.green)
        deathDataSet.color = ContextCompat.getColor(applicationContext, R.color.grey)

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

    // update data listener
    private fun updateLiveData() {
        // request state listener
        viewModel.requestState.observe(this, Observer {
            it?.let {
                if (it.status == RequestStatus.ERROR) {
                    Toast.makeText(applicationContext, "Error: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        // chart data listener
        viewModel.countryCases.observe(this, Observer {
            it?.let {
                totalCasesChart(it[0], it[1], it[2])
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overview_overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.aboutActivity -> {
                startActivity(Intent(this, AboutActivity::class.java))

                true
            }
            else -> true
        }
    }
}