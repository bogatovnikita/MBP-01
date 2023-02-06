package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ads.initAds
import yin_kio.main_menu.presentation.MainMenuFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAds()
    }
}