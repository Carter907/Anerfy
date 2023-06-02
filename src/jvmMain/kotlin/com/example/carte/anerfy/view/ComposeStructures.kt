package com.example.carte.anerfy.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StandardScreen(
    modifier: Modifier = Modifier.padding(20.dp),
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

@Composable
fun BackButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Back")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BasicAlert(text: String, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest,
        buttons = {
            Button(onClick = {
                onDismissRequest.invoke()
            }, modifier = Modifier.padding(5.dp)) {
                Text("Okay")
            }
        },
        text = { Text(text) },
        modifier = Modifier.width(200.dp)
    )
}

@Composable
fun RowPair(composOne: @Composable () -> Unit, composTwo: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        composOne.invoke()
        composTwo.invoke()




    }

}
