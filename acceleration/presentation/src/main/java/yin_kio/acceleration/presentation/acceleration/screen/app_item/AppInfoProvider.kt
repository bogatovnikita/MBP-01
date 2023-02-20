package yin_kio.acceleration.presentation.acceleration.screen.app_item

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build

class AppInfoProvider(
    private val context: Context,
) {

    fun getIcon(packageName: String) : Drawable?{
        return try {
            getPackageInfo(packageName).applicationInfo.loadIcon(context.packageManager)
        } catch (ex: Exception){
            null
        }
    }

    fun getName(packageName: String) : String{
        return try {
            getPackageInfo(packageName).applicationInfo.loadLabel(context.packageManager).toString()
        } catch (ex: Exception){
            ""
        }
    }


    private fun getPackageInfo(packageName: String) : PackageInfo{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION") context.packageManager.getPackageInfo(packageName, 0)
        }
    }


}