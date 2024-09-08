package com.swapnil.movielisting.util.internet

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Date

class InternetStateChangeNotifier private constructor(val context: Context) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var networkJob: Job
    private val _connectionState : MutableLiveData<ConnectionState> = MutableLiveData()
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        .build()
    private val currentNetworksSet = mutableSetOf<Network>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            networkJob.cancel()
            networkJob = scope.launch {
                val connectionState = internetConnectionState(context)
                currentNetworksSet.add(network)
                _connectionState.postValue(connectionState)
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                Log.i("ConnectionState", ConnectionState.CONNECTED.name)
                _connectionState.postValue(ConnectionState.CONNECTED)
            } else {
                Log.i("ConnectionState", ConnectionState.DISCONNECTED.name)
                _connectionState.postValue(ConnectionState.DISCONNECTED)
            }
            Log.i("ConnectionStateTime", Date().toString())
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            currentNetworksSet.remove(network)
            if (currentNetworksSet.isEmpty()) {
                _connectionState.postValue(ConnectionState.DISCONNECTED)
            }
        }
    }

    init {
        networkJob = scope.launch(Dispatchers.IO) {
            val connectionState = internetPeripheralConnectionState(context)
            _connectionState.postValue(connectionState)
        }
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    companion object {
    @SuppressLint("StaticFieldLeak")
    private var INSTANCE: InternetStateChangeNotifier? = null
        fun getInstance(context: Context): InternetStateChangeNotifier {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = InternetStateChangeNotifier(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    private fun getNotified(): LiveData<ConnectionState>  {
        return _connectionState
    }

    fun observe(owner: LifecycleOwner, stateChange: (ConnectionState) -> Unit) {
        getNotified().observe(owner) {
            stateChange(it)
        }
    }

    private suspend fun internetConnectionState(context: Context): ConnectionState {
        val isConnected = when(internetPeripheralConnectionState(context)) {
            ConnectionState.CONNECTED, ConnectionState.SLOW -> isInternetAvailable()
            ConnectionState.DISCONNECTED -> false
        }

        return if (isConnected) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.DISCONNECTED
        }
    }
}