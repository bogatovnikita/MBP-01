package com.example.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ads.initAds
import yin_kio.file_manager.di.FileManagerFragmentFactory
import yin_kio.file_manager.presentation.FileManagerFragment
import yin_kio.file_manager.presentation.FileManagerParentFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = FileManagerFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAds()

        val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, FileManagerParentFragment::class.java.name)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}