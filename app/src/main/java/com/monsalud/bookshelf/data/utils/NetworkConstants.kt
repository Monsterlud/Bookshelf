package com.monsalud.bookshelf.data.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkConstants {
    const val BASE_URL = "https://api.nytimes.com/svc/books/v3"
    const val API_KEY = "kI43UYfjGUF2RFDr4id99ucDAYPBzJkR"
}

sealed class DataError : Throwable() {
    data class Network(val errorType: Throwable) : DataError()
    data class Unknown(val errorType: Throwable) : DataError()
}
