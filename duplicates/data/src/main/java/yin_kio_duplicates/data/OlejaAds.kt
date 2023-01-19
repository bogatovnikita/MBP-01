package yin_kio_duplicates.data

import android.app.Activity
import com.example.ads.preloadAd
import yin_kio.duplicates.domain.gateways.Ads

class OlejaAds(
    private val activity: Activity
) : Ads {

    override fun preloadAd() {
        activity.preloadAd()
    }
}