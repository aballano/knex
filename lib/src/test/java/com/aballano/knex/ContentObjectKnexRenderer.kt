package com.aballano.knex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.mock

open class ContentObjectKnexRenderer(val view: View = mock()) : KnexRenderer<KnexContent<Any>>() {
    override fun inflate(inflater: LayoutInflater, parent: ViewGroup) = view
    override fun render(content: KnexContent<Any>, position: Int, payloads: List<*>) {}
}