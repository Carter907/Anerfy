package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.view.BackButton

class QuizResultScreen(private val quiz: Quiz, private val score: Float = 0f) : Screen {

    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                "score: ${(quiz.questions.size * score).toInt()}/${quiz.questions.size}",
                fontSize = 40.sp
            )
            BackButton {
                screenShowing.value = QuizzesScreen();
            }
            RetryButton {
                screenShowing.value = StartQuizScreen(quiz);
            }
        }

    }


    @Composable
    fun RetryButton(
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick
        ) {
            Text("Retry Quiz")
        }
    }
}