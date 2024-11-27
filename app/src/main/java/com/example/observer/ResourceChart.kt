package com.example.observer

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Debug.getMemoryInfo
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

// Class to display a real-time resource usage chart
class ResourceChart(private val context: Context) {

    // Lists to hold data points for the line chart
    private val timeEntries = mutableListOf<Entry>()
    private val usedRamEntries = mutableListOf<Entry>()
    private val freeRamEntries = mutableListOf<Entry>()
    private var time = 0f // This will represent time in seconds

    // Function to show a real-time chart in a dialog
    fun showRealTimeResourceChart() {
        // Inflate the dialog view
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_resource_chart, null)
        val lineChart = dialogView.findViewById<LineChart>(R.id.resourceLineChart)
        val totalRamTextView = dialogView.findViewById<TextView>(R.id.totalRamTextView)
        val usedRamTextView = dialogView.findViewById<TextView>(R.id.usedRamTextView)
        val freeRamTextView = dialogView.findViewById<TextView>(R.id.freeRamTextView)

        // Initial handler for real time (periodic updates)
        val handler = Handler(Looper.getMainLooper())

        // Create a runnable to update data periodically
        val updateRunnable = object : Runnable {
            override fun run() {
                // Get memory information
                val mem = MemoryUtils(context)
                val memoryInfo = mem.getMemoryInfo()
                val totalRam = memoryInfo["total"]?.toFloatOrNull() ?: 0f
                val usedRam = memoryInfo["used"]?.toFloatOrNull() ?: 0f
                val freeRam = memoryInfo["free"]?.toFloatOrNull() ?: 0f

                // Update text views
                totalRamTextView.text = "Total Usable RAM: ${memoryInfo["total"]} GB"
                usedRamTextView.text = "Used RAM: ${memoryInfo["used"]} GB"
                freeRamTextView.text = "Free RAM: ${memoryInfo["free"]} GB"

                // Add data points for line chart (time vs RAM usage)
                timeEntries.add(Entry(time, time)) // X = time, Y = time for now (just for x-axis labeling)
                usedRamEntries.add(Entry(time, usedRam))
                freeRamEntries.add(Entry(time, freeRam))

                // Update time for next data point
                time += 1

                // Prepare line chart data
                val usedRamDataSet = LineDataSet(usedRamEntries, "Used RAM").apply {
                    color = Color.rgb(255, 102, 0)  // Orange
                    lineWidth = 2f
                    setDrawValues(false)
                    setDrawCircles(false)
                }

                val freeRamDataSet = LineDataSet(freeRamEntries, "Free RAM").apply {
                    color = Color.rgb(0, 204, 102)  // Green
                    lineWidth = 2f
                    setDrawValues(false)
                    setDrawCircles(false)
                }

                val lineData = LineData(usedRamDataSet, freeRamDataSet)

                // Configure the line chart
                lineChart.apply {
                    data = lineData
                    description.isEnabled = false
                    setTouchEnabled(true)
                    setPinchZoom(true)
                    setScaleEnabled(true)
                    setDrawGridBackground(false)
                    invalidate() // Refresh the chart
                }

                // Repeat the update every 1 second
                handler.postDelayed(this, 1000)
            }
        }

        // Start the update loop
        handler.post(updateRunnable)

        // Show the chart in a dialog
        AlertDialog.Builder(context)
            .setTitle("Real-time RAM Utilization")
            .setView(dialogView)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
                handler.removeCallbacks(updateRunnable) // Stop updating when dialog is dismissed
            }
            .create()
            .show()
    }
}
