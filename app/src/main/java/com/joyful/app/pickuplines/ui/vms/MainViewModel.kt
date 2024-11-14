package com.joyful.app.pickuplines.ui.vms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joyful.app.pickuplines.ui.screenname.Screen

class MainViewModel : ViewModel() {
    // MutableLiveData to hold the title
    private val _title = MutableLiveData("Home")
    val title: LiveData<String> = _title

    // Update the title dynamically when navigating to a different screen
    fun updateTitle(screen: String) {
        _title.value = screen
    }
}