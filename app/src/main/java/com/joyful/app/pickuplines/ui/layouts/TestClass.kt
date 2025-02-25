package com.joyful.app.pickuplines.ui.layouts

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.drawToBitmap
import com.joyful.app.pickuplines.ui.layouts.lists.saveImageToExternalStorage
import com.joyful.app.pickuplines.ui.layouts.lists.saveImageToMediaStore
import com.joyful.app.pickuplines.ui.theme.black
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.text.Typography.quote


@Composable
fun QuoteEditorApp() {
    val context = LocalContext.current
    var quoteText by remember { mutableStateOf("Write your quote here...") }
    var textColor by remember { mutableStateOf(Color.Black) }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var backgroundImage by remember { mutableStateOf<ImageBitmap?>(null) }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val imageBitmap = context.loadImageBitmap(it,context,20)
            backgroundImage = imageBitmap
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Text Field for Quote
        BasicTextField(
            value = quoteText,
            onValueChange = { quoteText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor)
        )

        // Color Pickers
        ColorPicker(onColorSelected = { textColor = it })
        Spacer(modifier = Modifier.height(8.dp))
        ColorPicker(onColorSelected = { backgroundColor = it })

        // Image Picker
        Button(onClick = {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Text("Add Background Image")
        }

        // Preview Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(backgroundColor)
        ) {
            backgroundImage?.let { image ->
                Image(
                    bitmap = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = quoteText,
                color = textColor,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // Download Button
        Button(
            onClick = {
                saveComposableAsImage(
                    composable = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(backgroundColor)
                        ) {
                            backgroundImage?.let { image ->
                                Image(
                                    bitmap = image,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Text(
                                text = quoteText,
                                color = textColor,
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    context = context
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Download as Image")
        }
    }
}

@Composable
fun ColorPicker(onColorSelected: (Color) -> Unit) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Black, Color.White)
    Row {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color)
                    .clickable { onColorSelected(color) }
            )
        }
    }
}




fun saveComposableAsImage(composable: @Composable () -> Unit, context: Context,onAction: (Boolean) -> Unit = {}) {
//    val bitmap = Bitmap.createBitmap(1080, 1080, android.graphics.Bitmap.Config.ARGB_8888)
//    val canvas = Canvas(bitmap)
//    val composeView = ComposeView(context).apply {
//        setContent {
//            composable()
//        }
//    }
////    canvas.drawColor(black.toArgb())
//    composeView.draw(canvas)
//    canvas.save()
//
//    // Save bitmap to storage
//    saveBitmapToStorage(bitmap, context)

    val activity = context as? Activity ?: throw IllegalStateException("Context is not an Activity")

// Get the root view of the activity
    val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)

// Create ComposeView and set content
    val composeView = ComposeView(context).apply {
        setContent { composable() }
        layoutParams = ViewGroup.LayoutParams(1080, 1080) // Fixed size
    }

// Attach ComposeView to the root view
    rootView.addView(composeView)

// Post a task to measure and layout after attachment
    composeView.post {
        composeView.measure(
            View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY)
        )
        composeView.layout(0, 0, 1080, 1080)

        // Create bitmap and draw ComposeView
        val bitmap = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        composeView.draw(canvas)

        // Remove view after drawing
        rootView.removeView(composeView)

        // Save the bitmap
        saveBitmapToStorage(bitmap, context, onAction)
    }

}
suspend fun captureComposableAsImage(
    composable: @Composable () -> Unit,
    context: Context
): Bitmap {
    val activity = context as? Activity ?: throw IllegalStateException("Context is not an Activity")
    val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)

    val composeView = ComposeView(context).apply {
        setContent { composable() }
        layoutParams = ViewGroup.LayoutParams(1080, 1080)
    }

    rootView.addView(composeView)

    return suspendCoroutine { continuation ->
        composeView.post {
            composeView.measure(
                View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY)
            )
            composeView.layout(0, 0, 1080, 1080)

            val bitmap = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            composeView.draw(canvas)

            rootView.removeView(composeView)
            continuation.resume(bitmap)
        }
    }
}

fun saveBitmapToStorage(bitmap: android.graphics.Bitmap, context: Context,onAction: (Boolean) -> Unit = {}) {
    val filename = "quote_${System.currentTimeMillis()}.png"
    val contentValues = android.content.ContentValues().apply {
        put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(
            android.provider.MediaStore.Images.Media.RELATIVE_PATH,
            android.os.Environment.DIRECTORY_PICTURES + "/PickupLines"
        )
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )

    uri?.let {
        try {
            resolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, outputStream)
                android.widget.Toast.makeText(
                    context,
                    "Image saved to Pictures/PickupLines",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                onAction(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            android.widget.Toast.makeText(
                context,
                "Failed to save image",
                android.widget.Toast.LENGTH_SHORT
            ).show()
            onAction(false)
        }
    }
}

//fun Context.loadImageBitmap(uri: Uri): ImageBitmap {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//        // Use ImageDecoder for API 28 and above
//        val source = ImageDecoder.createSource(contentResolver, uri)
//        ImageDecoder.decodeBitmap(source).asImageBitmap()
//    } else {
//        // Use BitmapFactory for API 27 and below
//        val inputStream = contentResolver.openInputStream(uri)
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//        inputStream?.close()
//        bitmap.asImageBitmap()
//    }
//}


fun Context.loadImageBitmap(uri: Uri, context: Context, radius:Int): ImageBitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(contentResolver, uri)
        var bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE) // Force software rendering
        }
        bitmap = bitmap.applyBlur(context, radius)
        bitmap.asImageBitmap()
    } else {
        val inputStream = contentResolver.openInputStream(uri)
        var bitmap = BitmapFactory.decodeStream(inputStream)?.copy(Bitmap.Config.ARGB_8888, true) // Force software bitmap
        bitmap = bitmap?.applyBlur(context, radius)
        inputStream?.close()

        bitmap!!.asImageBitmap()
    }
}
