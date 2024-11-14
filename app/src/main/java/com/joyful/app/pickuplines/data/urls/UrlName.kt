package com.joyful.app.pickuplines.data.urls

const val GetCategoryList = "pickup_category/records"
const val GetPickLines = "pickup_lines/records?filter=(category="


fun getPickLines(category: String): String {
    return "$GetPickLines\"$category\")"
}