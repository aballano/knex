package com.aballano.knex.sample.ui.knex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aballano.knex.KnexRenderer
import com.aballano.knex.sample.R
import com.aballano.knex.sample.model.Video
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotterknife.bindView

class VideoKnexRenderer : KnexRenderer<Video>() {

    private val thumbnail: ImageView by bindView(R.id.iv_thumbnail)
    private val title: TextView by bindView(R.id.tv_title)
    private lateinit var imageLoader: RequestManager

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.video_view, parent, false)

    override fun setUpView() {
        imageLoader = Glide.with(context)
        thumbnail.setOnClickListener {
            val video = it.tag as Video
            Toast.makeText(context, "Video clicked. Title = ${video.title}", Toast.LENGTH_LONG).show()
        }
    }

    override fun render(content: Video, position: Int, payloads: List<*>) {
        thumbnail.tag = content
        imageLoader.clear(thumbnail)
        imageLoader.load(content.thumbnail)
            .into(thumbnail)
        title.text = content.title
    }
}
