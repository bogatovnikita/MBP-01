package yin_kio.garbage_clean.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import yin_kio.garbage_clean.presentation.GarbageCleanParentFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GarbageCleanParentFragment())
            .commit()

    }
}