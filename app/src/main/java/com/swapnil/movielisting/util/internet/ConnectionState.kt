package com.swapnil.movielisting.util.internet
import android.app.job.JobInfo.NETWORK_TYPE_ANY
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

enum class ConnectionState {
    CONNECTED, SLOW, DISCONNECTED
}

suspend fun isInternetAvailable(): Boolean {
    val client: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    val request: Request = Request.Builder()
        .url("https://www.google.com")
        .method("GET", null)
        .build()

    val response: Response? = try {
        client.newCall(request).execute()
    } catch (e: Exception) {
        null
    }
    val responseCode = response?.code ?: 999
    response?.close()
    return responseCode.toString().startsWith("2")
}

fun isConnectedToNetwork(networkType: Int, context: Context): Boolean {
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return (activeNetworkInfo != null && activeNetworkInfo.isConnected
            && (networkType == NETWORK_TYPE_ANY || activeNetworkInfo.type == networkType))
}

suspend fun internetPeripheralConnectionState(context: Context): ConnectionState {
    return if (isConnectedToNetwork(NETWORK_TYPE_ANY, context)) {
        ConnectionState.CONNECTED
    } else {
        ConnectionState.DISCONNECTED
    }
}