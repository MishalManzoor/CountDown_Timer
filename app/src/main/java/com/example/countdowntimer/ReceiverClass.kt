package com.example.countdowntimer

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReceiverClass : BroadcastReceiver() {
    @SuppressLint("UnspecifiedImmutableFlag", "ServiceCast")
    override fun onReceive(p0: Context, p1: Intent) {

        val intent = Intent(p0, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(p0, 0, intent, 0)

        val view = Intent(p0, MainActivity::class.java)
        val pendingIntent1 = PendingIntent.getActivity(p0, 0, view, 0)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(p0, "CONST_CHANNEL")
                .setContentTitle("Alarm")
                .setContentText("Times Up!!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(0, "view", pendingIntent1)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setColor(Color.BLUE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManagerCompat =
            NotificationManagerCompat.from(p0)
        notificationManagerCompat.notify(1, builder.build())
    }
}


/*
class BroadCast : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent?) {

       val builder : NotificationCompat.Builder =
           NotificationCompat.Builder(p0, "ch")
               .setSmallIcon(R.drawable.ic_launcher_background)
               .setContentTitle("Alarm turn ON")
               .setContentText("Alarm is ringing")
               .setAutoCancel(true)
               .setDefaults(NotificationCompat.DEFAULT_ALL)
               .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManagerCompat =
            NotificationManagerCompat.from(p0)
        notificationManagerCompat.notify(123,builder.build())
    }
}
 */