package com.joyful.app.pickuplines.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable as Serializable1


data class CategoryListModel (
    val page: Long? = null,
    val perPage: Long? = null,
    val totalItems: Long? = null,
    val totalPages: Long? = null,
    val items: List<CategoryList>? = null
): Serializable1


data class CategoryList (
    @SerializedName("collectionId")
    val collectionID: String? = null,

    val collectionName: String? = null,
    val created: String? = null,
    val id: String? = null,
    val name: String? = null,
    val color: String? = null,
    val textColor: String? = null,
    val assets: String? = null,
    val updated: String? = null
):Serializable1
