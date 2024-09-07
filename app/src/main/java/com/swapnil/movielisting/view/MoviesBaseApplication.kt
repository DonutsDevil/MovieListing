package com.swapnil.movielisting.view

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesBaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}