package com.joyful.app.pickuplines.ui.layouts.lists

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.joyful.app.pickuplines.TextDownloadableFontsSnippet1.provider
import com.joyful.app.pickuplines.data.models.CategoryList
import com.joyful.app.pickuplines.data.models.CategoryListModel
import com.joyful.app.pickuplines.data.models.PickupLineModel
import com.joyful.app.pickuplines.data.models.PickupModel
import com.joyful.app.pickuplines.ui.dialogs.InfoAlertDialog
import com.joyful.app.pickuplines.ui.layouts.AdComposable
import com.joyful.app.pickuplines.ui.layouts.LoadingContentColumn
import com.joyful.app.pickuplines.ui.layouts.captureBitmapFromView
import com.joyful.app.pickuplines.ui.layouts.currentTimeMillis
import com.joyful.app.pickuplines.ui.layouts.itemsWithAds
import com.joyful.app.pickuplines.ui.layouts.itemsWithAds2
import com.joyful.app.pickuplines.ui.layouts.loadInterstitial
import com.joyful.app.pickuplines.ui.layouts.mInterstitialAd
import com.joyful.app.pickuplines.ui.layouts.saveBitmapToFile
import com.joyful.app.pickuplines.ui.layouts.showInterstitial
import com.joyful.app.pickuplines.ui.screenname.ScreenName
import com.joyful.app.pickuplines.ui.theme.backGroundColor
import com.joyful.app.pickuplines.ui.theme.black
import com.joyful.app.pickuplines.ui.theme.colorPrimary
import com.joyful.app.pickuplines.ui.theme.white
import com.joyful.app.pickuplines.ui.vms.SingleVm
import com.xdroid.app.service.utils.enums.Status
import com.xdroid.app.service.utils.helper.DebugMode
import com.xdroid.app.service.utils.helper.DynamicResponse
import com.xdroid.app.service.utils.helper.NetworkHelper
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.format.TextStyle
import java.util.Random
import kotlin.text.Typography.quote


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCategoriesItems(
    navController: NavController,
    name:String,
    title:String
) {
    val myViewModel: SingleVm = koinViewModel()

    val states by myViewModel.getItems.collectAsState()
    val isDataLoaded = rememberSaveable { mutableStateOf(false) }

    if (!isDataLoaded.value) {
        DebugMode.e("askdjhaksjdhjasdahsd ${isDataLoaded.value}")
        LaunchedEffect(Unit) {
            myViewModel.getPickupLines(name) // Set as loaded to prevent future calls
        }
    }
    val itemModel = rememberSaveable { mutableStateOf(PickupLineModel()) }
    var showView by rememberSaveable { mutableStateOf(false) }
    var showAlert by rememberSaveable { mutableStateOf(false) }
    var alertMessage by rememberSaveable { mutableStateOf("") }
    var categoryItems by rememberSaveable { mutableStateOf(listOf<PickupModel>()) }



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
                    val response = DynamicResponse.myObject<PickupLineModel>(states.data)
                    DebugMode.e("data loaded $response")
                    itemModel.value = response
                    categoryItems = itemModel.value.items!!.shuffled(Random())
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
        Column(modifier = Modifier
            .fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 5.dp)
            ) {


                if(showView){
                    ItemList(
                        items = categoryItems,
                        navController = navController
                    )
                }
                else{
                    LoadingContentColumn()
                }

                if(showAlert){
                    InfoAlertDialog(message = alertMessage) {
                        showAlert = false

                    }
                }



            }

        }



}


@Composable
fun ItemList(
    items: List<PickupModel>?,
    navController: NavController
) {
    val count = 2
    DebugMode.e("length ${items?.size}")
    val newitems = rememberSaveable(items) {
        itemsWithAds2(items)
    }
    LazyColumn {


        items(newitems.size, key = { index ->
            (if (newitems[index] is PickupModel) {
                val ite = newitems[index] as PickupModel
                ite.id// or a unique identifier for MyItems
            } else {
                "ad_$index"  // Assign a unique key for ad items
            })!!

        }) { index ->
            if (newitems[index] is PickupModel) {
                val ite = newitems[index] as PickupModel
                QuoteBox(quote = ite.text!!, navController)
            } else {
                AdComposable()
            }

        }
//        if (items != null)
//            items(items.size) { index ->
////                PickupItem(items[index].text!!, navController)
//                QuoteBox(quote = items[index].text!!, navController)
//            }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PickupItem(
    item: String,
    navController: NavController,
    modifier: Modifier = Modifier.fillMaxWidth()
) {

    Box(
        modifier = modifier
            .padding(8.dp)
            .background(colorPrimary, shape = RoundedCornerShape(10.dp))
            .clickable {
                navController.navigate(ScreenName.detailRoute(ScreenName.EditScreen, item))


            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = item, color = Color.Black)
            Spacer(modifier = Modifier.height(5.dp))
        }


    }
}


@Composable
fun ShareDialog(onDismiss: () -> Unit, onShareText: String, context: Context) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Share Options") },

        confirmButton = {
            Button(onClick = {
                shareText(onShareText, context) // Replace with actual text input
                onDismiss()
            }) {
                Text("Share as Text")
            }
        },
        dismissButton = {
            Button(onClick = {
                val bitmap = captureBitmapFromView(onShareText)
                val file: Uri = saveBitmapToFile(context, bitmap)
                shareImage(file, context)
                onDismiss()
            }) {
                Text("Share as Image")
            }
        }
    )
}

private fun shareText(quote: String, context: Context) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, quote)
        type = "text/plain"
    }
    context.startActivity(
        Intent.createChooser(shareIntent, "Share Quote")
    )
}

private fun shareImage(imageFile: Uri, context: Context) {
    // Implement share image functionality// Replace with your package name
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, imageFile)
        putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.xdroid.app.changewallpaper.anime")
        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    // Start the chooser
    context.startActivity(
        Intent.createChooser(intent, "Share image via")
    )
}

@Composable
fun QuoteBox(
    quote: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF1F1F1), // Light gray background
    textColor: Color = Color.Black,
    elevation: Dp = 4.dp
) {

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val networkHelper: NetworkHelper = koinInject()

    var buttonClicked by rememberSaveable { mutableStateOf(false) }
    if (networkHelper.isNetworkConnected()) {
        if (mInterstitialAd == null)
            loadInterstitial(LocalContext.current)
    }

    var showDialog by remember { mutableStateOf(false) }

    if (buttonClicked) {
        LaunchedEffect(Unit) {
            if (networkHelper.isNetworkConnected()) {
                showInterstitial(context) {
                    buttonClicked = false
                    navController.navigate(ScreenName.detailRoute(ScreenName.EditScreen, quote))
                }
            } else {
                buttonClicked = false
                navController.navigate(ScreenName.detailRoute(ScreenName.EditScreen, quote))

            }
        }


    }
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(elevation)
        ) {
            Column(
            ) {

                Box(modifier = Modifier
                    .background(color = black)
                    .fillMaxWidth()) {

                    Column(modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 70.dp)
                        .fillMaxWidth()) {
                        Icon(Icons.Default.FormatQuote, contentDescription = "quote" , tint = Color.White, modifier= Modifier
                            .size(40.dp)
                            .graphicsLayer { rotationZ = 180f })
                        Text(
                            text = "$quote",
                            style = androidx.compose.ui.text.TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily(
                                    Font(googleFont = GoogleFont("Space Mono"), fontProvider = provider)
                                )
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                        )
                        Icon(Icons.Default.FormatQuote, contentDescription = "quote" , tint = Color.White, modifier = Modifier
                            .align(Alignment.End)
                            .size(40.dp) )
                    }
                    Column( modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)) {
                        IconButton(
                            onClick = {
                               buttonClicked = true
                            },
                            modifier = Modifier
                                .background(color = white, shape = CircleShape)

                            // Padding to avoid overlap with Card's edge
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Brush,
                                contentDescription = "More Options",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(16.dp) // Adjust size as needed
                            )
                        }
                    }



                }

                // Circular Icon Button at Top Right


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Copy Button
                    IconButton(onClick = {
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(quote))
                        Toast.makeText(context, "Quote copied to clipboard", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.CopyAll,
                            contentDescription = "Copy",
                            tint = textColor
                        )
                    }

                    // Share Button
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = textColor
                        )
                    }

                    // Save Button
                    IconButton(onClick = {
                        // Implement your save logic here
                        // For example, save to a database or shared preferences
                        saveQuoteAsImage(context, quote)

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "Save",
                            tint = textColor
                        )
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Dialog
        if (showDialog) {
            ShareDialog(onDismiss = { showDialog = false }, onShareText = quote, context)
        }


    }
   
}



fun saveQuoteAsImage(context: Context, quote: String) {
    // Check if the device is running API 29 (Android 10) or higher, and if so, use MediaStore for saving images.
    // Create Bitmap and Canvas
//    val displayMetrics = context.resources.displayMetrics
//    val widthInPixels = displayMetrics.widthPixels
//    val heightInPixels = displayMetrics.heightPixels

    val width = 1000
    val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
/*
    // Setup Paint to style text
    val paint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 20f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    // Draw the background (black)
    canvas.drawColor(android.graphics.Color.RED)

    canvas.drawText(quote, (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), paint)*/
    // Setup TextPaint for better text handling
    val textPaint = TextPaint().apply {
        color = android.graphics.Color.WHITE
        textSize = 50f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC)
    }

    val copyRight = TextPaint().apply {
        color = android.graphics.Color.WHITE
        textSize = 30f
        isAntiAlias = true
    }
    val padding = 10f
    // Create a StaticLayout to handle multiline text
    val staticLayout = StaticLayout.Builder.obtain(quote, 0, quote.length, textPaint, width-80) // Subtract 40 for padding
        .setAlignment(Layout.Alignment.ALIGN_CENTER)
        .setLineSpacing(1f, 1f)
        .setIncludePad(false)
        .build()

    // Draw the background (red)
    canvas.drawColor(android.graphics.Color.BLACK)

    // Draw the multiline quote centered on the canvas
    val yPosition = (bitmap.height / 2) - (staticLayout.height / 2) // Vertically center the text
    canvas.save()
    canvas.translate(padding, yPosition.toFloat()) // Apply horizontal margin and center vertically
    staticLayout.draw(canvas)
    canvas.restore()

    // Create a StaticLayout for the additional text
    val staticLayoutAdditional = StaticLayout.Builder.obtain("©Joyful", 0, 7, copyRight, width)
        .setAlignment(Layout.Alignment.ALIGN_CENTER)
        .setLineSpacing(1f, 1f)
        .setIncludePad(false)
        .build()
// Calculate vertical position for the additional text
    val additionalTextYPosition = (bitmap.height  - 80) // 20 pixels below the quote
    canvas.save()
    canvas.translate(padding, additionalTextYPosition.toFloat()) // Apply horizontal margin
    staticLayoutAdditional.draw(canvas)
    canvas.restore()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {


        // Save the image using MediaStore API
        saveImageToMediaStore(context, bitmap)
    } else {
        saveImageToExternalStorage(context,bitmap)
    }


}


fun captureBitmapFromView(quote: String): Bitmap {

    // Check if the device is running API 29 (Android 10) or higher, and if so, use MediaStore for saving images.
    // Create Bitmap and Canvas
//    val displayMetrics = context.resources.displayMetrics
//    val widthInPixels = displayMetrics.widthPixels
//    val heightInPixels = displayMetrics.heightPixels

    val width = 1000
    val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    /*
        // Setup Paint to style text
        val paint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 20f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        // Draw the background (black)
        canvas.drawColor(android.graphics.Color.RED)

        canvas.drawText(quote, (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), paint)*/
    // Setup TextPaint for better text handling
    val textPaint = TextPaint().apply {
        color = android.graphics.Color.WHITE
        textSize = 50f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC)
    }

    val copyRight = TextPaint().apply {
        color = android.graphics.Color.WHITE
        textSize = 30f
        isAntiAlias = true
    }
    val padding =10f
    // Create a StaticLayout to handle multiline text
    val staticLayout = StaticLayout.Builder.obtain(quote, 0, quote.length, textPaint, width-80) // Subtract 40 for padding
        .setAlignment(Layout.Alignment.ALIGN_CENTER)
        .setLineSpacing(1f, 1f)
        .setIncludePad(false)
        .build()

    // Draw the background (red)
    canvas.drawColor(android.graphics.Color.BLACK)

    // Draw the multiline quote centered on the canvas
    val yPosition = (bitmap.height / 2) - (staticLayout.height / 2) // Vertically center the text
    canvas.save()
    canvas.translate(padding, yPosition.toFloat()) // Apply horizontal margin and center vertically
    staticLayout.draw(canvas)
    canvas.restore()

    // Create a StaticLayout for the additional text
    val staticLayoutAdditional = StaticLayout.Builder.obtain("©Joyful", 0, 7, copyRight, width)
        .setAlignment(Layout.Alignment.ALIGN_CENTER)
        .setLineSpacing(1f, 1f)
        .setIncludePad(false)
        .build()
// Calculate vertical position for the additional text
    val additionalTextYPosition = (bitmap.height  - 80) // 20 pixels below the quote
    canvas.save()
    canvas.translate(padding, additionalTextYPosition.toFloat()) // Apply horizontal margin
    staticLayoutAdditional.draw(canvas)
    canvas.restore()



    return bitmap
}

fun saveImageToExternalStorage(context: Context, bitmap: Bitmap){
    // For devices running lower API levels (API 28 and below), you can save directly to the external storage as shown previously.

    try {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${currentTimeMillis}_pickup_lines.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Toast.makeText(context, "Quote saved as image", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving image", Toast.LENGTH_SHORT).show()
    }
}



fun saveImageToMediaStore(context: Context, bitmap: Bitmap) {
    // Get the content resolver
    val contentResolver = context.contentResolver

    // Create a ContentValues object to hold information about the image
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "quote_image_${System.currentTimeMillis()}.png")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Quotes")  // Save to Pictures/Quotes directory
    }

    // Insert the image into MediaStore
    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    // If the insertion is successful, write the Bitmap data to the output stream
    if (imageUri != null) {
        try {
            val outputStream: OutputStream? = contentResolver.openOutputStream(imageUri)
            outputStream?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                it.flush()
                Toast.makeText(context, "Quote saved as image", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving image", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Error saving image", Toast.LENGTH_SHORT).show()
    }
}

fun saveImageToDevice(context: Context, bitmap: Bitmap){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Save the image using MediaStore API
        saveImageToMediaStore(context, bitmap)
    } else {
        saveImageToExternalStorage(context,bitmap)
    }
}