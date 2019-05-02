package com.aballano.knex.sample.ui.knex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aballano.knex.KnexContent
import com.aballano.knex.KnexRenderer
import com.aballano.knex.sample.R
import kotterknife.bindView

open class SectionKnexRenderer : KnexRenderer<KnexContent<String>>() {

    private val title: TextView by bindView(R.id.tv_title)

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.section_view, parent, false)

    override fun render(content: KnexContent<String>, position: Int, payloads: List<*>) {
        title.text = content.item
    }
}
