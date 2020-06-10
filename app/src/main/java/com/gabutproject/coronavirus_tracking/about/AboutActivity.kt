package com.gabutproject.coronavirus_tracking.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabutproject.coronavirus_tracking.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.about)

        setContentView(R.layout.about_activity)
    }
}