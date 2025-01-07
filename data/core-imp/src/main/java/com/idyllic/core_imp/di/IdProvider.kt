package com.idyllic.core_imp.di

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.WIFI_SERVICE
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.idyllic.core_imp.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.net.NetworkInterface
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class IdProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    var hash = ""

    fun getDeviceNameAndOSVersion2() = Build.MODEL + " | Android " + Build.VERSION.RELEASE

    fun getDeviceNameAndOSVersion(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.lowercase(Locale.getDefault())
                .startsWith(manufacturer.lowercase(Locale.getDefault()))
        ) {
            capitalize(model) + " | Android " + Build.VERSION.RELEASE
        } else {
            capitalize(manufacturer) + " " + model + " | Android " + Build.VERSION.RELEASE
        }
    }


    private fun capitalize(s: String?): String {
        if (s.isNullOrEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            first.uppercaseChar().toString() + s.substring(1)
        }
    }

    fun getDeviceMode(): String = when (BuildConfig.DEBUG) {
        true -> {
            "dev"
        }

        else -> {
            ""
        }
    }

    fun timestamp(): Long {
        return Date().time
    }

    fun getIMEI(context: Context): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            "That device version low than Android v26"
        }
    }

    fun getIMEI(): String {
//        val telephonyManager = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.READ_PHONE_STATE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return ""
//        }
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            try {
//                val imei1 = telephonyManager.getImei(0)
//                val imei2 = telephonyManager.getImei(1)
//                Timber.d("IMEI1: $imei1")
//                Timber.d("IMEI2: $imei2")
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            telephonyManager.imei
//        } else {
//            telephonyManager.deviceId
//        }
        return ""
    }

    fun getLocalIpAddress(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        return inetAddress.hostAddress
                    }
                }
            }

        } catch (ex: java.lang.Exception) {
            Timber.e("IP Address : $ex")
        }
        return null
    }

    fun getMacAddress(): String {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = nif.hardwareAddress ?: return ""
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }
                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                Timber.d("getMacAddress : $res1")
                return res1.toString()
            }
        } catch (ex: Exception) {
        }
        val manager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo
        val address = info.macAddress
        return address ?: ""
    }

    fun getDeviceName(): String {
        return Build.MODEL
    }

    fun deviceType(): String {
        return "android"
    }

    @SuppressLint("HardwareIds")
    fun getDeviceID(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getVersionName(): String {
        return getPackageInfo()?.versionName ?: ""
    }

    fun getAppVersion(): Long {
        val regex = Regex("\\d+")
        val matches = regex.findAll(getVersionName())

        // Concatenate matched numbers and convert to Long
        val concatenatedNumbers = matches.joinToString("") { it.value }
        return if (concatenatedNumbers.isNotEmpty()) concatenatedNumbers.toLong() else 0L
    }

    fun getVersionCode(): Int {
        return getPackageInfo()?.versionCode ?: 0
    }

    private fun getPackageInfo(): PackageInfo? {
        try {
            return context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun hasDeviceFingerprintSupport(): Boolean {
        val fingerprintManager =
            context.getSystemService(Context.FINGERPRINT_SERVICE) as? FingerprintManager
                ?: return false
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_FINGERPRINT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            false
        } else fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()
    }

//    fun tempA() {
//        val activeNetwork = context.intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO)
//        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
//        if (isConnected) {
//            when (activeNetwork!!.type) {
//                ConnectivityManager.TYPE_WIFI -> postValue(ConnectionModel(ConnectType.WIFI, true))
//                ConnectivityManager.TYPE_MOBILE -> postValue(
//                    ConnectionModel(
//                        ConnectType.MOBILE,
//                        true
//                    )
//                )
//            }
//        } else {
//            postValue(ConnectionModel(ConnectType.UNKNOWN, false))
//        }
//    }

    fun Context.isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    fun getNetwork(): String {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return "-"
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return "-"
        when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return "wifi"
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return "ethernet"
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return "mobile "
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> return "bluetooth"
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_USB) -> return "ethernet"
            else -> return "?"
        }
    }
}
//                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//                when (tm.dataNetworkType) {
//                    TelephonyManager.NETWORK_TYPE_GPRS,
//                    TelephonyManager.NETWORK_TYPE_EDGE,
//                    TelephonyManager.NETWORK_TYPE_CDMA,
//                    TelephonyManager.NETWORK_TYPE_1xRTT,
//                    TelephonyManager.NETWORK_TYPE_IDEN,
//                    TelephonyManager.NETWORK_TYPE_GSM -> return "2G"
//                    TelephonyManager.NETWORK_TYPE_UMTS,
//                    TelephonyManager.NETWORK_TYPE_EVDO_0,
//                    TelephonyManager.NETWORK_TYPE_EVDO_A,
//                    TelephonyManager.NETWORK_TYPE_HSDPA,
//                    TelephonyManager.NETWORK_TYPE_HSUPA,
//                    TelephonyManager.NETWORK_TYPE_HSPA,
//                    TelephonyManager.NETWORK_TYPE_EVDO_B,
//                    TelephonyManager.NETWORK_TYPE_EHRPD,
//                    TelephonyManager.NETWORK_TYPE_HSPAP,
//                    TelephonyManager.NETWORK_TYPE_TD_SCDMA -> return "3G"
//                    TelephonyManager.NETWORK_TYPE_LTE,
//                    TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> return "4G"
//                    TelephonyManager.NETWORK_TYPE_NR -> return "5G"
//                    else -> return "?"
//                }
