package com.chakra.shoppinglist.data

import android.net.Uri
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.*

class SearchImageService(private val client: OkHttpClient) {

    private val URL = "https://pixabay.com/api/?key=8916913-905d7b5a1d35466fd2341bc9c&per_page=99&q="

    fun searchImages(query: String?): List<String> {
        return try {
            if (query == null) return listOf()
            val request = Request.Builder()
                    .url(URL + Uri.encode(query))
                    .build()
            val response: Response = client.newCall(request).execute()
            val element = JsonParser().parse(response.body()!!.string())
            val list = element.asJsonObject.getAsJsonArray("hits")
            val result: MutableList<String> = ArrayList()
            for (i in 0 until list.size()) {
                val `object` = list[i].asJsonObject
                result.add(`object`["webformatURL"].asString)
            }
            result
        } catch (e: Exception) {
            listOf()
        }
    }
}