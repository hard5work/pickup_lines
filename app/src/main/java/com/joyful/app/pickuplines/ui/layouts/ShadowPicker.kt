package com.joyful.app.pickuplines.ui.layouts


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun ShadowPicker(
    color:Color, offX:Float, offY:Float, blurRadius:Float,
    updated: (Color, Float, Float, Float) -> Unit, onClicked: () -> Unit) {
    // Mutable states for each padding value
    var shadowColor by remember { mutableStateOf(color) }
    var offsetX by remember { mutableFloatStateOf(offX) }
    var offsetY by remember { mutableFloatStateOf(offY) }
    var blurRad by remember { mutableFloatStateOf(blurRadius) }
    val lowestPadding by remember {
        mutableFloatStateOf(-100f)
    }
    val highestPadding by remember {
        mutableFloatStateOf(100f)
    }
    var viewColor by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Text Shadow", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,


            ) {
            // Slider for Top Padding
            Column(modifier = Modifier.width(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

                Button(onClick = {
                    viewColor = true
                }) {
                    Text(text = "Select Color")

                }
            }
            Column(modifier = Modifier.width(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {


                // Slider for Bottom Padding
                Text(text = "Blur Radius")
                Slider(
                    value = blurRad,
                    onValueChange = {
                        blurRad = it

                        updated(shadowColor, offsetX, offsetY, blurRad)
                    },
                    valueRange = 0f..highestPadding
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Column(modifier = Modifier.width(150.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                // Slider for Left Padding
                Text(text = "Offset X")
                Slider(
                    value = offsetX,
                    onValueChange = {
                        offsetX = it

                        updated(shadowColor, offsetX, offsetY, blurRad)
                    },
                    valueRange = lowestPadding..highestPadding
                )

            }

            Column(modifier = Modifier.width(150.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                // Slider for Right Padding
                Text(text = "Offset Y")
                Slider(
                    value = offsetY,
                    onValueChange = {
                        offsetY = it
                        updated(shadowColor, offsetX, offsetY, blurRad)
                    },
                    valueRange = lowestPadding..highestPadding
                )
            }
        }



        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { onClicked() }) {
            Row {
                Text(text = "Done")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        if (viewColor){
            ColorPickerDialog(currentColor = shadowColor, onColorChange = {
                shadowColor = Color(it)
                updated(shadowColor, offsetX, offsetY, blurRad)
            }, onDismiss = {
                viewColor = false
            }) {
                viewColor = false
            }
        }
    }
}