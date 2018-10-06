package com.aballano.knex.sample

import android.app.Application
import com.bumptech.glide.request.target.ViewTarget

class KnexSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ViewTarget.setTagId(R.id.glide_resource_id)
    }
}
