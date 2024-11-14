package com.joyful.app.pickuplines.ui.vms

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.joyful.app.pickuplines.data.urls.GetCategoryList
import com.joyful.app.pickuplines.data.urls.getPickLines
import com.joyful.app.pickuplines.ui.base.BaseVM
import com.xdroid.app.service.data.model.DefaultRequestModel
import com.xdroid.app.service.data.repository.MainRepository
import com.xdroid.app.service.utils.enums.Resource
import com.xdroid.app.service.utils.helper.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SingleVm(mainRepository: MainRepository, networkHelper: NetworkHelper) :
    BaseVM(mainRepository, networkHelper) {

    private var catReq = MutableStateFlow<Resource<JsonObject>>(Resource.idle())

    val getCat: StateFlow<Resource<JsonObject>>
        get() = catReq

    private var itemReq = MutableStateFlow<Resource<JsonObject>>(Resource.idle())

    val getItems: StateFlow<Resource<JsonObject>>
        get() = itemReq



    fun getCategories() {
        val defaultRequestModel = DefaultRequestModel()
        defaultRequestModel.url = GetCategoryList
        requestGetMethodDispose(defaultRequestModel, catReq)
    }

    fun getPickupLines(category: String) {
        val defaultRequestModel = DefaultRequestModel()
        defaultRequestModel.url = getPickLines(category)
        requestGetMethodDispose(defaultRequestModel, itemReq)
    }



    override fun onCleared() {
        super.onCleared()
    }


}