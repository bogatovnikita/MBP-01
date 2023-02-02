package com.entertainment.event.ssearch.presentation.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.entertainment.event.ssearch.domain.dnd.TimePickerSettings
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.DayPickerLayoutBinding
import com.entertainment.event.ssearch.presentation.models.DayPickerBtn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

typealias OnDayClick = (chosenDays: List<Int>) -> Unit

@AndroidEntryPoint
class DayPicker @JvmOverloads constructor(
    context: Context,
    private val attr: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : ConstraintLayout(context, attr, defStyleAttr) {

    @Inject
    lateinit var settings: TimePickerSettings

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _binding: DayPickerLayoutBinding =
        DayPickerLayoutBinding.inflate(LayoutInflater.from(context), this)

    private var listBtn = hashMapOf<Int, DayPickerBtn>()

    private lateinit var onDayClick: OnDayClick

    init {
        scope.launch(Dispatchers.Main) {
            listBtn = getListBtn()
            initState()
            initListeners()
            updateChosenDays()
        }
    }

    fun setOnChosenDaysListener(block: OnDayClick) {
        onDayClick = block
    }

    private fun initState() {
        listBtn.forEach { pair ->
            renderBtn(pair.value.button, pair.value.isChosen)
        }
    }

    private fun initListeners() {
        listBtn.forEach { pair ->
            pair.value.button.setOnClickListener {
                renderBtn(pair.value.button, !pair.value.isChosen)
                updateData(pair.key, pair.value)
                onDayClick(listBtn.filter { it.value.isChosen }.map { it.key })
            }
        }
    }

    private fun renderBtn(tv: TextView, isSwitched: Boolean) {
        if (isSwitched) {
            tv.background = getBackground(R.drawable.bg_button_full_green)
            tv.setTextColor(getColor(R.color.white))
        } else {
            tv.background = getBackground(R.drawable.bg_day_normal)
            tv.setTextColor(getColor(R.color.gray))
        }
    }

    private fun updateData(day: Int, state: DayPickerBtn) {
        updateList(day, state)
        scope.launch {
            when (day) {
                MONDAY -> settings.setMondayInclude(!state.isChosen)
                TUESDAY -> settings.setTuesdayInclude(!state.isChosen)
                WEDNESDAY -> settings.setWednesdayInclude(!state.isChosen)
                THURSDAY -> settings.setThursdayInclude(!state.isChosen)
                FRIDAY -> settings.setFridayInclude(!state.isChosen)
                SATURDAY -> settings.setSaturdayInclude(!state.isChosen)
                SUNDAY -> settings.setSundayInclude(!state.isChosen)
                else -> {}
            }
        }
    }

    private fun updateList(day: Int,  state: DayPickerBtn) {
        listBtn[day] = state.copy(isChosen = !state.isChosen)
        updateChosenDays()
    }

    private fun updateChosenDays() {
        val chosenDays = listBtn.filterValues { state -> state.isChosen }
        if (chosenDays.isEmpty()) {
            _binding.tvDays.text = getStringById(R.string.notification_manager_only_today)
        } else {
            _binding.tvDays.text =
                _binding.root.context.getString(
                    R.string.notification_manager_every_week,
                    chosenDays.map { "${it.value.day}, " }.reduce { acc, day -> acc + day }
                        .removeSuffix(", "))
        }
    }

    private suspend fun getListBtn(): HashMap<Int, DayPickerBtn> {
        with(_binding) {
            return hashMapOf(
                MONDAY to DayPickerBtn(getStringById(R.string.notification_manager_mon), mon, settings.isMondayIncluded()),
                TUESDAY to DayPickerBtn(getStringById(R.string.notification_manager_tu), tu, settings.isTuesdayIncluded()),
                WEDNESDAY to DayPickerBtn(getStringById(R.string.notification_manager_we), we, settings.isWednesdayIncluded()),
                THURSDAY to DayPickerBtn(getStringById(R.string.notification_manager_th), th, settings.isThursdayIncluded()),
                FRIDAY to DayPickerBtn(getStringById(R.string.notification_manager_fr), fr, settings.isFridayIncluded()),
                SATURDAY to DayPickerBtn(getStringById(R.string.notification_manager_sat), sat, settings.isSaturdayIncluded()),
                SUNDAY to DayPickerBtn(getStringById(R.string.notification_manager_sun), sun, settings.isSundayIncluded()),
            )
        }
    }

    private fun getBackground(id: Int) = AppCompatResources.getDrawable(_binding.root.context, id)

    private fun getStringById(id: Int) = _binding.root.context.getString(id)

    private fun getColor(id: Int) = _binding.root.context.getColor(id)

    companion object {
        private const val MONDAY = 1
        private const val TUESDAY = 2
        private const val WEDNESDAY = 3
        private const val THURSDAY = 4
        private const val FRIDAY = 5
        private const val SATURDAY = 6
        private const val SUNDAY = 7
    }
}