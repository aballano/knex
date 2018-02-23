package com.aballano.knex.sample.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.aballano.knex.sample.R
import kotterknife.bindView

class MainActivity : Activity() {

    private val openSimpleButton: Button by bindView(R.id.bt_open_rv_simple)
    private val openAdvancedButton by bindView<Button>(R.id.bt_open_rv_advanced)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openSimpleButton.setOnClickListener { open<SimpleRecyclerViewActivity>() }
        openAdvancedButton.setOnClickListener { open<AdvancedRecyclerViewActivity>() }
    }

    private inline fun <reified T> open() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }
}
