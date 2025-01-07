package com.idyllic.core_imp.remote

import com.idyllic.core_imp.model.BaseResponse
import com.idyllic.core_imp.model.BaseResponse2
import com.idyllic.core_imp.model.Line
import com.idyllic.core_imp.model.Product
import com.idyllic.core_imp.model.RefreshToken
import com.idyllic.core_imp.model.User
import com.idyllic.core_imp.model.LoginBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CoreService {

    /**
     * This is a dummy function. Change it according to the backend implementation.
     */
    @POST("v1/auth/refresh-token")
    suspend fun authRefreshToken(): retrofit2.Response<BaseResponse<RefreshToken>>

    @GET("products/{id}")
    fun getProducts(@Path("id") id: Int): Call<Product>

    @POST("user/login")
    fun login(
        @Body body: LoginBody
    ): Call<BaseResponse2<User>>

    @GET("car/get-active-line?include=userCar,lineAction.approvedOrder")
    fun getLines(
        @Query("page") page: Int?
    ): Call<BaseResponse2<List<Line>>>

}
