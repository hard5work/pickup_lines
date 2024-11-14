package com.xdroid.app.service.utils.helper

import android.util.Log


object DebugMode {
    fun e(tag: String, message: String, topic: String = "Failed") {

        Log.e("D $tag", "D $topic -> $message")

    }

    fun e(message: String, topic: String = "Failed") {

        Log.e("D Compose", "D $topic -> $message")

    }
}