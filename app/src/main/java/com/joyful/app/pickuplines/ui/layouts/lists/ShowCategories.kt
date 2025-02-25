package com.joyful.app.pickuplines.ui.layouts.lists

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.joyful.app.pickuplines.R
import com.joyful.app.pickuplines.TextDownloadableFontsSnippet1.provider
import com.joyful.app.pickuplines.data.models.AdModel
import com.joyful.app.pickuplines.data.models.CategoryList
import com.joyful.app.pickuplines.data.models.CategoryListModel
import com.joyful.app.pickuplines.ui.dialogs.InfoAlertDialog
import com.joyful.app.pickuplines.ui.dialogs.LoadingAlertDialog
import com.joyful.app.pickuplines.ui.layouts.AdComposable
import com.joyful.app.pickuplines.ui.layouts.BannerAdView
import com.joyful.app.pickuplines.ui.layouts.CheesyLoader
import com.joyful.app.pickuplines.ui.layouts.LoadingContentColumn
import com.joyful.app.pickuplines.ui.layouts.LottieAnimationFromUrl
import com.joyful.app.pickuplines.ui.layouts.itemsWithAds
import com.joyful.app.pickuplines.ui.layouts.itemsWithAds2
import com.joyful.app.pickuplines.ui.layouts.loadInterstitial
import com.joyful.app.pickuplines.ui.layouts.mInterstitialAd
import com.joyful.app.pickuplines.ui.layouts.showInterstitial
import com.joyful.app.pickuplines.ui.screenname.ScreenName
import com.joyful.app.pickuplines.ui.theme.backGroundColor
import com.joyful.app.pickuplines.ui.theme.cards
import com.joyful.app.pickuplines.ui.theme.colorPrimary
import com.joyful.app.pickuplines.ui.theme.colorPrimaryShades
import com.joyful.app.pickuplines.ui.theme.colorPrimaryTints
import com.joyful.app.pickuplines.ui.theme.red
import com.joyful.app.pickuplines.ui.theme.secondaryColor
import com.joyful.app.pickuplines.ui.theme.tertiary
import com.joyful.app.pickuplines.ui.vms.SingleVm
import com.xdroid.app.service.App.Companion.preferenceHelper
import com.xdroid.app.service.utils.constants.PrefConstant
import com.xdroid.app.service.utils.enums.Status
import com.xdroid.app.service.utils.helper.DebugMode
import com.xdroid.app.service.utils.helper.DynamicResponse
import com.xdroid.app.service.utils.helper.NetworkHelper
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.util.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCategories(
    navController: NavController
) {
    val myViewModel: SingleVm = koinViewModel()

    val states by myViewModel.getCat.collectAsState()
    val isDataLoaded = rememberSaveable { mutableStateOf(false) }

    val adBanner by myViewModel.adBanner.collectAsState()
    LaunchedEffect(Unit) {
        if (!isDataLoaded.value) {
            DebugMode.e("askdjhaksjdhjasdahsd ${isDataLoaded.value}")

            myViewModel.getCategories() // Set as loaded to prevent future calls


        }
        if (adBanner == null){
            myViewModel.getADList()
        }
    }
    val itemModel = rememberSaveable { mutableStateOf(CategoryListModel()) }
    var showView by rememberSaveable { mutableStateOf(false) }
    var showAlert by rememberSaveable { mutableStateOf(false) }
    var alertMessage by rememberSaveable { mutableStateOf("") }
    var categoryItems by rememberSaveable { mutableStateOf(listOf<CategoryList>()) }



    when (states.status) {
        Status.ERROR -> {
            LaunchedEffect(Unit) {
                showView = false
                alertMessage = states.message ?: "Something went wrong"
                showAlert = true
            }
        }

        Status.SUCCESS -> {
            LaunchedEffect(Unit) {
                if (!isDataLoaded.value) {
                    val response = DynamicResponse.myObject<CategoryListModel>(states.data)
                    DebugMode.e("data loaded $response")
                    itemModel.value = response
                    categoryItems = itemModel.value.items!!
                    isDataLoaded.value = true
                    showView = true
                    showAlert = false
                }
            }
        }

        Status.IDLE -> {
            showView = false
            DebugMode.e("data Idle state")


        }

        Status.LOADING -> {
            showView = false

            DebugMode.e("data loading state")


        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .padding(bottom = 5.dp)
        ) {


            if (showView) {
                NavList(
                    items = categoryItems,
                    navController = navController,
                    adBanner = adBanner
                )
            } else {
                LoadingContentColumn()
            }

            if (showAlert) {
                InfoAlertDialog(message = alertMessage) {
                    showAlert = false

                }
            }


        }

    }


}


@Composable
fun NavList(
    items: List<CategoryList>?,
    navController: NavController,
    adBanner: AdModel? = null
) {
    val count = 2
    DebugMode.e("length ${items?.size}")
    val newitems = rememberSaveable(items) {
        itemsWithAds(items)
    }
    val context = LocalContext.current
    val adUnitIds = rememberSaveable { context.getString(R.string.centerBanner) }

    // Keep track of the loading state
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isAdError by rememberSaveable { mutableStateOf(false) }

    // Use a static AdView instance to avoid recreation
    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.LARGE_BANNER)
            adUnitId = adUnitIds
        }
    }

    // Set the AdListener only once
    DisposableEffect(adView) {
        val adListener = object : AdListener() {
            override fun onAdLoaded() {
                DebugMode.e("Banner ad load success")
                isLoading = false
                isAdError = false
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                DebugMode.e("Banner ad load Failed-> ${error.message}")
                isLoading = false
                isAdError = true
            }
        }
        adView.adListener = adListener

        onDispose {
            adView.adListener = object : AdListener() {}
        }
    }

    // Load the ad only once
    LaunchedEffect(adView) {
        adView.loadAd(AdRequest.Builder().build())
    }

    LazyColumn {

        items(newitems.size, key = { index ->
            (if (newitems[index] is CategoryList) {
                val ite = newitems[index] as CategoryList
                ite.id// or a unique identifier for MyItems
            } else {
                "ad_$index"  // Assign a unique key for ad items
            })!!

        }) { index ->
            if (newitems[index] is CategoryList) {
                val ite = newitems[index] as CategoryList
                NavigationItem(ite, navController)
            } else {
                AdComposable(adView, isLoading, isAdError, adBanner)
            }

        }
//        if (items != null)
//            items(items.size) { index ->
//                NavigationItem(items[index], navController)
//            }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NavigationItem(
    item: CategoryList,
    navController: NavController,
    modifier: Modifier = Modifier.fillMaxWidth()
) {

//    val networkHelper: NetworkHelper = koinInject()
//
//    var buttonClicked by rememberSaveable { mutableStateOf(false) }
//    if (networkHelper.isNetworkConnected()) {
//        if (mInterstitialAd == null)
//            loadInterstitial(LocalContext.current)
//    }
    var navigate by remember {
        mutableStateOf(
            false
        )
    }
    var counter by remember {
        mutableIntStateOf(preferenceHelper.getValue(PrefConstant.COUNTER, 0) as Int)
    }


    var showloading by remember {
        mutableStateOf(
            false
        )
    }
    if (navigate) {
        navigate = false
        if (counter < 5) {
            navController.navigate(
                ScreenName.detailRoute(
                    ScreenName.ItemScreen,
                    item.id!!,
                    item.name!!
                )
            )
            counter += 1
            preferenceHelper.setValue(PrefConstant.COUNTER, counter)
            counter = preferenceHelper.getValue(PrefConstant.COUNTER, 0) as Int
        } else {
            showloading = true
            showInterstitial(LocalContext.current) {

                navController.navigate(
                    ScreenName.detailRoute(
                        ScreenName.ItemScreen,
                        item.id!!,
                        item.name!!
                    )
                )
                counter = 0
                showloading = false
                preferenceHelper.setValue(PrefConstant.COUNTER, counter)
                counter = preferenceHelper.getValue(PrefConstant.COUNTER, 0) as Int
            }
        }


    }

    if (showloading) {
        LoadingAlertDialog()
    }

    Card(

        modifier = modifier
            .padding(8.dp)
//            .shadow(elevation = 1.dp, shape = RoundedCornerShape(10.dp))
            .clickable {
                navigate = true



            },
        colors = CardDefaults.cardColors(
            containerColor = Color(
                android.graphics.Color.parseColor(
                    item.color
                )
            )
        ),
        elevation = CardDefaults.cardElevation(5.dp)
//         Color(android.graphics.Color.parseColor(item.color))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.name!!,
                    color = Color(android.graphics.Color.parseColor(item.textColor)),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = FontFamily(
                            Font(googleFont = GoogleFont("Open Sans"), fontProvider = provider)
                        )
                    )
                )
                LottieAnimationFromUrl(url = item.assets!!)
            }

            Spacer(modifier = Modifier.height(5.dp))

        }


    }


}


@Composable
fun Toolbar(text: String = "") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, color = Color.Black)
    }
}