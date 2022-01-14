package com.ta.prediksireksadanaarima

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CallApi {

    @GET(Url.URL)
    //query needed if there is any query
    fun getApi(@Query("limit") limit: Int):
    //model class is needed
    Observable<ResponseModel.Result>
}