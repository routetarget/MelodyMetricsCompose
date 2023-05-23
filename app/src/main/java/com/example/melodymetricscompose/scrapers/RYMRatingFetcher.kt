package com.example.melodymetricscompose.scrapers

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.lang.StrictMath.random
import kotlin.math.nextUp

class RYMRatingFetcher {
    companion object {
        private const val RYM_SEARCH_URL = "https://rateyourmusic.com/search"
        private const val RYM_RATING_SELECTOR = ".avg_rating"

        suspend fun fetchRating(artistName: String, albumName: String): scrapedInfo = withContext(
            Dispatchers.IO){
            val searchUrl = "$RYM_SEARCH_URL?searchterm=${artistName + " " + albumName.replace(" ", "+")}"
            val searchResults = Jsoup.connect(searchUrl).get().select(".infobox tr")

            //Log.d("Scraper","searchterm is $albumName")
            //Log.d("Scraper","searchURL is $searchUrl")
            //Log.d("Scraper","result is $searchResults")



            val albumUrl = "https://rateyourmusic.com" + searchResults.firstOrNull()?.select("a.searchpage")?.attr("href")
            Log.d("Search"," album URL is $albumUrl")
            if (albumUrl.isNullOrEmpty()) {
                //return@withContext null
                return@withContext scrapedInfo(url = null, rating = null)
            }

            delay((random().nextUp().toInt() + 1)/1000L)

            val albumDoc = Jsoup.connect(albumUrl).get()
            val ratingText = albumDoc.select(RYM_RATING_SELECTOR).firstOrNull()?.text()
            return@withContext scrapedInfo(albumUrl, ratingText?.toDoubleOrNull())

        }
    }


}

data class scrapedInfo(val url: String?, val rating: Double?)
