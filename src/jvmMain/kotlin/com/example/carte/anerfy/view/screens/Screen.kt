package com.example.carte.anerfy.view.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

sealed interface Screen {

    @Composable
    fun Screen(screenShowing: MutableState<Screen>);
}

