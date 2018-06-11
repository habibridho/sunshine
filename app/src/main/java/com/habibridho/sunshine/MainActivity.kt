package com.habibridho.sunshine

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
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
        if (item?.itemId == R.id.search_button) {
            makeGithubSearchQuery()
        }

        return super.onOptionsItemSelected(item)
    }

    fun makeGithubSearchQuery() {
        val query = searchEditText.text.toString()
        val url = netUtils.buildGithubSearchUrl(query)
        urlTextView.text = url.toString()

        GithubQueryTask().execute(url)
    }

    inner class GithubQueryTask: AsyncTask<URL, Void, String?>() {
        override fun doInBackground(vararg url: URL): String? {
            val searchUrl: URL = url[0]
            var githubSearchResults: String? = ""

            try {
                githubSearchResults = netUtils.getResponseFromHttpUrl(searchUrl)
            } catch (e: IOException) {
                toast("Could not make an http call")
                e.printStackTrace()
            }

            return githubSearchResults
        }

        override fun onPostExecute(result: String?) {
            result?.let {
                if (!it.equals("")) {
                    resultTextView.text = it
                }
            }
        }
    }
}
