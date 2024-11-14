package com.joyful.app.pickuplines.ui.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.xdroid.app.service.data.model.DefaultRequestModel
import com.xdroid.app.service.data.model.ErrorModel
import com.xdroid.app.service.data.repository.MainRepository
import com.xdroid.app.service.utils.constants.NetworkError
import com.xdroid.app.service.utils.enums.Resource
import com.xdroid.app.service.utils.helper.DebugMode
import com.xdroid.app.service.utils.helper.NetworkHelper
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


abstract class BaseVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()
//    private lateinit var mNavigator: WeakReference<N>

    var error = MutableStateFlow(ErrorModel("", ""))

    val getError: MutableStateFlow<ErrorModel>
        get() = error


    /* var navigator: N
         get() = mNavigator.get()!!
         set(navigator) {
             this.mNavigator = WeakReference(navigator)
         }*/

    fun getCompositeDisposable() = compositeDisposable


    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    // Method to simulate an error
    fun simulateError(error: String) {
        _errorMessage.value = error
    }

    // Method to clear the error message
    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
        DebugMode.e("BaseViewModel", "function noCleared() is called.")
    }

    fun requestPostMethod(
        requestModel: DefaultRequestModel,
        liveData: MutableStateFlow<Resource<JsonObject>>
    ) {
        viewModelScope.launch {
            liveData.value = Resource.loading()
            if (networkHelper.isNetworkConnected())
                compositeDisposable.add(mainRepository.postMethodComposite(requestModel, liveData))
            else {
                liveData.value = Resource.error(NetworkError.NO_INTERNET_CONNECTION)
            }
        }
    }


    fun requestGetMethodDispose(
        requestModel: DefaultRequestModel,
        liveData: MutableStateFlow<Resource<JsonObject>>
    ) {
        viewModelScope.launch {
            liveData.value = Resource.loading()
            if (networkHelper.isNetworkConnected())
                compositeDisposable.add(mainRepository.getMethodComposite(requestModel, liveData))
            else {
                liveData.value = Resource.error(NetworkError.NO_INTERNET_CONNECTION)
            }
        }
    }
    fun requestGetArrayMethodDispose(
        requestModel: DefaultRequestModel,
        liveData: MutableStateFlow<Resource<JsonArray>>
    ) {
        viewModelScope.launch {
            liveData.value = Resource.loading()
            if (networkHelper.isNetworkConnected())
                compositeDisposable.add(mainRepository.getArrayMethodComposite(requestModel, liveData))
            else {
                liveData.value = Resource.error(NetworkError.NO_INTERNET_CONNECTION)
            }
        }
    }




    fun requestPutMethod(
        requestModel: DefaultRequestModel,
        liveData: MutableStateFlow<Resource<JsonObject>>
    ) {
        viewModelScope.launch {
            liveData.value = Resource.loading()
            if (networkHelper.isNetworkConnected())
                compositeDisposable.add(
                    mainRepository.putMethodComposite(
                        requestModel,
                        liveData
                    )
                )
            else {
                liveData.value = Resource.error(NetworkError.NO_INTERNET_CONNECTION)
            }
        }
    }


    fun requestDeleteMethod(
        requestModel: DefaultRequestModel,
        liveData: MutableStateFlow<Resource<JsonObject>>
    ) {
        viewModelScope.launch {
            liveData.value = Resource.loading()
            if (networkHelper.isNetworkConnected())
                compositeDisposable.add(
                    mainRepository.deleteMethodComposite(
                        requestModel,
                        liveData
                    )
                )
            else {
                liveData.value = Resource.error(NetworkError.NO_INTERNET_CONNECTION)
            }
        }
    }

}