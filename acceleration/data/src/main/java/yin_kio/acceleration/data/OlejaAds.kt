package yin_kio.acceleration.data

import android.content.Context
import com.example.ads.preloadAd
import yin_kio.acceleration.domain.gateways.Ads

class OlejaAds(
    private val context: Context
) : Ads{

    override fun preloadAd() {
        context.preloadAd()
    }
}