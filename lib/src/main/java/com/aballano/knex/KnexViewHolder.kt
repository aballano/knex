package com.aballano.knex

import android.support.v7.widget.RecyclerView

open class KnexViewHolder(open val knexRenderer: KnexRenderer<Any>) : RecyclerView.ViewHolder(knexRenderer.rootView)