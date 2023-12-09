package com.example.myapplication.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.myapplication.theme.KTITheme
import com.example.myapplication.theme.kti_softblack
import dev.icerock.moko.resources.ImageResource

@Composable
fun KTIIcon(
    imageResource: ImageResource,
    modifier: Modifier = Modifier,
    size: Dp = Dp.Unspecified,
    tint: Color = KTITheme.colors.textMain,
    contentDescription: String? = null,
) {
    Icon(
        painter = dev.icerock.moko.resources.compose.painterResource(imageResource),
        contentDescription = contentDescription,
        modifier = modifier then Modifier.size(size),
        tint = tint
    )
}

@Composable
fun KTIIllustration(
    imageResource: ImageResource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Image(
        painter = dev.icerock.moko.resources.compose.painterResource(imageResource),
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@Composable
fun KTIIconButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {
    IconButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        icon.invoke()
    }
}

@Composable
fun KTIIconButton(
    onClick: () -> Unit,
    imageResource: ImageResource,
    size: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    IconButton(onClick = onClick) {
        KTIIcon(
            imageResource = imageResource,
            size = size,
            tint = tint,
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}

@Composable
fun KTIBackIcon() {
    Icon(Icons.Filled.ArrowBack, "Back Icon", tint = kti_softblack)
}

@Composable
fun KTIBackButton() {
    val navigator = LocalNavigator.currentOrThrow
    KTIIconButton(onClick = { navigator.pop() }) {
        KTIBackIcon()
    }
}