package com.aballano.knex.sample.ui.knex

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
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

    private val titleAnimator by lazy {
        ObjectAnimator.ofFloat(title, "translationX", 100f).apply {
            duration = 1000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            setAutoCancel(true)
            logWithData("animation ${hashCode()} created")
        }
    }

    override fun onAttached() {
        super.onAttached()
        title.tag = titleAnimator.apply {
            logWithData("animation ${hashCode()} started in onAttached")
            start()
        }
    }

    override fun onDetached() {
        (title.tag as ObjectAnimator).apply {
            logWithData("animation ${hashCode()} stopped")
            cancel()
        }
        title.tag = null
        super.onDetached()
    }

    private fun logWithData(msg: String) {
        Log.d("DEBUG", "${hashCode()} view ${title.hashCode()} $msg")
    }
}
