package com.entertainment.event.ssearch.data.device_functionality_info

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyManager
import com.entertainment.event.ssearch.domain.device_info.FunctionalityInfo
import com.entertainment.event.ssearch.domain.models.ChildFun
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import com.entertainment.event.ssearch.domain.models.ParentFun
import general.R
import javax.inject.Inject

class FunctionalityInfoImpl @Inject constructor(
    private val context: Application
) : FunctionalityInfo {

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
    private val isCameraAvailable: String
        get() {
            val available =
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            return isSupported(available)
        }

    private val isCameraAutofocusAvailable: String
        get() {
            val available =
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)
            return isSupported(available)
        }

    private val isExternalCameraAvailable: String
        get() {
            val available =
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_EXTERNAL)
            return isSupported(available)
        }

    private val isFlashCameraAvailable: String
        get() {
            val available =
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            return isSupported(available)
        }

    //Fingerprint
//    fun areFingerprintsEnrolled(): Boolean {
//        val fingerprintManager =
//            context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
//        return fingerprintManager.hasEnrolledFingerprints()
//    }
//
//    fun isFingerprintSensorPresent(): Boolean {
//        val fingerprintManager =
//            context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
//        return fingerprintManager.isHardwareDetected()
//    }

    //SIM
    fun isMultiSim(): Boolean {
        val manager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.getPhoneCount() > 1
    }

    //ExternalSDCard
    fun hasExternalSDCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    override fun getFunctionalityInfo(): DeviceFunctionGroup = DeviceFunctionGroup(
        parentFun = ParentFun(name = R.string.functions, id = 18),
        listFun = listOf(
            ChildFun(name = R.string.wireless_wifi, body = isWifiAvailable, id = 19),
            ChildFun(name = R.string.wifi_aware, body = isWifiAwareAvailable, id = 20),
            ChildFun(name = R.string.bluetooth, body = hasBluetooth, id = 21),
            ChildFun(name = R.string.bluetooth_le, body = hasBluetoothLe, id = 22),
            ChildFun(name = R.string.gps, body = hasGPS, id = 23),
            ChildFun(name = R.string.nfc, body = isNfcAvailable, id = 24),
            ChildFun(name = R.string.camera, body = isCameraAvailable, id = 25),
            ChildFun(name = R.string.autofocus_camera, body = isCameraAutofocusAvailable, id = 26),
            ChildFun(name = R.string.external_camera, body = isExternalCameraAvailable, id = 27),
            ChildFun(name = R.string.flash_camera, body = isFlashCameraAvailable, id = 28),
        )
    )

    private fun hasSystemFeature(feature: String) = context.packageManager.hasSystemFeature(feature)

    private fun isSupported(isSupported: Boolean) =
        if (isSupported) getString(R.string.supported) else getString(R.string.not_supported)

    private fun getString(id: Int) = context.getString(id)

}