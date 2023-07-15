package com.example.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.Instance.MySingleton
import com.example.newsapp.adapter.NewsData
import com.example.newsapp.adapter.NewsItemClicked
import com.example.newsapp.adapter.NewsListAdapater
import com.example.newsapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapater: NewsListAdapater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        newsData()
        mAdapater = NewsListAdapater(this)
    }

    private fun newsData() {
        val url =
            "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=7f5b00ff42ff467fa13c64179825b055"

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            null, {
                Log.i("tag", it.toString())
                val newsJsonArray = it.getJSONArray("articles")
                val listOfNews = ArrayList<NewsData>()
                for (i in 0 until newsJsonArray.length()) {
                    val news = NewsData(
                        newsJsonArray.getJSONObject(i).getString("title"),
                        newsJsonArray.getJSONObject(i).getString("author"),
                        newsJsonArray.getJSONObject(i).getString("url"),
                        newsJsonArray.getJSONObject(i).getString("urlToImage")
                    )
                    listOfNews.add(news)
                }
                mAdapater.updateNews(listOfNews)
                binding.rvNewsApp.adapter = mAdapater
            },
            {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozzila/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: NewsData) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}