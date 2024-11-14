package com.joyful.app.pickuplines.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.joyful.app.pickuplines.ui.components.ButtonComponent
import com.joyful.app.pickuplines.ui.components.OutlineButtonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ErrorSnackBar(
    message: String?,
    showDialog: Boolean, onDismiss: (Boolean) -> Unit
) {
    if (showDialog)
        Snackbar(
            action = {
                TextButton(onClick = { onDismiss(false) }) {
                    Text("Dismiss")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(message ?: "")
        }
}

@Composable
fun AlertDialogs(message: String, onButtonClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "Alert") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = { onButtonClick() }) {
                Text("OK")

            }
        }
    )
}

@Composable
fun AlertDialogs(
    title: String = "Alert",
    message: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onButtonClick: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = { onButtonClick() }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Composable
fun ErrorSnackbar(
    message: String?,
    durationMillis: Long = 3000
) {
    val snackbarVisibleState = remember { mutableStateOf(message != null) }
    val coroutineScope = rememberCoroutineScope()

    if (message != null) {
        LaunchedEffect(message) {
            coroutineScope.launch {
                delay(durationMillis)
                snackbarVisibleState.value = false
            }
        }
    }

    if (snackbarVisibleState.value) {
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Red),
            contentColor = Color.White
        ) {
            Text(message ?: "")
        }
    }
}


@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    image: Painter,
    confirmButtonText: String = "Ok",
    dismissButtonText: String = "Cancel",
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },

        containerColor = Color.Black,
        properties = DialogProperties(dismissOnClickOutside = true),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                )
                /* Spacer(modifier = Modifier.width(16.dp))
             Text(
                 text = title,
                 fontSize = 20.sp,
                 fontWeight = FontWeight.Bold
             )*/
            }
        },
        text = {
            Text(
                text = message,
            )
        },
        confirmButton = {
            Column() {

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlineButtonComponent(
                        label = dismissButtonText,
                        onClick = { onDismissButtonClick() },
                        width = 120

                    )
                    ButtonComponent(
                        label = confirmButtonText,
                        onClick = { onConfirmButtonClick() },
                        width = 120

                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

            }

        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .wrapContentWidth(align = Alignment.CenterHorizontally)


    )
}

@Composable
fun InfoAlertDialog(
    message: String,
    confirmButtonText: String = "Ok",
    onConfirmButtonClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnClickOutside = true),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "App Info",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Text(
                text = message
            )
        },
        confirmButton = {
            Column() {

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ButtonComponent(
                        label = confirmButtonText,
                        onClick = { onConfirmButtonClick() },
                        width = 120

                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

            }

        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .wrapContentWidth(align = Alignment.CenterHorizontally)


    )
}

@Composable
fun LoadingAlertDialog(
) {
    AlertDialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnClickOutside = true),

        confirmButton = {
            Column() {

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }

                Spacer(modifier = Modifier.height(10.dp))

            }

        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .wrapContentWidth(align = Alignment.CenterHorizontally)


    )
}

