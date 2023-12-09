package com.example.myapplication.screens.interviewSetup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.appkickstarter.shared.SharedRes
import com.example.myapplication.compose.GridVariant
import com.example.myapplication.compose.KTIBoxWithGradientBackground
import com.example.myapplication.compose.KTICardItem
import com.example.myapplication.compose.KTICircularProgressIndicator
import com.example.myapplication.compose.KTIGridWithCards
import com.example.myapplication.compose.KTIIllustration
import com.example.myapplication.compose.KTITopAppBar
import com.example.myapplication.compose.KTIVerticalSpacer
import com.example.myapplication.di.getScreenModel
import com.example.myapplication.model.Role
import com.example.myapplication.model.TopCategory
import com.example.myapplication.screens.categories.CategoriesScreenModel
import com.example.myapplication.screens.interviewAi.AIInterviewScreenModel

internal class InterviewSetupScreen(val role: Role) : Screen {

    @Composable
    override fun Content() {
        val viewModel: AIInterviewScreenModel = getScreenModel()

        val viewState = viewModel.viewState.collectAsState().value
        val isLoading = viewModel.isLoading.collectAsState().value

        LaunchedEffect(null) {
            viewModel.initialize(role)
        }

    }
}

@Composable
private fun InterviewSetupScreenContent(
    state: CategoriesScreenModel.ViewState,
    onCategoryClick: (TopCategory?) -> Unit,
    lazyGridState: LazyGridState
) {
    KTIBoxWithGradientBackground {
        when (state) {
            is CategoriesScreenModel.ViewState.CategoriesLoaded -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KTITopAppBar(title = "Categories")
                    KTIGridWithCards(
                        items = state.categories.map { topCategory ->
                            KTICardItem(
                                value = topCategory,
                                label = topCategory.displayName
                            )
                        },
                        onClick = onCategoryClick,
                        state = lazyGridState,
                        variant = GridVariant.TOP_CATEGORY,
                    )
                    KTIVerticalSpacer(height = 64.dp)
                    KTIIllustration(imageResource = SharedRes.images.undraw_scientist_ft0o)
                }
            }

            is CategoriesScreenModel.ViewState.Loading -> {
                KTICircularProgressIndicator()
            }
        }
    }
}