package com.aballano.knex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.mock

open class ObjectKnexRenderer(val view: View = mock()) : KnexRenderer<Any>() {
    override fun inflate(inflater: LayoutInflater, parent: ViewGroup) = view
    override fun render(content: Any, position: Int, payloads: List<*>) {}
}