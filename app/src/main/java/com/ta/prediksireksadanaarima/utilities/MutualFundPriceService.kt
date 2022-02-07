package com.ta.prediksireksadanaarima.utilities

import com.ta.prediksireksadanaarima.models.MutualFundRawResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MutualFundPriceService {
    @Headers(
        "Accept: application/json, text/plain, */*",
        "Accept-Encoding: gzip, deflate, br",
        "Accept-Language: en-US,en;q=0.9",
        "Origin: https://app.bibit.id",
        "Referer: https://app.bibit.id/",
        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0",
    )
    @GET("/products/{rd_code}/chart?period=1M")
    fun getMutualFundPrice(@Path(value="rd_code", encoded = true) rdCode: String?): Call<MutualFundRawResponseModel>

    companion object{
        const val API_BASE_URL = "https://api.bibit.id"
    }
}