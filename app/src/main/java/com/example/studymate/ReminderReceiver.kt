package com.example.studymate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val namaTugas =
            intent.getStringExtra("nama_tugas")
                ?: "Tugas Kuliah"

        val deadline =
            intent.getStringExtra("deadline")
                ?: "-"

        val channelId =
            "studymate_deadline"

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val channel =
                NotificationChannel(
                    channelId,
                    "Pengingat Deadline",
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.description =
                "Notifikasi pengingat deadline StudyMate"

            notificationManager
                .createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Reminder StudyMate")
                .setContentText(
                    "$namaTugas mendekati deadline $deadline"
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}