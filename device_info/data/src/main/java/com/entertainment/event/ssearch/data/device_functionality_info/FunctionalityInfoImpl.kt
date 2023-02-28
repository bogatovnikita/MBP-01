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
import android.nfc.NfcAdapter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.util.Size
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
            val available = getCameraInformation(FULL_HD)
            return isSupported(available)
        }

    private val is4KAvailable: String
        get() {
            val available = getCameraInformation(K_4)
            return isSupported(available)
        }

    private val is8KAvailable: String
        get() {
            val available = getCameraInformation(K_8)
            return isSupported(available)
        }

    private fun getCameraInformation(format: Pair<Int, Int>): Boolean {
        val mCameraManager =
            context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        // Получение списка камер с устройства
        val myCameras = mCameraManager.cameraIdList
        // выводим информацию по камере
        for (cameraID in myCameras) {
            // Получение характеристик камеры
            val cc: CameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraID)
            // Получение списка выходного формата, который поддерживает камера
            val configurationMap: StreamConfigurationMap? =
                cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            // Получение списка разрешений которые поддерживаются для формата jpeg
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

    ///----------------------------WIRELESS CHARGE--------------------------------


    private val batteryStatusIntent: Intent?
        get() {
            val batFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            return context.registerReceiver(null, batFilter)
        }

    val isWirelessChargeAvailable: String
        get() {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val present =
                batteryStatusIntent?.extras?.getString(BatteryManager.ACTION_CHARGING) ?: "none"
            return isSupported(true)
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
            ChildFun(name = R.string.usb_host, body = isUsbHostAvailable, id = 29),
            ChildFun(name = R.string.multitouch, body = isMultitouchAvailable, id = 30),
            ChildFun(name = R.string.infrared_port, body = isInfraredPortAvailable, id = 31),
            ChildFun(name = R.string.two_sim, body = isTwoSimAvailable, id = 32),
            ChildFun(name = R.string.e_sim, body = isESimAvailable, id = 33),
            ChildFun(name = R.string.sd_card, body = hasExternalSDCard, id = 34),
            ChildFun(name = R.string.finger_scanner, body = isFingerScannerAvailable, id = 35),
            ChildFun(name = R.string.wireless_charge, body = isWirelessChargeAvailable, id = 36),
            ChildFun(name = R.string.full_hd, body = isFullHdAvailable, id = 37),
            ChildFun(name = R.string.four_k, body = is4KAvailable, id = 38),
            ChildFun(name = R.string.eight_k, body = is8KAvailable, id = 39),
        )
    )

    private fun hasSystemFeature(feature: String) = context.packageManager.hasSystemFeature(feature)

    private fun isSupported(isSupported: Boolean) =
        if (isSupported) getString(R.string.supported) else getString(R.string.not_supported)

    private fun getString(id: Int) = context.getString(id)

}