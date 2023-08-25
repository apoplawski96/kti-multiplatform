package com.example.myapplication.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appkickstarter.shared.SharedRes

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
