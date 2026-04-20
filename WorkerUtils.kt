package com.example.myapplication.lab9

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

private const val TAG = "WorkerUtils"

/**
 * Hiển thị thông báo (Notification) khi Work bắt đầu chạy
 */
fun makeStatusNotification(message: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    try {
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    } catch (e: SecurityException) {
        Log.e(TAG, "Thiếu quyền hiển thị thông báo", e)
    }
}

/**
 * Hàm làm mờ ảnh (Sử dụng cách thu nhỏ và phóng to lại để tạo hiệu ứng mờ nhanh chóng)
 */
@WorkerThread
fun blurBitmap(bitmap: Bitmap, blurLevel: Int): Bitmap {
    val scaleFactor = 1f / (blurLevel * 3)
    val width = Math.round(bitmap.width * scaleFactor)
    val height = Math.round(bitmap.height * scaleFactor)

    val inputBitmap = Bitmap.createScaledBitmap(bitmap, width.coerceAtLeast(1), height.coerceAtLeast(1), false)
    return Bitmap.createScaledBitmap(inputBitmap, bitmap.width, bitmap.height, true)
}

/**
 * Lưu Bitmap vào một file tạm thời và trả về Uri
 */
@Throws(FileNotFoundException::class)
fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }
    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
            }
        }
    }
    return Uri.fromFile(outputFile)
}
fun Context.getImageUri(): Uri {
    val resources = this.resources
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(R.drawable.anh))
        .appendPath(resources.getResourceTypeName(R.drawable.anh))
        .appendPath(resources.getResourceEntryName(R.drawable.anh))
        .build()
}