package com.xdroid.app.service.data.networks

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable


interface ApiHelper {
    suspend fun postMethod(
        url: String,
        headers: HashMap<String, String>,
        body: JsonObject
    ): Observable<JsonObject>

    suspend fun getMethod(
        url: String,
        headers: HashMap<String, String>
    ): Observable<JsonObject>

    suspend fun getMethodArray(
        url: String,
        headers: HashMap<String, String>
    ): Observable<JsonArray>

    suspend fun putMethod(
        url: String, headers: HashMap<String, String>,
        body: HashMap<String, String>
    ): Observable<JsonObject>

    suspend fun deleteMethod(
        url: String,
        headers: HashMap<String, String>
    ): Observable<JsonObject>


}