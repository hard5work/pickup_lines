package com.joyful.app.pickuplines.ui.layouts

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.ForwardToInbox
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Gradient
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Padding
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.provider.FontsContractCompat
import androidx.navigation.NavController
import androidx.viewbinding.BuildConfig
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.Size
import coil3.BitmapImage
import coil3.compose.AsyncImage
import coil3.toBitmap
import com.airbnb.lottie.model.content.GradientColor
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.joyful.app.pickuplines.R
import com.joyful.app.pickuplines.TextDownloadableFontsSnippet1.provider
import com.joyful.app.pickuplines.createImageFile
import com.joyful.app.pickuplines.ui.layouts.lists.captureBitmapFromView
import com.joyful.app.pickuplines.ui.layouts.lists.saveImageToDevice
import com.joyful.app.pickuplines.ui.layouts.lists.saveImageToExternalStorage
import com.joyful.app.pickuplines.ui.layouts.lists.saveImageToMediaStore
import com.joyful.app.pickuplines.ui.screenname.ScreenName
import com.joyful.app.pickuplines.ui.theme.backGroundColor
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar
import com.skydoves.orchestra.colorpicker.AlphaSlideBar
import com.skydoves.orchestra.colorpicker.BrightnessSlideBar
import com.skydoves.orchestra.colorpicker.ColorPicker
import com.xdroid.app.service.utils.helper.NetworkHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.compose.koinInject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Objects
import kotlin.text.Typography.quote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupLineMaker(quoteText: String = "Today is a beautiful day.", navController: NavController) {
    var text by remember { mutableStateOf(quoteText) }
    var fontSize by remember { mutableFloatStateOf(45f) }
    var textColor by remember { mutableStateOf(Color.Black) }
    var textAlignment by remember { mutableStateOf(TextAlign.Center) }
    var padding by remember { mutableFloatStateOf(16f) }
    var topPadding by remember { mutableFloatStateOf(16f) }
    var bottomPadding by remember { mutableFloatStateOf(16f) }
    var leftPadding by remember { mutableFloatStateOf(16f) }
    var rightPadding by remember { mutableFloatStateOf(16f) }
    var textStyle by remember { mutableStateOf(TextStyle.Default) }
    var useGradient by remember { mutableStateOf(false) }
    var backgroundColor by remember { mutableStateOf(Color.LightGray) }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var opacity by remember { mutableFloatStateOf(1f) }
    var showBorder by remember { mutableStateOf(false) }
    var currentAlignment by remember { mutableStateOf(TextAlign.Center) }
    var currentFontStyle by remember {
        mutableStateOf(FontStyle.Normal)
    }
    var currentFontWeight by remember {
        mutableStateOf(FontWeight.Normal)
    }
    var currentAlignmentIcon by remember { mutableStateOf(Icons.Default.FormatAlignCenter) }
    var currentStyleIcon by remember { mutableStateOf(Icons.Default.TextFormat) }
    val fontName = GoogleFont("Lobster Two")

    var showTools by remember {
        mutableStateOf(true)
    }
    var showFonts by remember {
        mutableStateOf(false)
    }

    var showFontSize by remember {
        mutableStateOf(false)
    }

    var showColorDialog by remember {
        mutableStateOf(false)
    }

    var showPaddingView by remember {
        mutableStateOf(false)
    }
    var changeBackgroundColor by remember {
        mutableStateOf(false)
    }
    var showGrad by remember {
        mutableStateOf(false)
    }
    var showOpacity by remember {
        mutableStateOf(false)
    }

    var saveImage by remember {
        mutableStateOf(false)
    }

    var shareMessage by remember {
        mutableStateOf(false)
    }
    var showShadow by remember {
        mutableStateOf(false)
    }

    var showImageBlur by remember {
        mutableStateOf(false)
    }
    val fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )


    var firstColor by remember { mutableStateOf(Color.LightGray) }
    var secondColor by remember { mutableStateOf(Color.LightGray) }
    var selectedFont by remember { mutableStateOf(fontFamily) }
    var alpha by remember { mutableStateOf(1f) }
    var shadowColor by remember { mutableStateOf(Color.Gray) }
    var shadowOffsetX by remember { mutableStateOf(0f) }
    var shadowOffsetY by remember { mutableStateOf(0f) }
    var shadowBlurRadius by remember { mutableStateOf(0f) }
    var imageBlur by remember { mutableStateOf(0f) }


    val context = LocalContext.current
    val file = context.createImageFile()

    val view = remember { ComposeView(context) }

    // State to track if permission was granted
    var isPermissionGranted by remember { mutableStateOf(false) }

    // Create launcher to request permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isPermissionGranted = granted
        if (granted) {
            // Capture and save image after permission is granted
            captureBitmap(view) { uri ->
                uri?.let {

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Image"))

                }
            }
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }


    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null) {
                    imageUri = Uri.parse(data.data.toString())
                    if (imageUri.toString().isNotEmpty()) {
                        Log.d("myImageUri", "$imageUri ")
                    }
                }
            }
        }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
//            cameraLauncher.launch(uri)
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            galleryLauncher.launch(galleryIntent)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    // Function to cycle through alignments
    fun cycleAlignment() {
        when (currentAlignment) {
            TextAlign.Center -> {
                currentAlignment = TextAlign.Start
                currentAlignmentIcon = Icons.AutoMirrored.Filled.FormatAlignLeft
            }

            TextAlign.Start -> {
                currentAlignment = TextAlign.End
                currentAlignmentIcon = Icons.AutoMirrored.Filled.FormatAlignRight
            }

            TextAlign.End -> {
                currentAlignment = TextAlign.Center
                currentAlignmentIcon = Icons.Default.FormatAlignCenter
            }

            else -> {
                currentAlignment = TextAlign.Center
                currentAlignmentIcon = Icons.Default.FormatAlignCenter
            }
        }
    }

    fun cycleFontStyle() {
        when {
            // If currently Normal, change to Bold
            currentFontStyle == FontStyle.Normal && currentFontWeight == FontWeight.Normal -> {
                currentFontWeight = Bold
                currentStyleIcon = Icons.Default.FormatBold
            }
            // If currently Bold, change to Italic
            currentFontStyle == FontStyle.Normal && currentFontWeight == Bold -> {
                currentFontStyle = FontStyle.Italic
                currentFontWeight = FontWeight.Normal
                currentStyleIcon = Icons.Default.FormatItalic
            }
            // If currently Italic, reset to Normal
            currentFontStyle == FontStyle.Italic -> {
                currentFontStyle = FontStyle.Normal
                currentFontWeight = FontWeight.Normal
                currentStyleIcon = Icons.Default.TextFormat
            }
        }
    }

    val networkHelper: NetworkHelper = koinInject()

    var buttonClicked by rememberSaveable { mutableStateOf(false) }
    if (networkHelper.isNetworkConnected()) {
        if (mInterstitialAd == null)
            loadInterstitial(LocalContext.current)
    }

    if (buttonClicked) {
        if (networkHelper.isNetworkConnected()) {
            showInterstitial(LocalContext.current) {
                buttonClicked = false
                navController.navigateUp()
            }
        } else {
            navController.navigateUp()
        }
    }


        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Preview Area
            Box(
                modifier = Modifier
                    .weight(1f)
//                .size(1000.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, backgroundColor, RoundedCornerShape(8.dp))
            ) {
                // Background (Image or Color)
                imageUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(imageBlur.dp)
                            .clip(RoundedCornerShape(8.dp)),

                        contentScale = ContentScale.Crop,
                        alpha = alpha,
                        onSuccess = { result ->
                            // Capture the bitmap from the loaded image

                            Log.e("Success", "sdjkasd ajd asdjadka Success ${result.result}")
                            Log.e("Success", "sdjkasd ajd asdjadka Success ${result.result.image}")
                            Log.e("Success", "sdjkasd ajd asdjadka Success ${result}")
                            bitmap = (result.result.image as BitmapImage).bitmap

                        },
                        onError = {
                            Log.e(
                                "asda",
                                "sdjkasd ajd asdjadka sdeerrorrrororororororo ${it.result}"
                            )
                            // Handle error if needed
//                Toast.makeText(LocalContext.current, "Error loading image", Toast.LENGTH_SHORT).show()
                        }
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .let {
                            if (useGradient) {
                                it.background(
                                    Brush.verticalGradient(
                                        listOf(firstColor, secondColor)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            } else {
                                it.background(backgroundColor, shape = RoundedCornerShape(8.dp))
                            }
                        },

                    )

                // Editable TextField
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(
                            start = leftPadding.dp,
                            top = topPadding.dp,
                            end = rightPadding.dp,
                            bottom = bottomPadding.dp
                        )
                        .alpha(opacity)
                        .let {
                            if (showBorder) {
                                it
                                    .border(
                                        width = 1.dp,
                                        color = textColor.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(8.dp)
                            } else {
                                it
                            }
                        },
                    textStyle = TextStyle(
                        fontSize = fontSize.sp,
                        color = textColor,
                        fontFamily = selectedFont,
                        textAlign = currentAlignment,
                        fontStyle = currentFontStyle,
                        fontWeight = currentFontWeight,
                        shadow = Shadow(
                            color = shadowColor,
                            offset = Offset(shadowOffsetX, shadowOffsetY),
                            blurRadius = shadowBlurRadius
                        )
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = when (textAlignment) {
                                TextAlign.Center -> Alignment.Center
                                TextAlign.End -> Alignment.CenterEnd
                                else -> Alignment.CenterStart
                            }
                        ) {
                            innerTextField()
                        }
                    },
                    cursorBrush = SolidColor(textColor)
                )
            }

            if (showTools) {
                // Control Panel
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        /* Show font size dialog */
                        IconButton(
                            onClick = {
                                showFontSize = true
                                showTools = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.TextFields,
                                contentDescription = "Text Size"
                            )
                        }
                    }
                    item {
                        /* Show color picker */
                        IconButton(
                            onClick = {
                                showColorDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Text Color"
                            )
                        }
                    }
                    item {
                        /* Show Font Family Picker */
                        IconButton(
                            onClick = {
                                showFonts = true
                                showTools = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FontDownload,
                                contentDescription = "Fonts"
                            )
                        }
                    }
                    item {

                        /*Text Alignment */
                        IconButton(
                            onClick = { cycleAlignment() }
                        ) {
                            Icon(
                                imageVector = currentAlignmentIcon,
                                contentDescription = when (currentAlignment) {
                                    TextAlign.Center -> "Center Align"
                                    TextAlign.Start -> "Left Align"
                                    TextAlign.End -> "Right Align"
                                    else -> "Align Text"
                                }
                            )
                        }
                    }
                    item {
                        /* Show padding controls */
                        IconButton(
                            onClick = {

                                showPaddingView = true
                                showTools = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Padding,
                                contentDescription = "Padding"
                            )
                        }
                    }
                    item {

                        /* Show Text Styles, */
                        IconButton(
                            onClick = {
                                cycleFontStyle()
                            }
                        ) {
                            Icon(
                                imageVector = currentStyleIcon,
                                contentDescription = "Text style"
                            )
                        }
                    }
                    item {

                        /* Show Text Styles, */
                        IconButton(
                            onClick = {
                                showShadow = true
                                showTools = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inbox,
                                contentDescription = "Box Shadow"
                            )
                        }
                    }
                    item {
                        IconButton(
                            onClick = {
                                useGradient = true
                                showGrad = true
                                showTools = false
                                imageUri = null

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gradient,
                                contentDescription = "Gradient"
                            )
                        }
                    }
                    item {
                        /* Show color picker */
                        IconButton(
                            onClick = {
                                useGradient = false
                                changeBackgroundColor = true
                                imageUri = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ColorLens,
                                contentDescription = "Color"
                            )
                        }
                    }
                    item {
                        IconButton(
                            onClick = {
                                val permissionCheckResult =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.READ_MEDIA_IMAGES
                                        ) == PackageManager.PERMISSION_GRANTED
                                    } else {
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        ) == PackageManager.PERMISSION_GRANTED &&
                                                ContextCompat.checkSelfPermission(
                                                    context,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                                ) == PackageManager.PERMISSION_GRANTED
                                    }
                                if (permissionCheckResult) {
                                    val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                                        type = "image/*"
                                    }
                                    galleryLauncher.launch(galleryIntent)
                                } else {
                                    // Request a permission
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                    } else {
                                        galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Image, contentDescription = "Image")
                        }
                    }
                    /* Show opacity slider */
                    item {
                        IconButton(
                            onClick = {
                                showOpacity = true
                                showTools = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Opacity,
                                contentDescription = "Opacity"
                            )
                        }
                    }
                    /*Image Blur*/
                    item {
                        IconButton(
                            onClick = {
                                showImageBlur = true
                                showTools = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.BlurOn,
                                contentDescription = "Blur Image"
                            )
                        }
                    }
                    item {

                        IconButton(
                            onClick = {
                                saveImage = true

                            }
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                        }
                    }
                    item {
/* Share image */
                        IconButton(
                            onClick = { shareMessage = true }
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                        }
                    }
                }
            } else {
                if (showFontSize) {
                    FontSizeDialog(currentSize = fontSize, onSizeChange = { fs ->
                        fontSize = fs
                    }) {
                        showFontSize = false
                        showTools = true

                    }
                }
                if (showImageBlur) {
                    ImageBlurSelection(currentSize = imageBlur, onSizeChange = { fs ->
                        imageBlur = fs
                    }) {
                        showImageBlur = false
                        showTools = true

                    }
                }
                if (showFonts) {
                    FontSelectionScreen(onFontSelected = { fontFamily ->
                        selectedFont = fontFamily
                    }, onClicked = {
                        showFonts = false
                        showTools = true
                    })
                }
                if (showOpacity) {
                    ChangeOpacity(alpha = alpha, onValueChange = {
                        alpha = it
                    }) {
                        showOpacity = false
                        showTools = true
                    }
                }
                if (showShadow) {
                    ShadowPicker(color = shadowColor,
                        offX =
                        shadowOffsetX,
                        offY = shadowOffsetY,
                        blurRadius = shadowBlurRadius,
                        updated = { c, x, y, r ->
                            shadowColor = c
                            shadowOffsetX = x
                            shadowOffsetY = y
                            shadowBlurRadius = r
                        }) {
                        showShadow = false
                        showTools = true

                    }
                }
                if (showPaddingView) {

                    PaddingAdjusterDemo(
                        top = topPadding,
                        bottom = bottomPadding,
                        left = leftPadding,
                        right = rightPadding,
                        padding = { top, btm, left, right ->
                            topPadding = top
                            bottomPadding = btm
                            leftPadding = left
                            rightPadding = right
                        }, onClicked = {
                            showPaddingView = false
                            showTools = true
                        })
                }

                if (showGrad) {
                    GradientColor(
                        currentFirstColor = firstColor,
                        currentSecondColor = secondColor,
                        onColorFirstChange = {
                            firstColor = it
                        },
                        onColorSecondChange = {
                            secondColor = it
                        }

                    ) {
                        showTools = true
                        showGrad = false

                    }
                }

            }

            if (saveImage) {
//            if (bitmap!= null){
//              saveImageWithText(bitmap!!,text,context)
//            }
//            else {
//                SaveBitmapToStorage(bitmap) {
//                    saveImage = false
//                    Log.e("asdad", it + " akjdjhasgdjhasgdjhas ")
//                }
                saveQuoteToDevice(
                    context,
                    text,
                    backgroundColor,
                    imageUri,
                    blurRadius = imageBlur,
                    imageOpacity = alpha,
                    textColor = textColor,
                    textAlign = currentAlignment,
                    fontFamily = fontFamily
                )
//            }
            }

            if (showColorDialog) {
                ColorPickerDialog(currentColor = textColor, onColorChange = { c ->
                    textColor = Color(c)
                }, onDismiss = {
                    showColorDialog = false
                }) {
                    showColorDialog = false
                }

            }
            if (shareMessage) {
                ShareCardView(share = shareMessage) {
                    shareMessage = it

                }

            }
            if (changeBackgroundColor) {
                ColorPickerDialog(currentColor = backgroundColor, onColorChange = { c ->
                    backgroundColor = Color(c)
                }, onDismiss = {
                    changeBackgroundColor = false
                }) {
                    changeBackgroundColor = false
                }

            }
        }

}

@Composable
fun BoxContentToCapture() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.Blue)
            .padding(16.dp)
    ) {
        Text("Sample Content", color = Color.White)
    }
}

// Dialog composables for each feature
@Composable
fun FontSizeDialog(
    currentSize: Float,
    onSizeChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Adjust Font Size")
        Spacer(modifier = Modifier.height(10.dp))
        Slider(
            value = currentSize,
            onValueChange = onSizeChange,
            valueRange = 12f..100f
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onDismiss) {
            Text("Done")
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

}


fun saveImageWithText(bitmap: Bitmap, text: String, context: Context) {
    // Create a new bitmap with the same dimensions
    val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val canvas = Canvas(resultBitmap)

    // Draw the original bitmap
    canvas.drawBitmap(bitmap, 0f, 0f, null)

    // Set up the paint for the text
    val paint = Paint().apply {
        color = android.graphics.Color.BLACK // Text color
        textSize = 50f // Text size
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    // Draw the text on the bitmap
    canvas.drawText(text, 50f, 50f, paint) // Positioning the text; adjust as needed

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {


        // Save the image using MediaStore API
        saveImageToMediaStore(context, bitmap)
    } else {
        saveImageToExternalStorage(context, bitmap)
    }
}

// Dialog composables for each feature
@Composable
fun ImageBlurSelection(
    currentSize: Float,
    onSizeChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Blur Image")
        Spacer(modifier = Modifier.height(10.dp))
        Slider(
            value = currentSize,
            onValueChange = onSizeChange,
            valueRange = 0f..50f
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onDismiss) {
            Text("Done")
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

}

// Dialog composables for each feature
@Composable
fun GradientColor(
    currentFirstColor: Color,
    currentSecondColor: Color,
    onColorFirstChange: (Color) -> Unit,
    onColorSecondChange: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var frst by remember {
        mutableStateOf(false)
    }
    var second by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Gradient color")
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = {
                frst = true
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)) {
                Text(text = "First Color", color = Color.Black)
            }
            TextButton(
                onClick = { second = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text(text = "Second Color", color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onDismiss) {
            Text("Done")
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
    if (frst) {
        ColorPickerDialog(currentColor = currentFirstColor, onColorChange = { c ->
            onColorFirstChange(Color(c))
        }, onDismiss = {
            frst = false
        }) {
            frst = false
        }

    }
    if (second) {
        ColorPickerDialog(currentColor = currentSecondColor, onColorChange = { c ->
            onColorSecondChange(Color(c))
        }, onDismiss = {
            second = false
        }) {
            second = false
        }

    }

}


@Composable
fun ColorPickerDialog(
    currentColor: Color,
    onColorChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val (selectedColor, setSelectedColor)
            = remember { mutableStateOf(ColorEnvelope(currentColor.toArgb())) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Choose color ${selectedColor.hexCode}",
                color = Color(selectedColor.color)
            )
        },
        text = {
            ColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                onColorListener = { envelope, _ ->
                    setSelectedColor(envelope)
                    Log.e("color", envelope.color.toString())
                    Log.e("color", envelope.hexCode.toString())
                    onColorChange(envelope.color)
                },
                initialColor = currentColor,
                children = { colorPickerView ->
                    Column(modifier = Modifier.padding(top = 32.dp)) {
                        Box(modifier = Modifier.padding(vertical = 6.dp)) {
                            AlphaSlideBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                colorPickerView = colorPickerView
                            )
                        }
                        Box(modifier = Modifier.padding(vertical = 6.dp)) {
                            BrightnessSlideBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                colorPickerView = colorPickerView
                            )
                        }
                    }
                }
            )

        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CANCEL")

            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        }
    )


    // Implement color picker dialog
}


// Dialog composables for each feature
@Composable
fun ChangeOpacity(
    alpha: Float,
    onValueChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {


    // Overlay content
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Adjust Background Opacity",
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Slider to control the opacity
        Slider(
            value = alpha,
            onValueChange = { newAlpha -> onValueChange(newAlpha) },
            valueRange = 0f..1f, // Set range from fully transparent (0) to fully opaque (1)
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { onDismiss() }) {
            Text(text = "Done")

        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


// Add other dialog composables for remaining features

// Helper function to save the image
private suspend fun saveImage(context: Context) {
    // Implement image saving logic
}

// Helper function to share the image
private fun shareImage(context: Context, uri: Uri) {
    // Implement sharing logic
}


// Helper function to capture the bitmap (same as above)
private fun captureBitmap(view: View, onResult: (Uri?) -> Unit) {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)

    // Save the bitmap to a file in cache or external storage
    val file = File(view.context.cacheDir, "preview_image.png")
    try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val uri = FileProvider.getUriForFile(
            view.context,
            "${view.context.packageName}.provider",
            file
        )
        onResult(uri)
    } catch (e: IOException) {
        e.printStackTrace()
        onResult(null)
    }
}


fun captureAndSaveBitmap(context: Context, bitmap: Bitmap): Uri? {
    // Create file in external cache directory
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File(directory, "captured_image_${System.currentTimeMillis()}.png")

    try {
        // Save the bitmap as a PNG
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Notify success
        Toast.makeText(context, "Image saved!", Toast.LENGTH_SHORT).show()
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        return null
    }
}


@Composable
fun SaveBitmapToStorage(myBitmap: Bitmap?, onAction: (String) -> Unit) {
    val context = LocalContext.current
    val view = LocalView.current
    val bitmap = captureBitmapFromView(view)

// Create a content resolver reference
//    val contentResolver = context.contentResolver
    saveImageToDevice(context, bitmap)
    onAction("Completed")
//
//// For Android 10 (API 29) and above, save the image using MediaStore
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        // Create an image file in the Pictures directory
//        val contentValues = ContentValues().apply {
//            put(
//                MediaStore.Images.Media.DISPLAY_NAME,
//                "pickup_lines_${System.currentTimeMillis()}.png"
//            )
//            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
//            put(
//                MediaStore.Images.Media.RELATIVE_PATH,
//                Environment.DIRECTORY_PICTURES
//            ) // Save to "Pictures"
//        }
//
//        // Insert the image into the MediaStore and get a URI
//        val imageUri: Uri? =
//            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//
//        // If the URI is not null, write the image to the output stream
//        imageUri?.let { uri ->
//            try {
//                contentResolver.openOutputStream(uri)?.use { outputStream ->
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                    outputStream.flush()
//                }
//
//                // Once saved, the image should show up in the gallery
//                onAction(uri.toString()) // Pass the URI to handle it as needed
//            } catch (e: IOException) {
//                e.printStackTrace()
//                onAction("") // Handle failure
//            }
//        }
//    } else {
//        // For Android 9 (API 28) and below, you can still use the app's external directory
//        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val file = File(directory, "pickup_lines_${System.currentTimeMillis()}.png")
//        try {
//            FileOutputStream(file).use { outputStream ->
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                outputStream.flush()
//            }
//            // Refresh the gallery to show the image (this is for older versions)
////            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
//            onAction(Uri.fromFile(file).toString()) // Pass the URI to handle it as needed
//        } catch (e: IOException) {
//            e.printStackTrace()
//            onAction("") // Handle failure
//        }
//    }
}


@Composable
fun ShareCardView(
    share: Boolean,
    update: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    var sd by remember {
        mutableStateOf(
            share
        )
    }


    if (sd) {
        val bitmap = captureBitmapFromView(view)
        // Share the screenshot as an image

        val file: Uri = saveBitmapToFile(context, bitmap)


        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM, file)
        }

        val shareChooser = Intent.createChooser(shareIntent, null)
        val resInfoList: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(shareChooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName: String = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                file,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        context.startActivity(shareChooser)
        sd = false
        update(sd)
        /* val shareIntent = Intent().apply {
             action = Intent.ACTION_SEND
             type = "image/png"
             val bitmapUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
             putExtra(Intent.EXTRA_STREAM, bitmapUri)
         }
         val shareChooser = Intent.createChooser(shareIntent, null)
         context.startActivity(shareChooser)*/
    }
}

fun captureBitmapFromView(view: android.view.View): Bitmap {


    val bitmap = Bitmap.createBitmap(view.width, (view.height - 300), Bitmap.Config.ARGB_8888)
        .copy(Bitmap.Config.ARGB_8888, true)
    try {

        val canvas = android.graphics.Canvas(bitmap)
        view.draw(canvas)

//        canvas.drawBitmap(bitmap, 0f,0f, null)
    } catch (e: Exception) {

        Log.e("failed canvas error", e.message.toString())
//
//        val canvas = android.graphics.Canvas(bitmap)
//       canvas.drawBitmap(bitmap, 0f,0f, null)
    }


    return bitmap
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "${currentTimeMillis}_pickup_lines.png")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
    // Grant URI
    /*  context.grantUriPermission(
        context.packageName,
        fileUri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    )*/
    return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
}

val currentTimeMillis = System.currentTimeMillis()



fun saveQuoteToDevice(
    context: Context, quote: String, backgroudColor: Color, imageUri: Uri?,
    blurRadius: Float = 15f,
    imageOpacity: Float = 1f,
    textColor: Color,
    textAlign: TextAlign,
    fontFamily: FontFamily
) {
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
        color = textColor.toArgb()
        textSize = 70f
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }

    val copyRight = TextPaint().apply {
        color =  textColor.toArgb()
        textSize = 30f
        isAntiAlias = true
    }
    val padding = 10f
    // Create a StaticLayout to handle multiline text
    val staticLayout = StaticLayout.Builder.obtain(
        quote,
        0,
        quote.length,
        textPaint,
        width - 80
    ) // Subtract 40 for padding
        .setAlignment(Layout.Alignment.ALIGN_CENTER)
        .setLineSpacing(1f, 1f)
        .setIncludePad(false)
        .build()

    // Draw the background (red)
    canvas.drawColor(backgroudColor.toArgb())

    Log.e("Test data", "$imageOpacity -> ${imageOpacity*255}")

    // Draw the image from the URI (if available)
    imageUri?.let { uri ->
        val imageBitmap = loadBitmapFromUri(context, uri)
        imageBitmap?.let {
            // Apply blur and opacity
            val blurredBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                applyBlurWithRenderEffect(it, blurRadius)
            } else {
                applyBlurWithBlurMaskFilter(it, blurRadius)
            }
            val paint = Paint().apply {
                alpha = (imageOpacity*255).toInt() // Set opacity
            }
            // Draw the image, centered, scaled down to fit within the canvas
            val imageX = (canvas.width - it.width) / 2f
            val imageY = (canvas.height - it.height) / 4f  // Top quarter of canvas
            canvas.drawBitmap(it, imageX, imageY, paint)
        }
    }

    // Draw the multiline quote centered on the canvas
    val yPosition = (bitmap.height / 2) - (staticLayout.height / 2) // Vertically center the text
    canvas.save()
    canvas.translate(padding, yPosition.toFloat()) // Apply horizontal margin and center vertically
    staticLayout.draw(canvas)
    canvas.restore()

    val align = when(textAlign){
        TextAlign.Center -> Layout.Alignment.ALIGN_CENTER
        TextAlign.Right -> Layout.Alignment.ALIGN_CENTER
        TextAlign.Left -> Layout.Alignment.ALIGN_CENTER
        else -> Layout.Alignment.ALIGN_CENTER
    }
    // Create a StaticLayout for the additional text
    val staticLayoutAdditional = StaticLayout.Builder.obtain("©Joyful", 0, 7, copyRight, width)
        .setAlignment(align)
        .setLineSpacing(1f, 1f)
        .setIncludePad(false)
        .build()
// Calculate vertical position for the additional text
    val additionalTextYPosition = (bitmap.height - 80) // 20 pixels below the quote
    canvas.save()
    canvas.translate(padding, additionalTextYPosition.toFloat()) // Apply horizontal margin
    staticLayoutAdditional.draw(canvas)
    canvas.restore()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Save the image using MediaStore API
        saveImageToMediaStore(context, bitmap)
    } else {
        saveImageToExternalStorage(context, bitmap)
    }


}



// Function to load a Bitmap from a URI
fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        context.contentResolver.openInputStream(uri).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


// Function to apply blur effect to a Bitmap using RenderEffect (API 31+)
@RequiresApi(Build.VERSION_CODES.S)
fun applyBlurWithRenderEffect(bitmap: Bitmap, radius: Float): Bitmap {
    val blurredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(blurredBitmap)
    val paint = Paint()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        val renderEffect = RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP)
//        paint.setRenderEffect(renderEffect)  // Available in API 31 and above
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
    return blurredBitmap
}

// Function to apply blur effect to a Bitmap using BlurMaskFilter (for older versions)
fun applyBlurWithBlurMaskFilter(bitmap: Bitmap, radius: Float): Bitmap {
    val blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(blurredBitmap)
    val paint = Paint()
    paint.isAntiAlias = true
    paint.maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return blurredBitmap
}


