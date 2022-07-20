package org.experimentalplayers.faraday.utils

import android.app.Application
import timber.log.Timber

class MyApplication : Application() {

    companion object {
        var instance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()


        instance = this

        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

    }

}