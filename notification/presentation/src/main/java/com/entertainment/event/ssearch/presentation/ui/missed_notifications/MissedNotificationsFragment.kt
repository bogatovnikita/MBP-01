package com.entertainment.event.ssearch.presentation.ui.missed_notifications

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentMissedNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MissedNotificationsFragment : Fragment(R.layout.fragment_missed_notifications) {

    val binding: FragmentMissedNotificationsBinding by viewBinding()

}