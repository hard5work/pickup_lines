package com.joyful.app.pickuplines.ui.layouts

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil3.compose.rememberAsyncImagePainter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


@Composable
fun ImagePickerAndDownloader() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var message by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val context = LocalContext.current
    val contentResolver: ContentResolver = LocalContext.current.contentResolver

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
                    data.data.let {
                        val inputStream = context.contentResolver.openInputStream(it!!)
                        bitmap = BitmapFactory.decodeStream(inputStream)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            // Check for permissions
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

        }) {
            Text("Select Image from Gallery")
        }

        Spacer(modifier = Modifier.height(16.dp))



        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            imageUri?.let { uri ->
                // Start a coroutine to download the image
//                downloadImage(uri.toString(), File(context.getExternalFilesDir(null), "downloaded_image.jpg"))
                copyImageToFile(uri = uri,
                    outputFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "downloaded_image.jpg"),
                    contentResolver = contentResolver)
                message = "Download successful!"
            } ?: run {
                message = "Please select an image first."
            }
        }) {
            Text("Download Image")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message)
    }
}

fun downloadImage(uri: String, outputFile: File) {
    val client = OkHttpClient()
    val request = Request.Builder().url(uri).build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        FileOutputStream(outputFile).use { outputStream ->
            outputStream.write(response.body?.bytes())
        }
    }
}


fun copyImageToFile(uri: Uri, outputFile: File, contentResolver: ContentResolver) {
    var inputStream: InputStream? = null
    var outputStream: FileOutputStream? = null
    try {
        inputStream = contentResolver.openInputStream(uri)
        outputStream = FileOutputStream(outputFile)

        inputStream?.copyTo(outputStream)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}