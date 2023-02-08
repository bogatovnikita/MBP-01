package yin_kio.duplicates.presentation

import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import com.example.ads.showInter
import yin_kio.duplicates.domain.models.Destination

class Navigation(
    private val childNavController: NavController,
    private val parentNavController: NavController,
    private val activity: Activity,
    private val onCloseInter: () -> Unit,
    private val advicesDestination: Int,
    private val advicesUnitedTitle: String,
    private val advicesNoFilesTitle: String
) {

    private var currentDestination = Destination.List

    fun navigate(destination: Destination){
        if (currentDestination == destination) return

        handelDestination(destination)

        currentDestination = destination
    }

    private fun handelDestination(destination: Destination){
        when(destination){
            Destination.Permission -> childNavController.navigate(R.id.toPermission)
            Destination.List -> childNavController.navigateUp()
            Destination.UniteProgress -> childNavController.navigate(R.id.toUniteProgress)
            Destination.Inter ->  activity.showInter(onClosed = onCloseInter)
            Destination.AskContinue -> goToAskContinue()
            Destination.Advices -> parentNavController.navigate(advicesDestination)
            Destination.AdvicesUnited -> parentNavController.navigate(advicesDestination, title(advicesUnitedTitle))
            Destination.AdvicesNoFiles -> parentNavController.navigate(advicesDestination, title(advicesNoFilesTitle))
        }
    }

    private fun goToAskContinue() {
        childNavController.navigateUp()
        childNavController.navigate(R.id.toAskContinue)
    }

    private fun title(title: String) : Bundle{
        return Bundle().apply {
            putString(ADVICES_DIALOG_TITLE, title)
        }
    }

    companion object{
        private const val ADVICES_DIALOG_TITLE =  "dialog_title"
    }

}