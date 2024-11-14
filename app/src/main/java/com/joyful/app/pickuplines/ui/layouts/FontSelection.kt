package com.joyful.app.pickuplines.ui.layouts

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.provider.FontsContractCompat
import com.joyful.app.pickuplines.R
import com.joyful.app.pickuplines.TextDownloadableFontsSnippet1.provider


@Composable
fun FontSelectionScreen(onFontSelected: (FontFamily) -> Unit, onClicked: () -> Unit) {

    // Define GoogleFont instances for different fonts
    val fonts = listOf(
        GoogleFont("Roboto"),
        GoogleFont("Lobster Two"),
        GoogleFont("Lora"),
        GoogleFont("Open Sans"),
        GoogleFont("Pacifico"),
        GoogleFont("Playfair Display"),
        GoogleFont("Montserrat"),
        GoogleFont("Poppins"),
        GoogleFont("Merriweather"),
        GoogleFont("Nunito"),
        GoogleFont("Raleway"),
        GoogleFont("Source Sans Pro"),
        GoogleFont("Lato"),
        GoogleFont("PT Sans"),
        GoogleFont("Ubuntu"),
        GoogleFont("Oswald"),
        GoogleFont("Roboto Condensed"),
        GoogleFont("Quicksand"),
        GoogleFont("Inter"),
        GoogleFont("Rubik"),
        GoogleFont("Noto Sans"),
        GoogleFont("Titillium Web"),
        GoogleFont("Fira Sans"),
        GoogleFont("Inconsolata"),
        GoogleFont("Work Sans"),
        GoogleFont("Cabin"),
        GoogleFont("Mukta"),
        GoogleFont("Bitter"),
        GoogleFont("Space Grotesk"),
        GoogleFont("Libre Baskerville"),
        GoogleFont("Mulish"),
        GoogleFont("Arimo"),
        GoogleFont("Space Mono"),


        )

    // Create FontFamily list based on GoogleFonts
    val fontFamilies = fonts.map { font ->
        FontFamily(Font(googleFont = font, fontProvider = provider))
    }

    // Initial selected font family
    val selectedFontFamily by remember { mutableStateOf(fontFamilies.first()) }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        // Font Preview
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Text Font Style",
            style = TextStyle(fontFamily = selectedFontFamily, fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Horizontal list of fonts
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(fontFamilies) { fontFamily ->
                // Render each font as a selectable item
                Text(
                    text = "Sample Text",
                    style = TextStyle(fontFamily = fontFamily, fontSize = 18.sp),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onFontSelected(fontFamily)
                        } // Update selected font on click
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
