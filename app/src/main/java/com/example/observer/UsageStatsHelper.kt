package com.example.observer

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Helper class to manage and display app usage statistics
class UsageStatsHelper(private val context:Context) {

    // Function to get the list of apps used in the last 24 hours
    fun getLast24HoursApps(): List<UsageStats> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000 * 60 * 60 * 24 // Last 24 hours

        // Query usage stats and filter for apps with non-zero foreground time, excluding this app
        // Arranged into sorted order of foreground time
        return usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        ).filter {
            it.totalTimeInForeground > 0 &&
                    it.packageName != context.packageName
        }.sortedByDescending { it.totalTimeInForeground }
    }

    // Function to show a dialog with detailed usage stats for a specific app
    fun showAppDetailsDialog(packageName: String) {
        // Retrieve usage stats for the given package
        val stats = getLast24HoursApps().find { it.packageName == packageName }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Build the details message
        val details = buildString {
            append("App: ${getAppLabel(packageName)}\n")
            append("Package: $packageName\n")
            stats?.let {
                append("Total Time in Foreground: ${formatDuration(it.totalTimeInForeground)}\n")
                it.lastTimeUsed.takeIf { time -> time > 0 }?.let { lastUsed ->
                    append("Last Used: ${dateFormat.format(Date(lastUsed))}\n")
                }
            }
            // More details in future
        }

        // Show the details in an alert dialog
        AlertDialog.Builder(context)
            .setTitle("App Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }

    // Helper function to get the app label (name) from its package name
    private fun getAppLabel(packageName: String): String {
        return try {
            val packageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName // Fallback to package name if label is unavailable
        }
    }

    // Helper function to format duration in milliseconds as HH:mm:ss
    private fun formatDuration(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (millis % (1000 * 60)) / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}