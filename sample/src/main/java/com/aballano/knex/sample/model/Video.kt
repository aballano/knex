package com.aballano.knex.sample.model

data class Video(
    var isFavorite: Boolean = false,
    var isLiked: Boolean = false,
    var isLive: Boolean = false,
    var thumbnail: String? = null,
    var title: String? = null
)
