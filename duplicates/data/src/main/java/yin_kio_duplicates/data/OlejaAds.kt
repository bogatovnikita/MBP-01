package yin_kio_duplicates.data

import android.content.Context
import com.example.ads.preloadAd
import yin_kio.duplicates.domain.gateways.Ads

class OlejaAds(
    private val context: Context
) : Ads {

    override fun preloadAd() {
        context.preloadAd()
    }
}