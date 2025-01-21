package com.joyful.app.pickuplines.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class PickupLineModel (
    val page: Long? = null,
    val perPage: Long? = null,
    val totalItems: Long? = null,
    val totalPages: Long? = null,
    val items: List<PickupModel>? = null
): Serializable


data class PickupModel (
    val category: String? = null,

    @SerializedName("collectionId")
    val collectionID: String? = null,

    val collectionName: String? = null,
    val created: String? = null,
    val id: String? = null,
    val text: String? = null,
    val updated: String? = null
):Serializable

data class AdModel(
    val page: Long? = null,
    val perPage: Long? = null,
    val totalItems: Long? = null,
    val totalPages: Long? = null,
    val items: List<AdItem>? = null
) : Serializable


data class AdItem(
    val app: String? = null,

    @SerializedName("collectionId")
    val collectionID: String? = null,

    val collectionName: String? = null,
    val created: String? = null,
    val id: String? = null,
    val image: String? = null,
    val imageFile: String? = null,
    val link: String? = null,
    val store: String? = null,
    val title: String? = null,
    val type: String? = null,
    val status: Boolean? = false,
    val updated: String? = null
) : Serializable
