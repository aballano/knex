package com.aballano.knex

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Generic renderer to easily wrap Views for being used in the adapter.
 *
 * @param Content The type of the content used by the view.
 * @param View The View type.
 */
open class ViewKnexRenderer<in Content : Any, out View : android.view.View>(
    private val initFunction: (Context) -> View,
    private val renderFunction: (Content, View) -> Unit
) : KnexRenderer<Content>() {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
        initFunction(parent.context)

    @Suppress("UNCHECKED_CAST")
    override fun render(content: Content, position: Int, payloads: List<*>) =
        renderFunction(content, rootView as View)
}
