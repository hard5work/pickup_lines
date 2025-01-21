package com.joyful.app.pickuplines.data.urls

const val GetCategoryList = "pickup_category/records"
const val GetPickLines = "pickup_lines/records?filter=(category="

val adUrl = "ad/records?filter=(type=%27centerBanner%27)"

val imageUrl = "https://anish.pockethost.io/api/files/"


fun getPickLines(category: String): String {
    return "$GetPickLines\"$category\")"
}