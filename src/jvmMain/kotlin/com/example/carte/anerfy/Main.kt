package com.example.carte.anerfy

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.carte.anerfy.view.screens.Screen
import com.example.carte.anerfy.view.screens.TitleScreen


@Composable
@Preview
fun App() {

    val screen  = remember { mutableStateOf<Screen>(TitleScreen()) }
    MaterialTheme {

        screen.value.Screen(screen);
    }
}


fun main() {


    application {
        Window(onCloseRequest = ::exitApplication, title = "Anerfy") {
            App()
        }
    }
}
