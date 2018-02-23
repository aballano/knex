package com.aballano.knex.sample.ui

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aballano.knex.KnexBuilder
import com.aballano.knex.asContentType
import com.aballano.knex.sample.R
import com.aballano.knex.sample.model.Video
import com.aballano.knex.sample.model.VideoGenerator
import com.aballano.knex.sample.ui.knex.FooterKnexRenderer
import com.aballano.knex.sample.ui.knex.SectionKnexRenderer
import com.aballano.knex.sample.ui.knex.VideoKnexRenderer
import kotterknife.bindView

class AdvancedRecyclerViewActivity : Activity() {

    private val recyclerView: RecyclerView by bindView(R.id.rv_knex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val videoCollection = VideoGenerator.generateList(VIDEO_COUNT)

        val adapter = KnexBuilder.create()
            .bind(Video::class, ::VideoKnexRenderer)
            .bind(TYPE_FOOTER) { FooterKnexRenderer() }
            .bind(TYPE_SECTION, ::SectionKnexRenderer)
            /**
             * Type safety:
             *
             * This won't work since the Renderer is not KnexRenderer<KnexContent<...>>

            .bind(TYPE_FOOTER, ::VideoKnexRenderer)

             * And this won't work since the Renderer expects a Video class

            .bind(String::class, ::VideoKnexRenderer)

             **/
            .build()
            .into(recyclerView)

        videoCollection.forEachIndexed { index, video ->
            // Equivalent to: KnexContent("Video #" + (index + 1), TYPE_SECTION)
            adapter.add("Video #" + (index + 1) asContentType TYPE_SECTION)
            adapter.add(video)
        }
        adapter.add("by Alberto Ballano" asContentType TYPE_FOOTER)
    }

    companion object {
        // Let's add less videos so we can see the footer :)
        private const val VIDEO_COUNT = 10
        private const val TYPE_SECTION = 0
        private const val TYPE_FOOTER = 1
    }
}