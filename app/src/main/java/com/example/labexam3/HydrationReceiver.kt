package com.example.labexam3

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class HydrationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val channelId = "hydration_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // check whether Android 8+ or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Hydration Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminds you to drink water regularly."
            }
            notificationManager.createNotificationChannel(channel)
        }

        val openIntent = Intent(context, MainActivity::class.java)
        val openPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_water)
            .setContentTitle("💧 Time to Hydrate!")
            .setContentText("Drink some water to stay fresh and focused!")
            .setContentIntent(openPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)

        // Re-schedule the reminder
        val prefs = context.getSharedPreferences("hydration", Context.MODE_PRIVATE)
        val hours = prefs.getInt("interval_hours", 1)
        if (hours <= 0) return

        val intervalMillis = hours * 60 * 60 * 1000L

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val newIntent = Intent(context, HydrationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            newIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        //check whether Android 12+ or not
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                if (alarmManager?.canScheduleExactAlarms() == true) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + intervalMillis,
                        pendingIntent
                    )
                } else {
                    val warnText = "⚠️ Exact alarm permission not granted — open app settings to enable it."
                    android.widget.Toast.makeText(context, warnText, android.widget.Toast.LENGTH_LONG).show()
                }
            } else {
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + intervalMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            android.widget.Toast.makeText(
                context,
                "⚠️ Unable to schedule exact alarm (permission denied).",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }
}
