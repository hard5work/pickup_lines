package com.xdroid.app.service.data.networks

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*


interface ApiService {

    @POST()
    fun postMethod(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @Body jsonObject: JsonObject
    ): Observable<JsonObject>

    @GET()
    fun getMethod(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>
    ): Observable<JsonObject>

    @GET()
    fun getMethodArray(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>
    ): Observable<JsonArray>


    @FormUrlEncoded
    @PUT()
    fun putMethod(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>,
        @FieldMap body: HashMap<String, String>
    ): Observable<JsonObject>


    @DELETE()
    fun deleteMethod(
        @Url url: String,
        @HeaderMap headers: HashMap<String, String>
    ): Observable<JsonObject>

}