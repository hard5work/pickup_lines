package com.xdroid.app.service.utils.helper

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken


object DynamicResponse {
    inline fun <reified T> Gson.fromJson(json: String?): T {
        return fromJson(json, object: TypeToken<T>() {}.type)
    }

    inline fun <reified T> myObject(jsonObject: JsonObject?): T {
        return Gson().fromJson(
            jsonObject,
            object : TypeToken<T>() {}.type
        )
    }
    inline fun <reified T> myArray(jsonObject: JsonArray?): T {
        return Gson().fromJson(
            jsonObject,
            object : TypeToken<T>() {}.type
        )
    }
}