package com.example.myapplication

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.example.myapplication.screens.welcome.WelcomeScreen

@Composable
fun MainApp() {
//    var count by remember {
//        mutableStateOf(0)
//    }
//
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Button(onClick = { count++ }) {
//            Text("Count: $count")
//        }
//    }
    Navigator(WelcomeScreen)
}