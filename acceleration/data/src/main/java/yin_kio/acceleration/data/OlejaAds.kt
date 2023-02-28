package yin_kio.acceleration.data

import android.app.Application
import com.example.ads.preloadAd
import yin_kio.acceleration.domain.gateways.Ads

class OlejaAds(
    private val context: Application
) : Ads{

    override fun preloadAd() {
        context.preloadAd()
    }
}