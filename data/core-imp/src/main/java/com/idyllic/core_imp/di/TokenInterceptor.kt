package com.idyllic.core_imp.di

import com.idyllic.core_api.usecase.SecureStorageManager
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val secureStorageManager: SecureStorageManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()

        if (secureStorageManager.refreshToken.isBlank()) {
            val errorResponseBody = "Unauthorized: Token is missing"
            return Response.Builder()
                .code(444)
                .message("Error: Token is missing")
                .request(original)
                .body(errorResponseBody.toResponseBody("text/plain".toMediaTypeOrNull()))
                .protocol(Protocol.HTTP_1_1)
                .build()
        }

        if (secureStorageManager.token.isBlank().not()) {
            requestBuilder.header("Authorization", "Bearer ${secureStorageManager.token}")
        }
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}
