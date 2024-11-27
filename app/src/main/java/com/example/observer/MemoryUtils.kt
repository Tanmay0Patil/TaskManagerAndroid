package com.example.observer

import android.app.ActivityManager
import android.content.Context
import android.icu.text.DecimalFormat
import android.os.Build
import android.widget.Toast

// Class for memory related operations
class MemoryUtils (private val context: Context){

    // Function to clear RAM by terminating running processes, which itself shuts down the app
    fun clearRAM() {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = activityManager.runningAppProcesses

        // Iterate over running processes and kill them
        processes?.forEach { process ->
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    android.os.Process.killProcess(process.pid)     // Kill
                }
            } catch (e: Exception) {
                e.printStackTrace()     // Log any exceptions during process termination
            }
        }

        Toast.makeText(context, "RAM Cleared", Toast.LENGTH_SHORT).show()
        // not completed still errors
    }

    // Function to retrieve memory information: total, free, and used RAM
    fun getMemoryInfo(): Map<String, String> {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()

        activityManager.getMemoryInfo(memoryInfo)   // get memory details

        // Memory in GB
        val decimalFormat = DecimalFormat("#.##")
        val totalMemoryGB = decimalFormat.format(memoryInfo.totalMem / (1024.0 * 1024.0 * 1024.0))
        val freeMemoryGB = decimalFormat.format(memoryInfo.availMem / (1024.0 * 1024.0 * 1024.0))
        val usedMemoryGB =
            decimalFormat.format((memoryInfo.totalMem - memoryInfo.availMem) / (1024.0 * 1024.0 * 1024.0))

        return mapOf(
            "total" to totalMemoryGB,
            "free" to freeMemoryGB,
            "used" to usedMemoryGB
        )
    }
}