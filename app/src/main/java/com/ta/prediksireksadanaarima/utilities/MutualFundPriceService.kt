package com.ta.prediksireksadanaarima.utilities

import com.ta.prediksireksadanaarima.models.MutualFundPriceResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/* Retrofit service that maps the different endpoints on the API, you'd create one
 * method per endpoint, and use the @Path, @Query and other annotations to customize
 * these at runtime */

interface MutualFundPriceService {
    @GET("/api?")
    fun getMutualFundPrice(@Query("rd") rdCode: String?): Call<MutualFundPriceResponseModel>

    companion object{
        const val API_BASE_URL = "https://arima-reksadana-api.vercel.app"
    }
}