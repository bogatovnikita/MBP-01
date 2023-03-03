package com.entertainment.event.ssearch.data.device_functionality_info

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.ImageFormat.JPEG
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.util.Size
import com.entertainment.event.ssearch.data.util.Util
import com.entertainment.event.ssearch.domain.device_info.FunctionalityInfo
import com.entertainment.event.ssearch.domain.models.ChildFun
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import com.entertainment.event.ssearch.domain.models.ParentFun
import general.R
import javax.inject.Inject

class FunctionalityInfoImpl @Inject constructor(
    private val context: Application
) : Util(context), FunctionalityInfo {

    ///---------------------------WIFI--------------------------------
    private val isWifiAvailable: String
        get() {
            val hasWIFI = hasSystemFeature(PackageManager.FEATURE_WIFI)
            return isSupported(hasWIFI)
        }

    private val isWifiAwareAvailable: String
        get() {
            val hasWifiAware = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE)
            } else {
                false
            }
            return isSupported(hasWifiAware)
        }

    private val isWifiDirectAvailable: String
        get() {
            val hasDirectWIFI = hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)
            return isSupported(hasDirectWIFI)
        }

    private val is5GAvailable: String
        get() {
            val mn = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val has5G = mn.is5GHzBandSupported
            return isSupported(has5G)
        }

    ///---------------------------BLE--------------------------------
    private val hasBluetooth: String
        get() {
            val hasBLE = hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
            return isSupported(hasBLE)
        }

    private val hasBluetoothLe: String
        get() {
            val hasBleLe = hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
            return isSupported(hasBleLe)
        }

    ///---------------------------GPS--------------------------------
    private val hasGPS: String
        get() {
            val hasBLE = hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
            return isSupported(hasBLE)
        }

    ///---------------------------NFC--------------------------------
    private val isNfcAvailable: String
        get() {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
            return isSupported(nfcAdapter != null)
        }

    ///---------------------------Camera--------------------------------

    val FULL_HD = Pair(1920, 1080)
    val K_4 = Pair(4096, 2160)
    val K_8 = Pair(7680, 4320)

    private val isCameraAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            return isSupported(available)
        }

    private val isCameraAutofocusAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)
            return isSupported(available)
        }

    private val isExternalCameraAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_CAMERA_EXTERNAL)
            return isSupported(available)
        }

    private val isFlashCameraAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            return isSupported(available)
        }

    private val isFullHdAvailable: String
        get() {
            val available = isSupportedQuality(FULL_HD)
            return isSupported(available)
        }

    private val is4KAvailable: String
        get() {
            val available = isSupportedQuality(K_4)
            return isSupported(available)
        }

    private val is8KAvailable: String
        get() {
            val available = isSupportedQuality(K_8)
            return isSupported(available)
        }

    private fun isSupportedQuality(format: Pair<Int, Int>): Boolean {
        val mCameraManager =
            context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val myCameras = mCameraManager.cameraIdList

        for (cameraID in myCameras) {

            val cc: CameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraID)
            val configurationMap: StreamConfigurationMap? =
                cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val sizesJPEG: Array<Size> = configurationMap!!.getOutputSizes(JPEG)

            for (item in sizesJPEG) {
                val width = item.width
                val height = item.height
                if (format == Pair(width, height))
                    return true
            }

        }
        return false
    }

    ///---------------------------USB_HOST--------------------------------
    private val isUsbHostAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_USB_HOST)
            return isSupported(available)
        }

    ///---------------------------MULTITOUCH--------------------------------
    private val isMultitouchAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)
            return isSupported(available)
        }

    ///---------------------------MICROPHONE--------------------------------
    private val isMicrophoneAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
            return isSupported(available)
        }

    ///------------------------------IR----------------------------------
    private val isInfraredPortAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_CONSUMER_IR)
            return isSupported(available)
        }

    ///------------------------------SIM----------------------------------
    private val isTwoSimAvailable: String
        get() {
            val manager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return isSupported(manager.phoneCount > 1)
        }

    private val isESimAvailable: String
        get() {
            val available = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                (context.getSystemService(Context.EUICC_SERVICE) as EuiccManager).isEnabled
            } else {
                false
            }
            return isSupported(available)
        }

    ///----------------------------SDCard--------------------------------
    val hasExternalSDCard: String
        get() {
            val available = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            return isSupported(available)
        }

    ///----------------------------FINGER SCANNER--------------------------------
    val isFingerScannerAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
            return isSupported(available)
        }

    ///--------------------------------WALLPAPER----------------------------------

    private val isLiveWallpaperAvailable: String
        get() {
            val available = hasSystemFeature(PackageManager.FEATURE_LIVE_WALLPAPER)
            return isSupported(available)
        }

    ///-----------------------------------VR--------------------------------------

    private val isVRAvailable: String
        get() {
            val available = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hasSystemFeature(PackageManager.FEATURE_VR_HEADTRACKING)
            } else {
                false
            }
            return isSupported(available)
        }

    override fun getFunctionalityInfo(): DeviceFunctionGroup = DeviceFunctionGroup(
        parentFun = ParentFun(name = getString(R.string.functions), id = 18),
        listFun = listOf(
            ChildFun(name = getString(R.string.wireless_wifi), body = isWifiAvailable, id = 19),
            ChildFun(name = getString(R.string.wifi_aware), body = isWifiAwareAvailable, id = 20),
            ChildFun(name = getString(R.string.bluetooth), body = hasBluetooth, id = 21),
            ChildFun(name = getString(R.string.bluetooth_le), body = hasBluetoothLe, id = 22),
            ChildFun(name = getString(R.string.gps), body = hasGPS, id = 23),
            ChildFun(name = getString(R.string.nfc), body = isNfcAvailable, id = 24),
            ChildFun(name = getString(R.string.camera), body = isCameraAvailable, id = 25),
            ChildFun(name = getString(R.string.autofocus_camera), body = isCameraAutofocusAvailable, id = 26),
            ChildFun(name = getString(R.string.external_camera), body = isExternalCameraAvailable, id = 27),
            ChildFun(name = getString(R.string.flash_camera), body = isFlashCameraAvailable, id = 28),
            ChildFun(name = getString(R.string.usb_host), body = isUsbHostAvailable, id = 29),
            ChildFun(name = getString(R.string.multitouch), body = isMultitouchAvailable, id = 30),
            ChildFun(name = getString(R.string.infrared_port), body = isInfraredPortAvailable, id = 31),
            ChildFun(name = getString(R.string.two_sim), body = isTwoSimAvailable, id = 32),
            ChildFun(name = getString(R.string.e_sim), body = isESimAvailable, id = 33),
            ChildFun(name = getString(R.string.sd_card), body = hasExternalSDCard, id = 34),
            ChildFun(name = getString(R.string.finger_scanner), body = isFingerScannerAvailable, id = 35),
            ChildFun(name = getString(R.string.full_hd), body = isFullHdAvailable, id = 36),
            ChildFun(name = getString(R.string.four_k), body = is4KAvailable, id = 37),
            ChildFun(name = getString(R.string.eight_k), body = is8KAvailable, id = 38),
            ChildFun(name = getString(R.string.wifi_direct), body = isWifiDirectAvailable, id = 39),
            ChildFun(name = getString(R.string.live_wallpaper), body = isLiveWallpaperAvailable, id = 40),
            ChildFun(name = getString(R.string.vr), body = isVRAvailable, id = 41),
            ChildFun(name = getString(R.string.five_g), body = is5GAvailable, id = 42),
        )
    )

    private fun hasSystemFeature(feature: String) = context.packageManager.hasSystemFeature(feature)

    private fun isSupported(isSupported: Boolean) =
        if (isSupported) getString(R.string.supported) else getString(R.string.not_supported)

}