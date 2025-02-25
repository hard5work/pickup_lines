package com.joyful.app.pickuplines

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joyful.app.pickuplines.ui.layouts.BannerAdView
import com.joyful.app.pickuplines.ui.layouts.CanvasSaveExample
import com.joyful.app.pickuplines.ui.layouts.ImagePickerAndDownloader
import com.joyful.app.pickuplines.ui.layouts.PickupLineMaker
import com.joyful.app.pickuplines.ui.layouts.QuoteEditorApp
import com.joyful.app.pickuplines.ui.layouts.SaveImageFromLocalExample
import com.joyful.app.pickuplines.ui.layouts.lists.ShowCategories
import com.joyful.app.pickuplines.ui.layouts.lists.ShowCategoriesItems
import com.joyful.app.pickuplines.ui.layouts.loadInterstitial
import com.joyful.app.pickuplines.ui.layouts.mInterstitialAd
import com.joyful.app.pickuplines.ui.layouts.showInterstitial
import com.joyful.app.pickuplines.ui.screenname.Screen
import com.joyful.app.pickuplines.ui.screenname.ScreenName
import com.joyful.app.pickuplines.ui.theme.PickupLinesTheme
import com.joyful.app.pickuplines.ui.theme.backGroundColor
import com.joyful.app.pickuplines.ui.theme.black
import com.joyful.app.pickuplines.ui.theme.grey
import com.joyful.app.pickuplines.ui.theme.red
import com.joyful.app.pickuplines.ui.vms.MainViewModel
import com.xdroid.app.service.App.Companion.preferenceHelper
import com.xdroid.app.service.utils.constants.PrefConstant
import com.xdroid.app.service.utils.helper.DebugMode
import com.xdroid.app.service.utils.helper.NetworkHelper
import com.xdroid.app.service.utils.helper.PreferenceHelper
import org.koin.android.ext.android.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val networkHelper: NetworkHelper by inject()
    lateinit var timer: CountDownTimer

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        preferenceHelper.setValue(PrefConstant.COUNTER, 0)
        // Start the 15-minute countdown timer
        loadInterstitial(this)
        timer = object : CountDownTimer(5 * 60 * 1000, 1000) { // 15 minutes
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val time = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)
                DebugMode.e("Time Left $time")
            }

            override fun onFinish() {
                val dialog = android.app.AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("Scrolling for too long")
                dialog.setMessage("Take a small break to continue.")
                dialog.setCancelable(false)
                dialog.setPositiveButton("View an ad") { dialog, _ ->
                    // Handle OK button click
                    if (mInterstitialAd == null) {
                        loadInterstitial(this@MainActivity) { _ ->
                            showInterstitial(this@MainActivity) {
                                timer.start()
                            }
                        }

                    } else {
                        showInterstitial(this@MainActivity) {
                            timer.start()
                        }
                    }
//                    showInterstialAds()
                    dialog.dismiss() // Close the dialog
                }
                dialog.show()
            }
        }
        timer.start()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Check if intent has notification data
        var notificationTitle:String? = /*intent.getStringExtra("notification_title")*/ null
        var notificationMessage :String? = /*intent.getStringExtra("notification_message")*/ null

        val extras = intent.extras
        if (extras != null) {
            DebugMode.e("My DATA ${extras.keySet().size}")
            for ( data in extras.keySet()){
                DebugMode.e("My DATA keysets $data")
            }
            notificationTitle = extras.getString("notification_title")
            notificationMessage = extras.getString("notification_message")

        }

        DebugMode.e("My DATA ${intent.extras.toString()}")
        DebugMode.e("My DATA $notificationTitle $notificationMessage")


        setContent {
            val con = LocalContext.current
            val navController = rememberNavController()
            var buttonClicked by rememberSaveable { mutableStateOf(false) }


            val mainViewModel: MainViewModel = viewModel()
            val title by mainViewModel.title.observeAsState("Pickup Lines") // Observe the title state
            if (buttonClicked) {
                LaunchedEffect(Unit) {
                    DebugMode.e("Always Running 1")
                    if (networkHelper.isNetworkConnected()) {

                        DebugMode.e("Always Running 2")
                        showInterstitial(con) {
                            buttonClicked = false
                            navController.navigateUp()
                        }
                    } else {
                        buttonClicked = false
                        navController.navigateUp()
                    }
                }

            }

            PickupLinesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(modifier = Modifier.background(backGroundColor),
                        topBar = {
                            TopAppBar(
                                title = { Text(text = title) },
                                navigationIcon = {
                                    // Show the back arrow if the back stack has more than one screen
                                    if (title != "Pickup Lines") {
                                        IconButton(onClick = {
                                            if (title == "Editor") {
                                                buttonClicked = true
                                            } else
                                                navController.navigateUp()
                                        }) {
                                            Icon(
                                                Icons.AutoMirrored.Default.ArrowBack,
                                                contentDescription = "Back"
                                            )
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = backGroundColor,
                                    titleContentColor = Color.White,
                                    navigationIconContentColor = Color.White
                                )
                            )
                        }) { padding ->
                        Column(modifier = Modifier.padding(padding)) {
                            MyApp(
                                notificationTitle = notificationTitle,
                                notificationMessage = notificationMessage
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                            {
                                MyApp(navController = navController, mainViewModel)
//                                QuoteEditorApp()

                            }


                            if (networkHelper.isNetworkConnected()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()

                                )
                                {
                                    Column {
                                        Spacer(modifier = Modifier.height(5.dp))
                                        BannerAdView()
                                    }
                                }
                            }
                        }
                    }
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                // Handle case where permission is denied
            }
        }
}


@Composable
fun MyApp(navController: NavHostController, mainViewModel: MainViewModel) {
//    val navController = rememberNavController()
    preferenceHelper = PreferenceHelper(LocalContext.current)
    /*All navigation are included here and all navigation are done from here.*/
    /*Add new route to app for navigation*/

    NavHost(navController, startDestination = ScreenName.CategoryScreen) {
        composable(ScreenName.CategoryScreen) {
            mainViewModel.updateTitle("Pickup Lines")
            ShowCategories(navController)
        }
        composable(ScreenName.ItemScreenData2) { backstack ->
            val quote = backstack.arguments?.getString("categoryId") ?: ""
            val title = backstack.arguments?.getString("name") ?: ""
            mainViewModel.updateTitle(title)
            ShowCategoriesItems(navController, quote, title)
        }

        composable(ScreenName.EditScreenData2) { backstack ->
            val quote = backstack.arguments?.getString("name") ?: "Print Me"
            mainViewModel.updateTitle("Editor")
            PickupLineMaker(quoteText = quote, navController)
        }

    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PickupLinesTheme {
    }
}

object TextDownloadableFontsSnippet1 {
    // [START android_compose_text_df_provider]
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    // [END android_compose_text_df_provider]
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir /* directory */
    )
}

@Composable
fun MyApp(notificationTitle: String?, notificationMessage: String?) {
    // State to control the visibility of the dialog
    var showDialog by remember { mutableStateOf(notificationTitle != null && notificationMessage != null) }

    if (showDialog) {
        NotificationDialog(
            title = notificationTitle ?: "Notification",
            message = notificationMessage ?: "You have a new message.",
            onDismiss = { showDialog = false }
        )
    }

    // Rest of your app's content goes here
}

@Composable
fun NotificationDialog(title: String, message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}