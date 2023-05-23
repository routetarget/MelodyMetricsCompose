package com.example.melodymetricscompose.scrapers

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import kotlin.math.nextUp

class AOYRatingFetcher {
        companion object {
            private const val SEARCH_URL = "https://www.albumoftheyear.org/search"
            private const val RATING_SELECTOR = ".albumUserScore > a:nth-child(1)"

            suspend fun fetchRating(artistName: String, albumName: String): scrapedInfo = withContext(
                Dispatchers.IO){
                val searchUrl = "$SEARCH_URL/?q=${artistName + " " + albumName.replace(" ", "+")}"
                val searchResults = Jsoup.connect(searchUrl).get().select(".infobox tr")

                Log.d("Scraper","searchterm is $albumName")
                Log.d("Scraper","searchURL is $searchUrl")
                Log.d("Scraper","result is $searchResults")



                val albumUrl = "https://www.albumoftheyear.org" + searchResults.firstOrNull()?.select("a.searchpage")?.attr("href")
                Log.d("Search"," album URL is $albumUrl")
                if (albumUrl.isNullOrEmpty()) {
                    //return@withContext null
                    return@withContext scrapedInfo(url = null, rating = null)
                }

                delay((StrictMath.random().nextUp().toInt() + 1)/1000L)

                val albumDoc = Jsoup.connect(albumUrl).get()
                val ratingText = albumDoc.select(RATING_SELECTOR).firstOrNull()?.text()
                //return@withContext ratingText?.toDoubleOrNull()
                return@withContext scrapedInfo(albumUrl, ratingText?.toDoubleOrNull())

            }
        }


    }

    //data class scrapedInfoAOY(val url: String?, val rating: Double?)
