package com.example.observer

// Necessary imports
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Main Screen of our app
class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var totalRamTextView: TextView
    private lateinit var usedRamTextView: TextView
    private lateinit var freeRamTextView: TextView
    private lateinit var clearRamButton: Button
    private lateinit var resourceChartButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppAdapter

    // For periodic UI updates
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 2000 // 2 seconds

    // Runnable for refreshing UI
    private val uiUpdater = object : Runnable {
        override fun run() {
            refreshUI()
            handler.postDelayed(this, updateInterval)
        }
    }

    // Entry point when app opens
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        initializeViews()

        // Check and request usage permission if needed
        val perm = UsagePermission(this)
        if (!perm.checkUsagePermission()) {
            perm.requestUsagePermission()
        }
        // For scrollable listing of app activities(recent)
        setupRecyclerView()

        // Set up button listeners
        setupButtonListeners()

        // Start UI updates
        handler.post(uiUpdater)
    }

    // Stop the handler when activity is Destroyed
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(uiUpdater)
    }

    // Initialise views from layout
    private fun initializeViews() {
        totalRamTextView = findViewById(R.id.totalRamTextView)
        usedRamTextView = findViewById(R.id.usedRamTextView)
        freeRamTextView = findViewById(R.id.freeRamTextView)
        clearRamButton = findViewById(R.id.clearRamButton)
        resourceChartButton = findViewById(R.id.resourceChartButton)
        recyclerView = findViewById(R.id.runningAppsRecyclerView)
    }


    private fun setupButtonListeners() {
        // Clear RAM when button clicked
        clearRamButton.setOnClickListener {
            val mem = MemoryUtils(this)
            mem.clearRAM()
        }
        // Show RAM vs time chart
        resourceChartButton.setOnClickListener {
            val chart = ResourceChart(this)
            chart.showRealTimeResourceChart()
        }
    }

    private fun refreshUI() {
        // Update mem info
        val mem = MemoryUtils(this)

        val memoryInfo = mem.getMemoryInfo()
        totalRamTextView.text = "Total Usable RAM: ${memoryInfo["total"]} GB"
        usedRamTextView.text = "Used RAM: ${memoryInfo["used"]} GB"
        freeRamTextView.text = "Free RAM: ${memoryInfo["free"]} GB"

        // Update RecyclerView with apps used in the last 24 hours
        val usage = UsageStatsHelper(this)
        val last24HoursApps = usage.getLast24HoursApps().map { it.packageName }
        adapter.updateData(last24HoursApps)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        // helper for usage stats and killing tasks
        val info = UsageStatsHelper(this);
        val kill1 = KillTasks(this);

        // Creating adapter with click listener
        adapter = AppAdapter(
            emptyList(),
            onAppClick = { appName -> info.showAppDetailsDialog(appName) },
            onKillAppClick = { packageName -> kill1.killApp(packageName) }
        )
        recyclerView.adapter = adapter
        refreshUI()
    }
}

