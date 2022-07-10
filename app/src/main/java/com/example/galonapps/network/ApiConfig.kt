package com.example.galonapps.network

import com.example.galonapps.config.Constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getRetroInstance(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
            client
//                .addInterceptor { chain ->
//                val request = chain.request()
//                val httpRequest = request.newBuilder()
//                    .addHeader(
//                        HEADER_TOKEN_FIELD,
//                        "Bearer ${prefs.token ?: ""}"
//                    )
//                    .build()
//                chain.proceed(httpRequest)
//            }
                .addInterceptor(logging)

            return Retrofit.Builder()
                .baseUrl("${BASE_URL}api/")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
