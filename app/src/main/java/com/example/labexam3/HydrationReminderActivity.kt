package com.example.labexam3

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class HydrationReminderActivity : AppCompatActivity() {

    private lateinit var spinnerInterval: Spinner
    private lateinit var btnSaveInterval: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hydration_reminder)

        spinnerInterval = findViewById(R.id.spinnerInterval)
        btnSaveInterval = findViewById(R.id.btnSaveInterval)

        //Back button
        findViewById<TextView>(R.id.backText).setOnClickListener {
            finish()
        }

        val pref = getSharedPreferences("hydration", Context.MODE_PRIVATE)
        val savedHours = pref.getInt("interval_hours", 0)

        val spinnerIndex = when (savedHours) {
            0 -> 0
            1 -> 1
            2 -> 2
            3 -> 3
            4 -> 4
            5 -> 5
            6 -> 6
            8 -> 7
            else -> 0
        }

        spinnerInterval.setSelection(spinnerIndex)


        // Ask notification permission if Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }

        btnSaveInterval.setOnClickListener {
            val selectedPos = spinnerInterval.selectedItemPosition
            val pref = getSharedPreferences("hydration", Context.MODE_PRIVATE)

            when (selectedPos) {
                0 -> {
                    cancelHydrationReminder()
                    pref.edit().putInt("interval_hours", 0).apply()
                    showCustomToast("Reminders turned off", "success")
                }

                1 -> setInterval(pref, 1)
                2 -> setInterval(pref, 2)
                3 -> setInterval(pref, 3)
                4 -> setInterval(pref, 4)
                5 -> setInterval(pref, 5)
                6 -> setInterval(pref, 6)
                7 -> setInterval(pref, 8)
            }

            finish()
        }
    }

    private fun setInterval(pref: android.content.SharedPreferences, hours: Int) {
        saveInterval(hours)
        setHydrationReminder(hours)
        showCustomToast("Reminder set every $hours hour(s)", "success")
    }

    private fun saveInterval(hours: Int) {
        val pref = getSharedPreferences("hydration", Context.MODE_PRIVATE)
        pref.edit().putInt("interval_hours", hours).apply()
    }

    private fun cancelHydrationReminder() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(this, HydrationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager?.cancel(pendingIntent)
    }

    private fun setHydrationReminder(hours: Int) {
        if (hours <= 0) return

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(this, HydrationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intervalMillis = hours * 60 * 60 * 1000L
        val triggerTime = System.currentTimeMillis() + intervalMillis

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager?.canScheduleExactAlarms() == true) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    intent.data = android.net.Uri.parse("package:$packageName")
                    startActivity(intent)
                    showCustomToast("Enable 'Exact alarms' permission and try again", "error")
                }
            } else {
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            showCustomToast("Exact alarm scheduling not allowed", "error")
        }
    }

    private fun showCustomToast(message: String, type: String) {
        val layout = layoutInflater.inflate(R.layout.custom_toast, null)
        val toastText = layout.findViewById<TextView>(R.id.toastText)
        val toastIcon = layout.findViewById<ImageView>(R.id.toastIcon)

        toastText.text = message
        val iconRes = if (type == "success") R.drawable.ic_success else R.drawable.ic_error
        toastIcon.setImageResource(iconRes)

        with(Toast(applicationContext)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}
