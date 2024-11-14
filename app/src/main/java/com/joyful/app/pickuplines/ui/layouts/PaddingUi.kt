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
import androidx.compose.ui.unit.sp


@Composable
fun PaddingAdjusterDemo(
    top:Float, bottom:Float, left:Float, right:Float,
    padding: (Float, Float, Float, Float) -> Unit, onClicked: () -> Unit) {
    // Mutable states for each padding value
    var topPadding by remember { mutableFloatStateOf(top) }
    var bottomPadding by remember { mutableFloatStateOf(bottom) }
    var leftPadding by remember { mutableFloatStateOf(left) }
    var rightPadding by remember { mutableFloatStateOf(right) }
    val lowestPadding by remember {
        mutableFloatStateOf(0f)
    }
    val highestPadding by remember {
        mutableFloatStateOf(100f)
    }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Padding", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,


        ) {
            // Slider for Top Padding
            Column(modifier = Modifier.width(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = "Top")
                Slider(
                    value = topPadding,
                    onValueChange = {
                        topPadding = it
                        padding(topPadding, bottomPadding, leftPadding, rightPadding)
                    },
                    valueRange = lowestPadding..highestPadding,

                    )
            }
            Column(modifier = Modifier.width(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {


                // Slider for Bottom Padding
                Text(text = "Bottom")
                Slider(
                    value = bottomPadding,
                    onValueChange = {
                        bottomPadding = it

                        padding(topPadding, bottomPadding, leftPadding, rightPadding)
                    },
                    valueRange = lowestPadding..highestPadding
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
                Text(text = "Left")
                Slider(
                    value = leftPadding,
                    onValueChange = {
                        leftPadding = it

                        padding(topPadding, bottomPadding, leftPadding, rightPadding)
                    },
                    valueRange = lowestPadding..highestPadding
                )

            }

            Column(modifier = Modifier.width(150.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                // Slider for Right Padding
                Text(text = "Right")
                Slider(
                    value = rightPadding,
                    onValueChange = {
                        rightPadding = it
                        padding(topPadding, bottomPadding, leftPadding, rightPadding)
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
    }
}