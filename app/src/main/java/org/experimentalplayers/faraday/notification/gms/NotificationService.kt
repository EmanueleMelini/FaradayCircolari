package org.experimentalplayers.faraday.notification.gms

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import it.emanuelemelini.kotlinadroidutils.checkService
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.ui.HomeActivity


class NotificationService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCM Service"
        private const val FCM_CHANNEL = "org.experimentalplayers.faraday.FirebaseNotificationService"
        private const val NOTIFICATION_ID = 2
    }

    private var notificationManager: NotificationManager? = null

    override fun onNewToken(newToken: String) {
        //Ignored
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(checkService(this)) {
            sendNotification(remoteMessage)
        }
    }

    private fun createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(notificationManager?.getNotificationChannel(FCM_CHANNEL) == null) {
                val notificationChannel = NotificationChannel(FCM_CHANNEL, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH)
                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

        val rNotification = remoteMessage.notification ?: return

        notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel()

        val flags = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        else
            PendingIntent.FLAG_UPDATE_CURRENT

        val intent = if(!rNotification.clickAction.isNullOrBlank())
            Intent(rNotification.clickAction!!)
        else
            Intent(this, HomeActivity::class.java)

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            flags
        )

        val icon = if(rNotification.icon != null)
            IconCompat.createWithContentUri(rNotification.icon!!)
        else
            IconCompat.createWithResource(this, R.drawable.ic_launcher_foreground)

        val color = if(rNotification.color != null)
            Color.parseColor(rNotification.color!!)
        else
            ResourcesCompat.getColor(resources, R.color.colorPrimary, null)

        val notification = NotificationCompat.Builder(this, FCM_CHANNEL)
            .setSmallIcon(icon)
            .setContentTitle(rNotification.title)
            .setContentText(rNotification.body)
            .setColor(color)
            .setContentIntent(contentIntent)
            .build()

        val manager = NotificationManagerCompat.from(applicationContext)
        manager.notify(NOTIFICATION_ID, notification)

        Log.d(TAG, "From: " + remoteMessage.from)
        Log.d(TAG, "Notification Message Body: " + rNotification.body)

    }

}