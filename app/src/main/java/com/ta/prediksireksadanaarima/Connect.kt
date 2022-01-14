package com.ta.prediksireksadanaarima

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Connect {

    companion object {

        private fun getRetrofit(Url:String): Retrofit {
            return Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl(Url)
                .build()
        }

        private fun getApiData(): Retrofit {
            return getRetrofit(Url.BASE_URL)
        }

        fun callApi():CallApi{
            val retrofitCall = getApiData()
            return retrofitCall.create(CallApi::class.java)
        }

    }
}