package com.aballano.knex

import androidx.recyclerview.widget.RecyclerView

open class KnexViewHolder(open val knexRenderer: KnexRenderer<Any>) :
    RecyclerView.ViewHolder(knexRenderer.rootView)
