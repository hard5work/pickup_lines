package com.joyful.app.pickuplines.di.module

import com.joyful.app.pickuplines.ui.vms.SingleVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SingleVm(get(), get()) }
}