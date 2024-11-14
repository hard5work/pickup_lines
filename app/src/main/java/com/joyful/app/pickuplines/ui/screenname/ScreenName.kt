package com.joyful.app.pickuplines.ui.screenname


object ScreenName {
    //Services
    const val CategoryScreen = "CategoryScreen"
    const val ItemScreen = "ItemScreen"
    const val EditScreen = "EditScreen"
    const val ItemScreenData = "ItemScreen/{categoryId}/{name}"
    const val ItemScreenData2 = "$ItemScreen?categoryId={categoryId}&name={name}"
    const val EditScreenData2 = "$EditScreen?name={name}"

    fun detailRoute(name: String, url: String, cName:String): String {
        return "$name?categoryId=$url&name=$cName"
    }
    fun detailRoute(name: String, url: String): String {
        return "$name?name=$url"
    }

}