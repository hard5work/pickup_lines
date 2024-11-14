package com.xdroid.app.service

import android.app.Application
import android.content.Context
import com.xdroid.app.service.utils.helper.PreferenceHelper
import java.io.File


class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        preferenceHelper = PreferenceHelper(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    companion object {
        lateinit var baseApplication: Context
        lateinit var preferenceHelper: PreferenceHelper

    }

    override fun onTerminate() {
        super.onTerminate()
    }

}

