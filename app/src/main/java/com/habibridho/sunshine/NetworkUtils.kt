package com.habibridho.sunshine

import android.content.res.Resources
import android.net.Uri
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class NetworkUtils() {
    companion object {
        val OPEN_WEATHER_SEARCH_URL = "https://api.openweathermap.org/data/2.5/weather"
        val OPEN_WEATHER_QUERY_PARAM = "q"
        val OPEN_WEATHER_APP_KEY_PARAM = "APPID"
    }

    @Throws(MalformedURLException::class)
    fun buildSearchUrl(query: String): URL {
        val uri = Uri.parse(OPEN_WEATHER_SEARCH_URL).buildUpon()
                .appendQueryParameter(OPEN_WEATHER_QUERY_PARAM, query)
                .appendQueryParameter(OPEN_WEATHER_APP_KEY_PARAM, BuildConfig.OPEN_WEATHER_API_KEY)
                .build()

        return URL(uri.toString())
    }

    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        try {
            val input = urlConnection.inputStream

            val scanner = Scanner(input)
            scanner.useDelimiter("\\A")

            val hasInput = scanner.hasNext()

            if (hasInput) {
                return scanner.next()
            }

            return null
        } finally {
            urlConnection.disconnect()
        }
    }
}