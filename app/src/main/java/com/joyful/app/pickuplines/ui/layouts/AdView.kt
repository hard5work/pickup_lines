package com.joyful.app.pickuplines.ui.layouts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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
import com.joyful.app.pickuplines.data.models.AdItem
import com.joyful.app.pickuplines.data.models.AdModel
import com.joyful.app.pickuplines.data.models.CategoryList
import com.joyful.app.pickuplines.data.models.PickupModel
import com.joyful.app.pickuplines.data.urls.imageUrl
import com.joyful.app.pickuplines.utils.isNull
import com.xdroid.app.service.utils.helper.DebugMode
import kotlinx.coroutines.delay
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
fun AdComposable(adView: AdView, isLoading: Boolean, isAdError: Boolean, adBanner: AdModel?) {
    Column( modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

//        Spacer(modifier = Modifier.height(10.dp))

//        ListBannerAdView()

        AdSection(adBanner)
        ListBannerAdView(adView, isLoading, isAdError)
        Spacer(modifier = Modifier.height(5.dp))

    }

}


@Composable
fun AdSection(adBanner: AdModel? = null) {
    if (adBanner != null) {
        if (adBanner.items?.isNotEmpty().isNull()) {
            val ban = adBanner.items!!
            if (ban.isNotEmpty()) {
                AutoAdSliderNetwork(banner = ban)
            }
        }
    }


}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AutoAdSliderNetwork(banner: List<AdItem>, modifier: Modifier = Modifier) {
    // State to keep track of the current image index
    var currentIndex by remember { mutableStateOf(0) }
    var banners: List<AdItem> by remember {
        mutableStateOf(listOf())
    }
    LaunchedEffect(banner.isNotEmpty()) {
        banners = activeList(banner).shuffled()
        DebugMode.e("asd test banners ${banners.size}")
    }
    if (banners.isNotEmpty()) {


        val context = LocalContext.current

        // Launch a timer using a LaunchedEffect with a key of the current index
        LaunchedEffect(key1 = currentIndex) {
            delay(5000L) // Delay for 5 seconds
            currentIndex =
                (currentIndex + 1) % banners.size // Move to the next image, loop back to the first
        }

        // Display the current image
        // AnimatedContent to handle slide animations


        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn() with
                        slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
            },
            modifier = modifier.fillMaxSize()
        ) { targetIndex ->
            val imagePainter = banners[targetIndex]
            val images = imageUrl + "${imagePainter.collectionID}/${imagePainter.id}/${imagePainter.imageFile}"
//        DebugMode.e("Ad Banners $images")
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { openLinkInBrowser(context, imagePainter.link.isNull()) }
            ) {

                NetworkImageAds(
                    url = images,
                    contentDescription = "Auto Slider Ads",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(18.dp))
                        .padding(vertical = 10.dp),
                )
            }
        }
    }
}



fun activeList(banners: List<AdItem>): List<AdItem> {
    return banners.filter { it.status.isNull() }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NetworkImageAds(
    url: String?,
    modifier: Modifier,
    contentDescription: String? = "Network Image"
) {
    val requestOptions = RequestOptions()
        .skipMemoryCache(true) // Skip memory cache
        .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
    val context = LocalContext.current

    GlideImage(
        model = url,
        contentDescription = contentDescription,
        loading = placeholder(R.drawable.shimmer_shape),
        failure = placeholder(R.drawable.shimmer_shape),
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
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


@Composable
fun ListBannerAdView(adView: AdView, isLoading: Boolean, isAdError: Boolean) {

    val context = LocalContext.current
    val adUnitIds = context.getString(R.string.centerBanner)

//    var isLoading by rememberSaveable { mutableStateOf(true) }
    // Set the size of the ad container (100x100 dp)
    // UI Composition

    var isLoading2 by rememberSaveable { mutableStateOf(false) }
    Column {
        if (isLoading) {
            ShimmerAdPlaceHolder()
        }
        if (isAdError) {
            isLoading2 = true

        } else {
            isLoading2 = false
        }

        AndroidView(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            factory = { adView },
            update = { adView ->
                adView.loadAd(AdRequest.Builder().build())
                // Update logic if needed (e.g., reloading the ad)
            }
        )
    }
}

@Composable
fun ShimmerAdPlaceHolder(
    modifier: Modifier = Modifier
) {
    // Use shimmer effect on placeholder items
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        ShimmerEffect(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
        )

    }
}





var mInterstitialAd: InterstitialAd? = null

fun loadInterstitial(context: Context, onDataLoaded: (Boolean) -> Unit = {}) {
    InterstitialAd.load(
        context,
        context.getString(R.string.interstital), //Change this with your own AdUnitID!
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                onDataLoaded(false)
                DebugMode.e("onAdFailedToLoad  from loadInterstitialAd ->${adError.message}")
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                onDataLoaded(true)
                DebugMode.e("Success Ful onAdLoaded from loadInterstitialAd")
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



fun openLinkInBrowser(context: android.content.Context, url: String) {
    try {
        // Check if it's a Play Store URL
        if (url.contains("play.google.com/store/apps/details")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            // Direct to Play Store if available
            intent.setPackage("com.android.vending") // Play Store package name
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return
            }
        }

        // Fallback to browser if Play Store is not available
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (browserIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(browserIntent)
        } else {
            Toast.makeText(context, "No application can handle this URL.", Toast.LENGTH_SHORT)
                .show()
        }
    } catch (e: Exception) {
        DebugMode.e("Error opening URL: ${e.message}")
        e.printStackTrace()
    }
}


