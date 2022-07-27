package org.experimentalplayers.faraday.utils

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import it.emanuelemelini.kotlinadroidutils.checkService
import org.experimentalplayers.faraday.BuildConfig
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

        val auth = Firebase.auth
        val user = auth.currentUser

        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Timber.d("LOGGED-$user-${task.result.user}")
                } else {
                    Timber.d("NOT LOGGED-$user")
                    task.exception?.printStackTrace()
                }
            }
            .addOnFailureListener {
                Timber.d("FAILED-$user")
                it.printStackTrace()
            }

        if(checkService(instance!!)) {
            FirebaseMessaging.getInstance().subscribeToTopic(FIREBASE_TOPIC)
        }

    }

}