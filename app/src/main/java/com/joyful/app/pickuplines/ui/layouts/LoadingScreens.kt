package com.joyful.app.pickuplines.ui.layouts

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.joyful.app.pickuplines.R


@Composable
fun LoadingContent() {
    // Get the screen width
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Assuming a fixed item width, adjust the number of columns based on screen width
    val itemWidth = 120.dp // Set the desired width for each item
    val count = (screenWidth / itemWidth).toInt()
    Column {
        LazyVerticalGrid(columns = GridCells.Fixed(count),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(count = 10){
                SingleShimmer()
            }
        }




    }
}


@Composable
fun LoadingContentColumn() {
    // Get the screen width
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Assuming a fixed item width, adjust the number of columns based on screen width
    val itemWidth = 120.dp // Set the desired width for each item
    val count = (screenWidth / itemWidth).toInt()
    Column {
        LazyVerticalGrid(columns = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(count = 10){
                ShimmerPlaceholderSingle()
            }
        }




    }
}

@Composable
fun SingleShimmer() {
    ShimmerPlaceholder()
}


@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
    shimmerWidth: Float = 200f
) {
    // Define animation to shift the gradient position
    val transition = rememberInfiniteTransition(label = "Looping")
    val shimmerTranslate by transition.animateFloat(
        initialValue = -shimmerWidth,
        targetValue = shimmerWidth * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = EaseInOut),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    // Define the shimmer brush with a gradient
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 1f),
            Color.White.copy(alpha = 0.5f),
            Color.LightGray.copy(alpha = 1f)
        ),
        //TOPLEFTCORNER TO BOTTOMRIGHTCORNER
        start =  Offset(x = shimmerTranslate, y = shimmerTranslate),
        end = Offset(x = shimmerTranslate + shimmerWidth, y = shimmerTranslate + shimmerWidth)

        //Straight Linear
//        start = Offset(x = shimmerTranslate, y = 0f),
//        end = Offset(x = shimmerTranslate + shimmerWidth, y = 0f)
    )

    // Use Box with shimmer effect background
    Box(
        modifier = modifier
            .background(shimmerBrush)
            .fillMaxSize()
    )
}


@Composable
fun ShimmerPlaceholder(
    modifier: Modifier = Modifier
) {
    // Use shimmer effect on placeholder items
    Column(
        modifier = modifier
            .padding(16.dp)
            .width(350.dp)
    ) {
        ShimmerEffect(modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
        )

    }
}



@Composable
fun SingleAdShimmer(
    modifier: Modifier = Modifier
) {
    // Use shimmer effect on placeholder items
    Column(
        modifier = modifier
            .padding(16.dp)
            .width(320.dp)
    ) {
        ShimmerEffect(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
        )

    }
}



@Composable
fun ShimmerPlaceholderSingle(
    modifier: Modifier = Modifier
) {
    // Use shimmer effect on placeholder items
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        ShimmerEffect(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()

        )

    }
}

@Composable
fun CheesyLoader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cheesy))
    val progress by animateLottieCompositionAsState(composition,
        iterations = LottieConstants.IterateForever)
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}


@Composable
fun LottieAnimationFromUrl(url:String) {
    // Replace this with the URL of your Lottie JSON file
//    val animationUrl = "https://assets9.lottiefiles.com/packages/lf20_jcikwtux.json"
    val animationUrl = url
//    https://lottie.host/746d2751-3cfa-4027-8e2a-17d14dac7bb1/3fEFNPyz6z.json

    // Load the Lottie composition from the URL
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(animationUrl))

    // Manage the animation progress
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // Display the animation
    LottieAnimation(
        modifier = Modifier.size(70.dp,70.dp),
        composition = composition,
        progress = progress,// Modify as needed
    )
}