package com.aballano.knex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

open class SubObjectKnexRenderer : KnexRenderer<Any>() {
    private lateinit var view: View

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup) = view
    override fun render(content: Any, position: Int, payloads: List<*>) {}
}
