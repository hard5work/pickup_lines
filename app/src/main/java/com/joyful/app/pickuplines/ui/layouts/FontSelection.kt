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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.provider.FontsContractCompat
import com.joyful.app.pickuplines.R
import com.joyful.app.pickuplines.TextDownloadableFontsSnippet1.provider
import com.xdroid.app.service.utils.helper.DebugMode
import org.koin.core.component.getScopeName


@Composable
fun FontSelectionScreen(onFontSelected: (FontFamily) -> Unit, onClicked: () -> Unit) {

    // Define GoogleFont instances for different fonts
    val fonts = listOf(
        GoogleFont("Lobster Two"),
        GoogleFont("Roboto"),
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
        GoogleFont("Alegreya"),
        GoogleFont("Bebas Neue"),
        GoogleFont("Bungee"),
        GoogleFont("Cairo"),
        GoogleFont("Caveat"),
        GoogleFont("Chivo"),
        GoogleFont("Comfortaa"),
        GoogleFont("Concert One"),
        GoogleFont("Dancing Script"),
        GoogleFont("Domine"),
        GoogleFont("Exo"),
        GoogleFont("Fjalla One"),
        GoogleFont("Fredoka One"),
        GoogleFont("Heebo"),
        GoogleFont("Hind"),
        GoogleFont("Josefin Sans"),
        GoogleFont("Kalam"),
        GoogleFont("Kanit"),
        GoogleFont("Karma"),
        GoogleFont("Mali"),
        GoogleFont("Manrope"),
        GoogleFont("Maven Pro"),
        GoogleFont("Merriweather Sans"),
        GoogleFont("Notable"),
        GoogleFont("Overpass"),
        GoogleFont("Philosopher"),
        GoogleFont("Rajdhani"),
        GoogleFont("Secular One"),
        GoogleFont("Sora"),
        GoogleFont("Teko"),
        GoogleFont("Varela Round"),
        GoogleFont("Vollkorn"),
        GoogleFont("Zilla Slab"),
//        GoogleFont(""),


        )

    // Create FontFamily list based on GoogleFonts
    val fontFamilies = fonts.map { font ->
        FontFamily(Font(googleFont = font, fontProvider = provider))
    }

    // Initial selected font family
    var selectedFontFamily by remember { mutableStateOf(fontFamilies.first()) }
    // Obtain Typeface from the selected FontFamily


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
            itemsIndexed(fontFamilies) { index, fontFamily ->
                // Render each font as a selectable item

                val fontName = fonts[index]
                val isSelected = fontFamily == selectedFontFamily
//                Text(
//                    text = fontFamily.getScopeName().value,
//                    style = TextStyle(fontFamily = fontFamily, fontSize = 18.sp),
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp)
//                        .clickable {
//                            onFontSelected(fontFamily)
//
//
//                        } // Update selected font on click
//                )
                Text(
                    text = fontName.name,
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.Blue else Color.Black
                    ),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(if (isSelected) Color.LightGray else Color.Transparent, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                        .clickable {
                            selectedFontFamily = fontFamily
                            onFontSelected(fontFamily)
                        }
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

// Method to load Typeface from Compose FontFamily
//fun loadTypeface(context: Context, font: GoogleFont): android.graphics.Typeface? {
//
//
//    val fontRequest = GoogleFont.FontRequest(
//        provider = provider,
//        fontName = font.name
//    )
//
//    return try {
//        val family = ResourcesCompat.getFont(context, fontRequest)
//        family
//    } catch (e: Exception) {
//        e.printStackTrace()
//        null
//    }
//}

