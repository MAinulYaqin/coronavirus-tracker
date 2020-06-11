package com.gabutproject.coronavirus_tracking.about

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.gabutproject.coronavirus_tracking.R
import com.gabutproject.coronavirus_tracking.databinding.AboutActivityBinding
import kotlinx.android.synthetic.main.about_app_card.view.*

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: AboutActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.about_activity)

        binding.lifecycleOwner = this

        // set appBar's title
        setTitle(R.string.about)

        // item listener
        onItemClick()
    }

    private fun startBrowserActivity(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun onItemClick() {
        binding.aboutCard.star_github_item.setOnClickListener {
            startBrowserActivity("https://github.com/ai-null/coronavirus-tracker.git")
        }

        binding.aboutCard.license_item.setOnClickListener {
            startBrowserActivity("https://raw.githubusercontent.com/ai-null/coronavirus-tracker/master/LICENSE")
        }

        binding.aboutCard.api_item.setOnClickListener {
            startBrowserActivity("https://covid19api.com")
        }
    }
}