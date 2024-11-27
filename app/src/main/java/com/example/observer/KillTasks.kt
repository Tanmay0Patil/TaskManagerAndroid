package com.example.observer

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

// utility class for killing tasks or apps
class KillTasks (private val context: Context){

    // Function to check if an app can be killed (avoids killing system apps or the current app)
    private fun canKillApp(packageName: String): Boolean {
        // Prevent killing system apps or your own app
        return packageName != context.packageName && !isSystemApp(packageName)
    }

    // Function to kill the app with the given package name
    // Though limitations are there for this
    fun killApp(packageName: String) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Check if app can be killed
        val canKill = canKillApp(packageName)

        if (canKill) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Issues with killing Code fails here too . It can't kill
                    activityManager.killBackgroundProcesses(packageName)
                    Toast.makeText(context, "App $packageName killed", Toast.LENGTH_SHORT).show()
                } else {
                    // For older Android versions
                    val runningProcesses = activityManager.runningAppProcesses
                    runningProcesses?.find { it.processName == packageName }?.let { process ->
                        android.os.Process.killProcess(process.pid)
                        Toast.makeText(context, "App $packageName killed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to kill app: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Show dialog box if cant kill
            AlertDialog.Builder(context)
                .setTitle("Cannot Kill App")
                .setMessage("This app cannot be killed because it is a system app or has special permissions or you don't have permission to do so.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    // Check if an app is a system app
    private fun isSystemApp(packageName: String): Boolean {
        return try {
            val packageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}