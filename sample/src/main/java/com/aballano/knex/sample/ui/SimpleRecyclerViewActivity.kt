package com.aballano.knex.sample.ui

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aballano.knex.KnexBuilder
import com.aballano.knex.sample.R
import com.aballano.knex.sample.model.VideoGenerator
import com.aballano.knex.sample.ui.knex.VideoKnexRenderer
import kotterknife.bindView

class SimpleRecyclerViewActivity : Activity() {

    private val recyclerView: RecyclerView by bindView(R.id.rv_knex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val videoCollection = VideoGenerator.generateList(VIDEO_COUNT)
        val adapter = KnexBuilder.create(::VideoKnexRenderer)
              .build()
              .into(recyclerView)

        videoCollection.forEach { adapter.add(it) }
    }

    companion object {
        private const val VIDEO_COUNT = 100
    }
}