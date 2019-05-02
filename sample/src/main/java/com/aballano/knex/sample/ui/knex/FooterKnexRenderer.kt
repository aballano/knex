package com.aballano.knex.sample.ui.knex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aballano.knex.sample.R

class FooterKnexRenderer : SectionKnexRenderer() {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.footer_view, parent, false)
}
