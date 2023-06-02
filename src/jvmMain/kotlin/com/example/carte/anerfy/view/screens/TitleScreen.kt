package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.carte.anerfy.view.StandardScreen

class TitleScreen : Screen {

    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {
        StandardScreen(title = "Welcome to Anerfy", modifier = Modifier) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                Button(
                    content = { Text("next") },
                    onClick = {
                        screenShowing.value = QuizzesScreen();
                    }
                )
            }
        }
    }
}