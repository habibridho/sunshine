package com.habibridho.sunshine

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.IOException

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

        try {
            val githubSearchResults = netUtils.getResponseFromHttpUrl(url)
            resultTextView.text = githubSearchResults
        } catch (e: IOException) {
            toast("Could not make http call")
        }
    }
}
