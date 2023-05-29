package com.example.carte.anerfy.view.animate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun LoadingVisibilityAnimation(visible: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) { // this: AnimatedVisibilityScope
        // Use AnimatedVisibilityScope#transition to add a custom animation
        // to the AnimatedVisibility.
        LoadingAnimation()

    }

}


@Composable
fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Icon(
        Icons.Rounded.Refresh,
        "loading icon",
        modifier = Modifier.rotate(rotation)
    );


}
