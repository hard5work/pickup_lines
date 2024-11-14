package com.xdroid.app.service.data.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class DefaultRequestModel {
    var url: String = ""
    var headers: HashMap<String, String> = HashMap()
    var loginBody: HashMap<String, String> = HashMap()
    var body: JsonObject = JsonObject()
    var setToken: Boolean = true
    var fileBody = HashMap<String, RequestBody>()
    var fileBodyDocs = HashMap<String, List<HashMap<String, RequestBody>>>()
    var fileBodyDocs2 = HashMap<String, List<RequestBody>>()
    var fileBodyList = ArrayList<HashMap<String, RequestBody>>()
    var files = ArrayList<MultipartBody.Part>()
    var multipleFile: HashMap<String, MultipartBody.Part> = HashMap()
    var multipleFileDriver: List<MultipartBody.Part> = ArrayList<MultipartBody.Part>()
    var file: MultipartBody.Part? = null
}

// Extensions.kt
fun createPartFromString(stringData: String): RequestBody {
    return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
}

data class ErrorModel(
    @SerializedName("status")
    val title: String,
    val message: String
)