package com.lduboscq.appkickstarter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.myapplication.android.theme.AppThemeMode
import com.example.myapplication.screens.welcome.WelcomeScreen
import com.example.myapplication.theme.KTITheme
import com.lduboscq.appkickstarter.list.ListScreenContent
import com.lduboscq.appkickstarter.list.PersonsListScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun MainApp() {
    KTITheme(themeMode = AppThemeMode.LIGHT) {
        Navigator(WelcomeScreen) { navigator ->
            SlideTransition(navigator)
        }
    }
}
