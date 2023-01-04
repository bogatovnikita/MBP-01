package yin_kio.file_manager.data

import android.app.Activity
import com.example.ads.preloadAd
import yin_kio.file_manager.domain.gateways.Ads

class OlejaAds(
    private val activity: Activity
) : Ads {

    override fun preload() {

        activity.preloadAd()
    }
}