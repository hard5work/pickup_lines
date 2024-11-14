package com.joyful.app.pickuplines.di.module

import com.xdroid.app.service.data.repository.MainRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepository(get())
    }
}