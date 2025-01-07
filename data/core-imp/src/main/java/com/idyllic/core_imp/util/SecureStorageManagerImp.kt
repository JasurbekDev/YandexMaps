package com.idyllic.core_imp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.idyllic.core_api.usecase.SecureStorageManager
import com.idyllic.core_imp.BuildConfig
import javax.inject.Inject

class SecureStorageManagerImp @Inject constructor(context: Context) : SecureStorageManager {
    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
        private const val IS_TEST_SERVER = "IS_TEST_SERVER"
        private const val WORKING_BASE_URL = "WORKING_BASE_URL"
        private const val EXPIRES_IN = "EXPIRES_IN"
    }

    private val masterKey: MasterKey =
        MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    private val encryptedPrefs: SharedPreferences = try {
        EncryptedSharedPreferences.create(
            context,
            StorageConfig.SECURE_SHARED_PREFERENCES_FILE_NAME,
            masterKey, // masterKey created above
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        context.getSharedPreferences(
            StorageConfig.SECURE_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE
        )
    }

    private val sharedPreferences = context.getSharedPreferences(
        StorageConfig.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE
    )
    private val sharedPreferencesLanguage = context.getSharedPreferences(
        StorageConfig.SHARED_PREFERENCES_FILE_NAME_LANGUAGE, Context.MODE_PRIVATE
    )


    override var token: String by StringPreferenceDelegate(encryptedPrefs, TOKEN_KEY, "null")

    override var refreshToken: String by StringPreferenceDelegate(encryptedPrefs, REFRESH_TOKEN_KEY, "null")

    override var isTestServer: Boolean
        get() = sharedPreferences.getBoolean(IS_TEST_SERVER, false)
        set(value) {
            sharedPreferences.edit().putBoolean(IS_TEST_SERVER, value).apply()
        }

    override var workingBaseUrl: String by StringPreferenceDelegate(
        encryptedPrefs,
        WORKING_BASE_URL,
        BuildConfig.BASE_URL_PROD_API
    )

    override var expiresIn: Long?
        get() = sharedPreferences.getLong(EXPIRES_IN, 10_000)
        set(value) {
            val currentTimeMillis: Long = System.currentTimeMillis()
            sharedPreferences.edit()
                .putLong(EXPIRES_IN, (value ?: 1) * 1_000 + currentTimeMillis)
                .apply()
        }

    override fun clearAll() {
        try {
//            sharedPreferences.edit().clear().apply()
            token = ""
            refreshToken = ""
//            encryptedPrefs.edit().clear().apply()
        } catch (_: Exception) {
//            e.printStackTrace()
        }
    }
}
