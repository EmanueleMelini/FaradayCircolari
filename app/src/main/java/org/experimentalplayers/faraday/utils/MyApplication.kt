package org.experimentalplayers.faraday.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import it.emanuelemelini.kotlinadroidutils.checkService
import org.experimentalplayers.faraday.BuildConfig
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MyApplication : Application() {

    companion object {
        var instance: MyApplication? = null
        var sharedPreferences: SharedPreferences? = null
    }

    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

    override fun onCreate() {
        super.onCreate()

        instance = this
        sharedPreferences = getSharedPreferences(SHARED_FARADAY, Context.MODE_PRIVATE)

        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        val auth = Firebase.auth
        val user = auth.currentUser

        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Timber.d("LOGGED-${user?.uid}-${task.result.user?.uid}")
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