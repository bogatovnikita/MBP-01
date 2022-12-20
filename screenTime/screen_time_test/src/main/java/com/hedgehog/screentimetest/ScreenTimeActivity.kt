package com.hedgehog.screentimetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hedgehog.presentation.ui.first_screen.FirstScreenTimeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenTimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, FirstScreenTimeFragment()).commit()
    }
}