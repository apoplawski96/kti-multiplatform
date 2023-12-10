@file:OptIn(ExperimentalResourceApi::class)

package com.example.myapplication.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appkickstarter.shared.SharedRes
import com.example.myapplication.theme.kti_dark_grey
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun KTITopAppBar(
    title: String? = null,
    iconsSection: @Composable () -> Unit = { TopBarIconsSection() },
    isNested: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        LeftSection(title = title, isNested = isNested)
        Row(verticalAlignment = Alignment.CenterVertically) {
            iconsSection.invoke()
        }
    }
}

@Composable
fun KTIChatTopAppBar() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing))
    )
    val rainbowColorsBrush = Brush.horizontalGradient(
        listOf(
            Color.Red,
            Color.Magenta,
            Color.Blue,
            Color.Cyan,
            Color.Green,
            Color.Yellow,
        )
    )
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        KTIBackButton()
        Image(
            painter = dev.icerock.moko.resources.compose.painterResource(SharedRes.images.avatar),
            contentDescription = "",
            modifier = Modifier.clip(CircleShape).size(44.dp).drawBehind {
                rotate(rotationAnimation.value) {
                    drawCircle(rainbowColorsBrush, style = Stroke(4f))
                }
            }
        )
        KTIHorizontalSpacer(16.dp)
        Column {
            KTITextNew("Mr interviewer", fontSize = 16.sp, fontWeight = FontWeight.W500)
            KTITextNew("Online", fontSize = 12.sp, color = kti_dark_grey)
        }
    }
}

@Composable
private fun RowScope.LeftSection(
    isNested: Boolean,
    title: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isNested) KTIBackButton()
        KTIHorizontalSpacer(width = 8.dp)
        title?.let { KTITextNew(text = title, fontSize = 18.sp, fontWeight = FontWeight.W500) }
    }
}

@Composable
private fun TopBarIconsSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        KTIIconButton(
            onClick = {
//                showNotYetImplementedToast(context)
            }
        ) {
            KTIIcon(imageResource = SharedRes.images.ic_book, size = 24.dp)
        }
        KTIIconButton(
            onClick = {
//                showNotYetImplementedToast(context)
            }
        ) {
            KTIIcon(imageResource = SharedRes.images.ic_user, size = 24.dp)
        }
        KTIIconButton(
            onClick = {
//                showNotYetImplementedToast(context)
            }
        ) {
            KTIIcon(imageResource = SharedRes.images.ic_menu, size = 24.dp)
        }
    }
}
