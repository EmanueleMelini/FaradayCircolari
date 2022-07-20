package org.experimentalplayers.faraday.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

open class FetchService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)

        Timber.d("TEST")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onTaskRemoved(rootIntent: Intent) {

        startService(
            Intent(
                applicationContext,
                this.javaClass
            ).setPackage(packageName)
        )

        super.onTaskRemoved(rootIntent)
    }

}