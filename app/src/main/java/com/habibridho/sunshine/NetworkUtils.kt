package com.habibridho.sunshine

import android.net.Uri
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class NetworkUtils() {
    companion object {
        val GITHUB_SEARCH_URL = "https://api.github.com/search/repositories"
        val GITHUB_QUERY_PARAM = "q"
        val GITHUB_SORT_PARAM = "sort"
    }

    @Throws(MalformedURLException::class)
    fun buildGithubSearchUrl(query: String): URL {
        val uri = Uri.parse(GITHUB_SEARCH_URL).buildUpon()
                .appendQueryParameter(GITHUB_QUERY_PARAM, query)
                .appendQueryParameter(GITHUB_SORT_PARAM, "stars")
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