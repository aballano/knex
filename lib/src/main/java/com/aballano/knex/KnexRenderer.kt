package com.aballano.knex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class KnexRenderer<in T : Any> {

    lateinit var rootView: View
        private set

    lateinit var context: Context
        private set

    fun onCreate(layoutInflater: LayoutInflater, parent: ViewGroup) {
        context = parent.context
        rootView = inflate(layoutInflater, parent)
        setUpView()
    }

    abstract fun inflate(inflater: LayoutInflater, parent: ViewGroup): View

    open fun setUpView() {}

    abstract fun render(content: T, position: Int, payloads: List<*>)
}