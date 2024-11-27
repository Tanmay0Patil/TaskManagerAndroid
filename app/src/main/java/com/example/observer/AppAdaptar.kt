package com.example.observer

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppAdapter(
    private var appList: List<String>,
    private val onAppClick: (String) -> Unit,
    private val onKillAppClick: (String) -> Unit
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    // ViewHolder for holding the app item layout
    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName: TextView = itemView.findViewById(R.id.appName)
        val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        val killAppButton: Button = itemView.findViewById(R.id.killAppButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        // Inflate item layout for each app
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val packageName = appList[position]
        val context = holder.itemView.context

        try {
            // Get app info
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)

            // Set app name
            val appName = packageManager.getApplicationLabel(applicationInfo).toString()
            holder.appName.text = appName

            // Load and set app icon
            val icon = packageManager.getApplicationIcon(applicationInfo)
            holder.appIcon.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            // Fallback for apps that can't be found
            holder.appName.text = packageName
            holder.appIcon.setImageResource(android.R.drawable.sym_def_app_icon)
        }

        // Handle item clicks
        holder.itemView.setOnClickListener {
            onAppClick(packageName)
        }

        // Kill app button
        holder.killAppButton.setOnClickListener {
            onKillAppClick(packageName)
        }
    }

    override fun getItemCount(): Int = appList.size

    // Update the app list and refresh the RecyclerView
    fun updateData(newAppList: List<String>) {
        appList = newAppList
        notifyDataSetChanged()
    }
}