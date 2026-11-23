package com.example.studymate.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseApiClient {

    private const val BASE_URL =
        "https://uzzvcnnvycjihojizbxf.supabase.co/"

    const val API_KEY =
        "sb_publishable_16fcwLLF6i2ryR8CwzN_SQ_zCH1s_QQ"

    val apiService: SupabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApiService::class.java)
    }
}