package yin_kio.garbage_clean.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import yin_kio.garbage_clean.presentation.databinding.FragmentGarbageCleanBinding

class GarbageCleanFragment : Fragment(R.layout.fragment_garbage_clean) {

    private val binding: FragmentGarbageCleanBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}