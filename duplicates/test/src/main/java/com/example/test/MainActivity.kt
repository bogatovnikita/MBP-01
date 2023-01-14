package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import yin_kio.duplicates.presentation.ParentFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ParentFragment())
            .commit()
    }
}