package com.monsalud.bookshelf.data.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkUtils {
    fun hasInternetConnection(connectivityManager: ConnectivityManager?): Boolean {
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}

object NetworkConstants {
    const val BASE_URL = "https://api.nytimes.com/svc/books/v3"
    const val API_KEY = "G492oBh8zC5wPahLX8Nlqj1GJoGAzEjq"
}

sealed class DataError : Throwable() {
    data class Network(val errorType: Throwable) : DataError()
    data class Unknown(val errorType: Throwable) : DataError()
}
