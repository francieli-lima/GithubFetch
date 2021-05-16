package br.com.francielilima.githubfetch.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.TextView

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false

    val networkCapabilities: NetworkCapabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun TextView.setVisibility(condition: Boolean = true) {
    this.visibility = if (condition) View.VISIBLE else View.GONE
}