package com.idyllic.core_imp.util

import android.util.Log
import com.google.gson.Gson
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_imp.model.BaseResponse
import com.idyllic.core_imp.model.BaseResponse2
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import kotlin.coroutines.resume

suspend fun <E, T> Response<BaseResponse<E>>.await2(mapper: (dto: E) -> T): ResourceUI<T> =
    suspendCancellableCoroutine {
        try {
            val body = body()
            val data = body?.data
            val success = body?.success
            val error = body?.error

            Timber.d("ERROR 1 body WWW: ${body}")
            Timber.d("ERROR 1 WWW: ${error?.message}")
            Timber.d("ERROR 1 success WWW: ${success}")
            if (success == false) {
                Timber.d("ERROR 2 WWW: ${error?.message}")
                it.resume(ResourceUI.Error(Exception(error?.message), error?.code ?: ""))
                return@suspendCancellableCoroutine
            }
            Timber.d("ERROR 3 WWW: ${error?.message}")
            Timber.d("ERROR 4 WWW: ${code()}")
            when (code()) {
                in 200..299 -> {
                    if (data == null) {
                        if (error?.message != null) {
                            it.resume(ResourceUI.Error(Exception(error.message), error.code ?: ""))
                            return@suspendCancellableCoroutine
                        }
                        it.resume(ResourceUI.Error(NullPointerException()))
                        return@suspendCancellableCoroutine
                    }
                    it.resume(ResourceUI.Success(mapper.invoke(data), code()))
                }

                500 -> {
                    it.resume(ResourceUI.Error(InternalServerError("")))
                }

                401 -> {
                    it.resume(ResourceUI.Error(TokenWrongException("")))
                }

                else -> {
                    val message: String = handleError(errorBody())
                    Timber.d("ERROR 5 WWW: $message")
                    it.resume(ResourceUI.Error(NetworkException(message)))
                }
            }
        } catch (e: Throwable) {
            Timber.d("ERROR 6 body WWW: $e")
            it.resume(
                when (e) {
                    is NetworkException -> ResourceUI.Error(Exception("Data is Empty"))
                    is NullPointerException -> ResourceUI.Error(Exception("Data is Empty"))
                    is UnknownHostException -> ResourceUI.Error(ServerException("Not connection with server"))
                    is SSLHandshakeException -> ResourceUI.Error(ServerException("Not connection with server"))
                    is SSLException -> ResourceUI.Error(ServerException("Not connection with server"))
                    is SocketException -> ResourceUI.Error(SocketException("Not connection with server"))
                    else -> ResourceUI.Error(e)
                }
            )
        }
    }

suspend fun <E, T> Response<BaseResponse<List<E>>>.awaitList2(mapper: (dto: E) -> T): ResourceUI<List<T>> =
    suspendCancellableCoroutine {
        try {
            val body: BaseResponse<List<E>>? = body()
            val data = body?.data
            val success = body?.success
            val error = body?.error

            Timber.d("ERROR List1 body WWW: ${body}")
            Timber.d("ERROR List1 WWW: ${error?.message}")
            Timber.d("ERROR List1 success WWW: ${success}")
            if (success == false) {
                Timber.d("ERROR List2 WWW: ${error?.message}")
                it.resume(ResourceUI.Error(Exception(error?.message), error?.code ?: ""))
                return@suspendCancellableCoroutine
            }
            Timber.d("ERROR List3 WWW: ${error?.message}")
            Timber.d("ERROR List4 WWW: ${code()}")
            when (code()) {
                in 200..299 -> {
                    if (data == null) {
                        it.resume(ResourceUI.Error(NullPointerException()))
                        return@suspendCancellableCoroutine
                    }
                    it.resume(ResourceUI.Success(data.map { e -> mapper.invoke(e) }, code()))
                }

                500 -> {
                    it.resume(ResourceUI.Error(InternalServerError("")))
                }

                401 -> {
                    it.resume(ResourceUI.Error(TokenWrongException("")))
                }

                else -> {
                    val message: String = handleError(errorBody())
                    Timber.d("ERROR List5 WWW: ${message}")
                    it.resume(ResourceUI.Error(NetworkException(message)))
                }
            }
        } catch (e: Throwable) {
            Timber.d("ERROR List6 WWW: ${e}")
            it.resume(
                when (e) {
                    is NetworkException -> ResourceUI.Error(Exception("Data is Empty"))
                    is NullPointerException -> ResourceUI.Error(Exception("Data is Empty"))
                    is UnknownHostException -> ResourceUI.Error(ServerException("Not connection with server"))
                    is SSLHandshakeException -> ResourceUI.Error(ServerException("Not connection with server"))
                    is SSLException -> ResourceUI.Error(ServerException("Not connection with server"))
                    is SocketException -> ResourceUI.Error(SocketException("Not connection with server"))
                    else -> ResourceUI.Error(e)
                }
            )
        }
    }


suspend fun <E, T> Call<BaseResponse<List<E>>>.awaitList(mapper: (dto: E) -> T): ResourceUI<List<T>> =
    suspendCancellableCoroutine {
        enqueue(object : Callback<BaseResponse<List<E>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<E>>>,
                response: Response<BaseResponse<List<E>>>
            ) {
                val body: BaseResponse<List<E>>? = response.body()
                val data = body?.data
                val success = body?.success
                val error = body?.error

                Timber.d("ERROR List1 body WWW: ${body}")
                Timber.d("ERROR List1 WWW: ${error?.message}")
                Timber.d("ERROR List1 success WWW: ${success}")
                if (success == false) {
                    Timber.d("ERROR List2 WWW: ${error?.message}")
                    it.resume(ResourceUI.Error(Exception(error?.message), error?.code ?: ""))
                    return
                }
                Timber.d("ERROR List3 WWW: ${error?.message}")
                Timber.d("ERROR List4 WWW: ${response.code()}")
                when (response.code()) {
                    in 200..299 -> {
                        if (data == null) {
                            it.resume(ResourceUI.Error(NullPointerException()))
                            return
                        }
                        it.resume(
                            ResourceUI.Success(
                                data.map { e -> mapper.invoke(e) },
                                response.code()
                            )
                        )
                    }

                    500 -> {
                        it.resume(ResourceUI.Error(InternalServerError("")))
                    }

                    401 -> {
                        it.resume(ResourceUI.Error(TokenWrongException("")))
                    }

                    else -> {
                        val message: String = handleError(response.errorBody())
                        Timber.d("ERROR List5 WWW: ${message}")
                        it.resume(ResourceUI.Error(NetworkException(message)))
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<E>>>, t: Throwable) {
                it.resume(
                    when (t) {
                        is NetworkException -> ResourceUI.Error(Exception("Data is Empty"))
                        is NullPointerException -> ResourceUI.Error(Exception("Data is Empty"))
                        is UnknownHostException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SSLHandshakeException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SSLException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SocketException -> ResourceUI.Error(SocketException("Not connection with server"))
                        else -> ResourceUI.Error(t)
                    }
                )
            }

        })
    }

suspend fun <E, T> Call<BaseResponse<E>>.await(mapper: (dto: E) -> T): ResourceUI<T> =
    suspendCancellableCoroutine {
        enqueue(object : Callback<BaseResponse<E>> {
            override fun onResponse(
                call: Call<BaseResponse<E>>,
                response: Response<BaseResponse<E>>
            ) {
                val body = response.body()
                val data = body?.data
                val success = body?.success
                val error = body?.error

                Timber.d("ERROR body WWW: ${body}")
                Timber.d("ERROR WWW: ${error?.message}")
                Timber.d("ERROR success WWW: ${success}")

                if (success == false) {
                    Timber.d("ERROR WWW: ${error?.message}")
                    it.resume(
                        ResourceUI.Error(
                            ErrorToServerError(error?.message ?: ""), error?.code ?: ""
                        )
                    )
                    return
                }

                when (response.code()) {
                    in 200..299 -> {
                        if (data == null) {
                            if (error?.message != null) {
                                it.resume(
                                    ResourceUI.Error(
                                        ErrorToServerError(error.message ?: ""), error.code ?: ""
                                    )
                                )
                                return
                            }
                            it.resume(ResourceUI.Error(NullPointerException()))
                            return
                        }
                        it.resume(ResourceUI.Success(mapper.invoke(data), response.code()))
                    }

                    500 -> {
                        it.resume(ResourceUI.Error(InternalServerError("")))
                    }

                    401 -> {
                        it.resume(ResourceUI.Error(TokenWrongException("")))
                    }

                    403 -> {
                        val message: String = handleError2(response.errorBody())
                        it.resume(ResourceUI.Error(DeviceNotActivated(message)))
                    }

                    429 -> {
                        // Kopr api ga so'rov berish uchun 429 kodini qaytaradi
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(DdosServerError(message)))
                    }

                    503 -> {
                        // Texnicheski Pererive
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(TechnicalBreakServerError(message)))
                    }

                    426 -> {
                        // Majburi update
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(AppUpdateException(message)))
                    }

                    418 -> {
                        // Bloklangan
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(BlockUserException(message)))
                    }

                    444 -> {
                        // Navigate to login screen
                        val message = "Error"
                        it.resume(ResourceUI.Error(RefreshTokenExpired(message)))
                    }

                    else -> {
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(NetworkException(message)))
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<E>>, t: Throwable) {
                if (t is java.lang.IllegalStateException) {
                    // to catch Illegal State Exception
                }
                Timber.d("ERRORONE WWW: ${t.localizedMessage}")
                Timber.d("ERRORONE WWW: ${t}")
                it.resume(
                    when (t) {
                        is NumberFormatException -> ResourceUI.Error(NumberFormatException("Data is Empty"))
                        is InterruptedIOException -> ResourceUI.Error(TimeoutError("Timeout"))
                        is NetworkException -> ResourceUI.Error(Exception("Data is Empty"))
                        is NullPointerException -> ResourceUI.Error(Exception("Data is Empty"))
                        is UnknownHostException -> ResourceUI.Error(UnknownHostException("Not connection with server"))
                        is SSLHandshakeException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SSLException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SocketException -> ResourceUI.Error(SocketException("Not connection with server"))
                        else -> ResourceUI.Error(t)
                    }
                )
            }
        })
    }

suspend fun <E, T> Call<BaseResponse2<E>>.awaitGeneral(mapper: (dto: E) -> T): ResourceUI<T> =
    suspendCancellableCoroutine {
        enqueue(object : Callback<BaseResponse2<E>> {
            override fun onResponse(
                call: Call<BaseResponse2<E>>,
                response: Response<BaseResponse2<E>>
            ) {
                val body = response.body()
                val data = body?.data
                val success = response.isSuccessful
//                val error = body?.error

                Timber.d("Body WWW: ${body}")
                Timber.d("ERROR body WWW: ${response.errorBody()?.string()}")
//                Timber.d("ERROR WWW: ${error?.message}")
                Timber.d("ERROR success WWW: ${success}")

                if (success == false) {
//                    Timber.d("ERROR WWW: ${error?.message}")
                    it.resume(
                        ResourceUI.Error(
                            ErrorToServerError(""), response.code().toString() ?: ""
                        )
                    )
                    return
                }

                when (response.code()) {
                    in 200..299 -> {
                        if (data == null) {
                            it.resume(
                                ResourceUI.Error(
                                    ErrorToServerError(response.errorBody().toString() ?: ""), response.code().toString() ?: ""
                                )
                            )
//                            it.resume(ResourceUI.Error(NullPointerException()))
                            return
                        }
                        it.resume(ResourceUI.Success(mapper.invoke(data), response.code()))
                    }

                    500 -> {
                        it.resume(ResourceUI.Error(InternalServerError("")))
                    }

                    401 -> {
                        it.resume(ResourceUI.Error(TokenWrongException("")))
                    }

                    403 -> {
                        val message: String = handleError2(response.errorBody())
                        it.resume(ResourceUI.Error(DeviceNotActivated(message)))
                    }

                    429 -> {
                        // Kopr api ga so'rov berish uchun 429 kodini qaytaradi
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(DdosServerError(message)))
                    }

                    503 -> {
                        // Texnicheski Pererive
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(TechnicalBreakServerError(message)))
                    }

                    426 -> {
                        // Majburi update
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(AppUpdateException(message)))
                    }

                    418 -> {
                        // Bloklangan
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(BlockUserException(message)))
                    }

                    444 -> {
                        // Navigate to login screen
                        val message = "Error"
                        it.resume(ResourceUI.Error(RefreshTokenExpired(message)))
                    }

                    else -> {
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(NetworkException(message)))
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse2<E>>, t: Throwable) {
                if (t is java.lang.IllegalStateException) {
                    // to catch Illegal State Exception
                }
                Timber.d("ERRORONE WWW: ${t.localizedMessage}")
                Timber.d("ERRORONE WWW: ${t}")
                it.resume(
                    when (t) {
                        is NumberFormatException -> ResourceUI.Error(NumberFormatException("Data is Empty"))
                        is InterruptedIOException -> ResourceUI.Error(TimeoutError("Timeout"))
                        is NetworkException -> ResourceUI.Error(Exception("Data is Empty"))
                        is NullPointerException -> ResourceUI.Error(Exception("Data is Empty"))
                        is UnknownHostException -> ResourceUI.Error(UnknownHostException("Not connection with server"))
                        is SSLHandshakeException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SSLException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SocketException -> ResourceUI.Error(SocketException("Not connection with server"))
                        else -> ResourceUI.Error(t)
                    }
                )
            }
        })
    }

suspend fun <E, T> Call<BaseResponse2<List<E>>>.awaitGeneralForList(mapper: (dto: BaseResponse2<List<E>>) -> T): ResourceUI<T> =
    suspendCancellableCoroutine {
        enqueue(object : Callback<BaseResponse2<List<E>>> {
            override fun onResponse(
                call: Call<BaseResponse2<List<E>>>,
                response: Response<BaseResponse2<List<E>>>
            ) {
                val body = response.body()
                val success = response.isSuccessful
//                val error = body?.error

                Timber.d("ERROR body WWW: ${body}")
//                Timber.d("ERROR WWW: ${error?.message}")
                Timber.d("ERROR success WWW: ${success}")

                if (success == false) {
//                    Timber.d("ERROR WWW: ${error?.message}")
                    it.resume(
                        ResourceUI.Error(
                            ErrorToServerError(""), response.code().toString() ?: ""
                        )
                    )
                    return
                }

                when (response.code()) {
                    in 200..299 -> {
                        if (body == null) {
                            it.resume(
                                ResourceUI.Error(
                                    ErrorToServerError(response.errorBody().toString() ?: ""), response.code().toString() ?: ""
                                )
                            )
//                            it.resume(ResourceUI.Error(NullPointerException()))
                            return
                        }
                        it.resume(ResourceUI.Success(mapper.invoke(body), response.code()))
                    }

                    500 -> {
                        it.resume(ResourceUI.Error(InternalServerError("")))
                    }

                    401 -> {
                        it.resume(ResourceUI.Error(TokenWrongException("")))
                    }

                    403 -> {
                        val message: String = handleError2(response.errorBody())
                        it.resume(ResourceUI.Error(DeviceNotActivated(message)))
                    }

                    429 -> {
                        // Kopr api ga so'rov berish uchun 429 kodini qaytaradi
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(DdosServerError(message)))
                    }

                    503 -> {
                        // Texnicheski Pererive
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(TechnicalBreakServerError(message)))
                    }

                    426 -> {
                        // Majburi update
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(AppUpdateException(message)))
                    }

                    418 -> {
                        // Bloklangan
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(BlockUserException(message)))
                    }

                    444 -> {
                        // Navigate to login screen
                        val message = "Error"
                        it.resume(ResourceUI.Error(RefreshTokenExpired(message)))
                    }

                    else -> {
                        val message: String = handleError(response.errorBody())
                        it.resume(ResourceUI.Error(NetworkException(message)))
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse2<List<E>>>, t: Throwable) {
                if (t is java.lang.IllegalStateException) {
                    // to catch Illegal State Exception
                }
                Timber.d("ERRORONE WWW: ${t.localizedMessage}")
                Timber.d("ERRORONE WWW: ${t}")
                it.resume(
                    when (t) {
                        is NumberFormatException -> ResourceUI.Error(NumberFormatException("Data is Empty"))
                        is InterruptedIOException -> ResourceUI.Error(TimeoutError("Timeout"))
                        is NetworkException -> ResourceUI.Error(Exception("Data is Empty"))
                        is NullPointerException -> ResourceUI.Error(Exception("Data is Empty"))
                        is UnknownHostException -> ResourceUI.Error(UnknownHostException("Not connection with server"))
                        is SSLHandshakeException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SSLException -> ResourceUI.Error(ServerException("Not connection with server"))
                        is SocketException -> ResourceUI.Error(SocketException("Not connection with server"))
                        else -> ResourceUI.Error(t)
                    }
                )
            }
        })
    }

private fun handleError(body: ResponseBody?): String {
    val tempError = """{ "errorMessage" = "Some Error from network" }"""
    val byteArray: ByteArray = body?.bytes() ?: tempError.toByteArray()
    return try {
        JSONObject(String(byteArray)).getString("errorMessage")
    } catch (e: JSONException) {
        e.message.toString()
    }
}

private fun handleError2(body: ResponseBody?): String {
    val tempError = """{ "message" = "Some Error from network" }"""
    val byteArray: ByteArray = body?.bytes() ?: tempError.toByteArray()
    return try {
        val gson = Gson()
        val errorResponse = gson.fromJson(String(byteArray), BaseResponse::class.java)
        errorResponse.error?.message.toString()
    } catch (e: JSONException) {
        e.message.toString()
    }
}
