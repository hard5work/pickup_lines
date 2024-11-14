package com.joyful.app.pickuplines.ui.screenname

sealed class Screen(val route: String, val title: String) {
    object Home : Screen(ScreenName.CategoryScreen, "Pickup Lines")
    object Items : Screen(ScreenName.ItemScreenData2, ScreenName.ItemScreen)
    object Edit : Screen(ScreenName.EditScreenData2, ScreenName.EditScreen)

    data class Item(val routeName: String, val dynamicTitle: String) : Screen(routeName, dynamicTitle)

}