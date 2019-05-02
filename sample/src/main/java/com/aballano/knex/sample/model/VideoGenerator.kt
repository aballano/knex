package com.aballano.knex.sample.model

import java.util.HashMap
import java.util.Random

object VideoGenerator {

    private val videos: HashMap<String, String> = hashMapOf(
        "The Big Bang Theory" to "http://thetvdb.com/banners/_cache/posters/80379-9.jpg",
        "Breaking Bad" to "http://thetvdb.com/banners/_cache/posters/81189-22.jpg",
        "Arrow" to "http://thetvdb.com/banners/_cache/posters/257655-15.jpg",
        "Game of Thrones" to "http://thetvdb.com/banners/_cache/posters/121361-26.jpg",
        "Lost" to "http://thetvdb.com/banners/_cache/posters/73739-2.jpg",
        "How I met your mother" to "http://thetvdb.com/banners/_cache/posters/75760-29.jpg",
        "Dexter" to "http://thetvdb.com/banners/_cache/posters/79349-24.jpg",
        "Sleepy Hollow" to "http://thetvdb.com/banners/_cache/posters/269578-5.jpg",
        "The Vampire Diaries" to "http://thetvdb.com/banners/_cache/posters/95491-27.jpg",
        "Friends" to "http://thetvdb.com/banners/_cache/posters/79168-4.jpg",
        "New Girl" to "http://thetvdb.com/banners/_cache/posters/248682-9.jpg",
        "The Mentalist" to "http://thetvdb.com/banners/_cache/posters/82459-1.jpg",
        "Sons of Anarchy" to "http://thetvdb.com/banners/_cache/posters/82696-1.jpg"
    )

    private val random = Random()

    fun generateList(videoCount: Int): List<Video> {
        return (0..videoCount).map { generateRandomVideo() }
    }

    private fun generateRandomVideo() = Video().apply {
        configureFavoriteStatus(this)
        configureLikeStatus(this)
        configureLiveStatus(this)
        configureTitleAndThumbnail(this)
    }

    private fun configureLikeStatus(video: Video) {
        video.isLiked = random.nextBoolean()
    }

    private fun configureFavoriteStatus(video: Video) {
        video.isFavorite = random.nextBoolean()
    }

    private fun configureLiveStatus(video: Video) {
        video.isLive = random.nextBoolean()
    }

    private fun configureTitleAndThumbnail(video: Video) {
        val maxInt = videos.size
        val randomIndex = random.nextInt(maxInt)
        video.title = getKeyForIndex(randomIndex)
        video.thumbnail = getValueForIndex(randomIndex)
    }

    private fun getKeyForIndex(randomIndex: Int): String =
        videos.keys.elementAt(randomIndex)

    private fun getValueForIndex(randomIndex: Int): String =
        videos.values.elementAt(randomIndex)
}
