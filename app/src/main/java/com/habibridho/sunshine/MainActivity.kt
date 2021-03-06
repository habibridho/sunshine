package com.habibridho.sunshine

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var netUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        netUtils = NetworkUtils()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.main, it)
            return true
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.search_button -> makeSearchQuery()
            R.id.refresh_button -> clearSearch()
        }

        return super.onOptionsItemSelected(item)
    }

    fun clearSearch() {
        urlTextView.text = ""
        resultTextView.text = ""
    }

    fun makeSearchQuery() {
        val query = searchEditText.text.toString()
        val url = netUtils.buildSearchUrl(query)
        urlTextView.text = url.toString()

        QueryTask().execute(url)
    }

    fun showJSONDataView() {
        errorMessageText.visibility = View.INVISIBLE
        resultTextView.visibility = View.VISIBLE
    }

    fun showErrorMessage() {
        errorMessageText.visibility = View.VISIBLE
        resultTextView.visibility = View.INVISIBLE
    }

    inner class QueryTask: AsyncTask<URL, Void, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            loadingIndicator.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg url: URL): String? {
            val searchUrl: URL = url[0]
            var githubSearchResults: String? = ""

            try {
                githubSearchResults = netUtils.getResponseFromHttpUrl(searchUrl)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return githubSearchResults
        }

        override fun onPostExecute(result: String?) {
            loadingIndicator.visibility = View.INVISIBLE
            if (result == null || result.equals("")) {
                showErrorMessage()
            } else {
                resultTextView.text = result
                showJSONDataView()
            }
        }
    }
}
