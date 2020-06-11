package com.gabutproject.coronavirus_tracking.about

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.gabutproject.coronavirus_tracking.R
import com.gabutproject.coronavirus_tracking.databinding.AboutActivityBinding

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<AboutActivityBinding>(this, R.layout.about_activity)

        binding.lifecycleOwner = this

        binding.startGithubItem.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ai-null"))
            startActivity(browserIntent)
        }

        setTitle(R.string.about)
    }
}