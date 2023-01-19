package com.example.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ads.initAds
import yin_kio.duplicates.presentation.fragments.ParentFragment
import yin_kio_duplicates.di.DuplicatesFragmentFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = DuplicatesFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAds()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, supportFragmentManager.fragmentFactory.instantiate(classLoader, ParentFragment::class.java.name))
            .commit()
    }
}