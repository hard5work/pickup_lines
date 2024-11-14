package com.joyful.app.pickuplines.di.module

import com.joyful.app.pickuplines.BuildConfig
import com.xdroid.app.service.data.networks.ApiHelper
import com.xdroid.app.service.data.networks.ApiHelperImpl
import com.xdroid.app.service.di.module.provideApiService
import com.xdroid.app.service.di.module.provideNetworkHelper
import com.xdroid.app.service.di.module.provideOkHttpClient
import com.xdroid.app.service.di.module.provideRetrofit
import com.xdroid.app.service.utils.helper.PreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { provideOkHttpClient(BuildConfig.DEBUG) }
    single { provideRetrofit(get(), BuildConfig.BASE_URL) }
    single { provideApiService(get()) }
    single { provideNetworkHelper(androidContext()) }
    single { PreferenceHelper(androidContext()) }

    single<ApiHelper> {
        return@single ApiHelperImpl(get())
    }
}
