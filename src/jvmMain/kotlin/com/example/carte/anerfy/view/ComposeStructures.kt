package com.example.carte.anerfy.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StandardScreen(modifier: Modifier = Modifier.padding(20.dp),
                   title: String = "title",
                   topBar: @Composable () -> Unit = {
                       Row(
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.Center,
                           modifier = Modifier.fillMaxWidth().height(50.dp),
                           content = {
                               Text(title);
                           }
                       )
                   },
                   bottomBar: @Composable () -> Unit = {},
                   content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        content = content
    )
}
