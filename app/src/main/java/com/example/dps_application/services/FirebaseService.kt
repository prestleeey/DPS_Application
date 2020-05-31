package com.example.dps_application.services

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dps_application.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        Log.d("1337", "onNewToken: "+p0);
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d("1337", "onMessageReceived");
        var builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle("Павел Дуров")
            .setContentText("Надо сделать")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }

        super.onMessageReceived(p0)
    }

}