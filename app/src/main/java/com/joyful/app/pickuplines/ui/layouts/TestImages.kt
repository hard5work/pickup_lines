package com.joyful.app.pickuplines.ui.layouts

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.applyCanvas
import coil.request.ImageRequest
import coil3.compose.AsyncImage
import com.joyful.app.pickuplines.MainActivity
import com.joyful.app.pickuplines.ui.layouts.lists.saveImageToExternalStorage
import com.joyful.app.pickuplines.ui.layouts.lists.saveImageToMediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Composable
fun SaveImageFromLocalExample() {
    // Replace with the path to your local image
    val imagePath = "/storage/emulated/0/Pictures/1731408019046.jpg" // Update this with your actual image path
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showToast by remember { mutableStateOf(false) }

    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Load the image from the local file
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(File(imagePath)) // Load the image from local storage
                .build(),
            contentDescription = "Image",
            modifier = Modifier.size(200.dp),
            onSuccess = { result ->
                // Capture the bitmap from the loaded image
                bitmap = (result as BitmapDrawable).bitmap
            },
            onError = {
                Log.e("asda","sdjkasd ajd asdjadka sdeerrorrrororororororo ${it.result}")
                // Handle error if needed
//                Toast.makeText(LocalContext.current, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Check for write permissions
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bitmap?.let {  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {


                    // Save the image using MediaStore API
//                    saveImageToMediaStore(context, it)
                } else {
//                    saveImageToExternalStorage(context,it)
                } }
                showToast = true
            } else {
                // Request permission
                ActivityCompat.requestPermissions(
                    (context as MainActivity),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }) {
            Text("Save Image")
        }

        if (showToast) {
            Toast.makeText(LocalContext.current, "Image saved!", Toast.LENGTH_SHORT).show()
            showToast = false
        }
    }
}

private fun saveImageToGallery(bitmap: Bitmap) {
    try {
        val directory = File("/storage/emulated/0/Pictures") // Define your directory
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "saved_image.png") // Change the filename as needed
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        // Handle the error
    }
}


@Composable
fun CanvasSaveExample() {
    val context = LocalContext.current

    // This ImageBitmap will store our canvas content.
    val bitmapWidth = 1000
    val bitmapHeight = 1000
    var canvasBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val bitmap = ImageBitmap(bitmapWidth, bitmapHeight)
    canvasBitmap = bitmap

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display Canvas
        Canvas(
            modifier = Modifier
                .size(500.dp)
                .background(Color.Black),
            onDraw = {
//                drawCircle(
//                    color = Color.Blue,
//                    radius = size.minDimension / 3,
//                    center = Offset(size.width / 2, size.height / 2)
//                )
                // More drawing operations can be added here
                // Draw centered text
                val text = "Hello, Canvas!"
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 40f
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                    typeface = android.graphics.Typeface.create(FontFamily.SansSerif.name, android.graphics.Typeface.BOLD)
                }

                // Calculate position for centered text
                val x = size.width / 2
                val y = size.height / 2 - (paint.descent() + paint.ascent()) / 2

                drawContext.canvas.nativeCanvas.drawText(text, x, y, paint)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(onClick = {
//            captureBitmapFromView("Hello, Canvas")
        }) {
            Text(text = "Save Canvas to Device")
        }
    }
}


// Function to save the canvas to file
fun saveCanvasToFile(context: android.content.Context, bitmap: ImageBitmap) {
    val bmp = bitmap.asAndroidBitmap()
//    saveImageToExternalStorage(context, bmp)

}