package com.idyllic.core_api.usecase

interface SecureStorageManager {
    var token: String

    var refreshToken: String

    var isTestServer: Boolean

    var workingBaseUrl: String

    var expiresIn: Long?

    fun clearAll()
}
