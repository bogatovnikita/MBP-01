package com.hedgehog.screentimetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hedgehog.presentation.ui.first_screen.FirstScreenTimeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenTimeTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_time_test)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, FirstScreenTimeFragment()).commit()
    }
}