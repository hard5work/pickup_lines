package com.joyful.app.pickuplines.ui.layouts

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.joyful.app.pickuplines.MainActivity
import com.joyful.app.pickuplines.R
import com.joyful.app.pickuplines.data.models.CategoryList
import com.joyful.app.pickuplines.data.models.PickupModel
import com.xdroid.app.service.utils.helper.DebugMode
import java.util.Date


@Composable
fun BannerAdView() {

    val context = LocalContext.current
    val adUnitIds = context.getString(R.string.bannerId)
    //"ca-app-pub-3940256099942544/6300978111"
    AndroidView(
        modifier = Modifier
            .fillMaxWidth(),

        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                // Add your adUnitID, this is for testing.
                adUnitId = adUnitIds
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            adView.loadAd(AdRequest.Builder().build())
        }
    )
}



fun itemsWithAds(items: List<CategoryList>?): List<Any> {
    val mixedList = mutableListOf<Any>()
    items?.forEachIndexed { index, item ->
        mixedList.add(item)
        // Add an ad placeholder every `interval` items
//        DebugMode.e("calculated Value ${(index + 1) % 15}")
        if ((index + 1) % 4 == 0) {
            mixedList.add("AdItem")
        }
    }
    return mixedList
}
fun itemsWithAds2(items: List<PickupModel>?): List<Any> {
    val mixedList = mutableListOf<Any>()
    items?.forEachIndexed { index, item ->
        mixedList.add(item)
        // Add an ad placeholder every `interval` items
//        DebugMode.e("calculated Value ${(index + 1) % 15}")
        if ((index + 1) % 4 == 0) {
            mixedList.add("AdItem")
        }
    }
    return mixedList
}

@Composable
fun AdComposable() {
    Column( modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.height(10.dp))

        ListBannerAdView()
        Spacer(modifier = Modifier.height(5.dp))

    }

}
@Composable
fun ListBannerAdView() {

    val context = LocalContext.current
    val adUnitIds = context.getString(R.string.centerBanner)

    var isLoading by remember { mutableStateOf(true) }
    // Set the size of the ad container (100x100 dp)
    Column {


        if(isLoading)
            SingleAdShimmer()

        AndroidView(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            factory = { context ->
                AdView(context).apply {
//                setAdSize(adSize)
                    setAdSize(AdSize.LARGE_BANNER)
                    adUnitId = adUnitIds
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            isLoading = false
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            isLoading = true
                        }
                    }

                    loadAd(AdRequest.Builder().build())
                }
            },
            update = { adView ->
                adView.loadAd(AdRequest.Builder().build())
            }
        )
    }
}




var mInterstitialAd: InterstitialAd? = null

fun loadInterstitial(context: Context) {
    InterstitialAd.load(
        context,
        context.getString(R.string.interstital), //Change this with your own AdUnitID!
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null

                DebugMode.e("onAdFailedToLoad ${adError.message}")
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd

                DebugMode.e("Success Ful onAdLoaded")
            }
        }
    )
}

fun showInterstitial(context: Context, onAdDismissed: () -> Unit) {
    val activity = context as Activity
    DebugMode.e("isInit? $mInterstitialAd")
    if (mInterstitialAd == null){
        onAdDismissed()
    }
    if (mInterstitialAd != null && activity != null) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(e: AdError) {
                mInterstitialAd = null
                DebugMode.e("Error Message ${e.message}")
            }

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null

                DebugMode.e("Success Ful")
                loadInterstitial(context)
                onAdDismissed()
            }
        }
        mInterstitialAd?.show(activity)
    }
}

fun removeInterstitial() {
    mInterstitialAd?.fullScreenContentCallback = null
    mInterstitialAd = null
}

object OpenApp{
    private var appOpenAd: AppOpenAd? = null
    private var isShowingAd = false
    private var loadTime: Long = 0

    fun loadAppOpenAd(context: Context) {
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            context, context.getString(R.string.openApp), adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    // Handle the error
                }
            }
        )
    }

    fun isAdAvailable(): Boolean {
        return appOpenAd != null && (Date().time - loadTime) < 4 * 3600000 // 4 hours in milliseconds
    }

    fun showAppOpenAdIfAvailable(context: Context) {
        if (!isShowingAd && isAdAvailable()) {
            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    loadAppOpenAd(context)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    isShowingAd = false
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }
            appOpenAd?.show(MainActivity())

        } else {
            loadAppOpenAd(context)
        }
    }


}
