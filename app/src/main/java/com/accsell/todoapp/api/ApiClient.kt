package com.accsell.todoapp.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ApiClient(private val token: String) {
    private val client = OkHttpClient.Builder()
        .hostnameVerifier { hostname, session ->
            hostname == "beta.mrdekk.ru" || hostname == "hive.mrdekk.ru"
        }
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://beta.mrdekk.ru/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}