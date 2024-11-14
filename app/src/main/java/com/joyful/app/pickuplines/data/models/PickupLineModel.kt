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
