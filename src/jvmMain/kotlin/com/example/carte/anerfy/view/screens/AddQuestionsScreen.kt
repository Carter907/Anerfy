package com.example.carte.anerfy.view.screens

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.example.carte.anerfy.model.Difficulty
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.view.StandardScreen

class AddQuestionsScreen(
    private val quiz: Quiz =
        Quiz(
            name = "",
            description = "",
            difficulty = Difficulty.EASY,
            questions = emptySet()
        )
) : Screen {
    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {
        QuestionForm();
    }

    @Composable
    fun QuestionForm() {
        StandardScreen(
            title = "Add a Question to your Quiz!"

        ) {
            // get the question itself

            var questionContent = remember { mutableStateOf("") }

            OutlinedTextField(
                questionContent.value,
                { questionContent.value = it },
                placeholder = { Text("please enter the question") }
            )

            // get the answer to the question or any false answers

            var falseQuestions = remember { mutableStateListOf<String>() }

            OutlinedTextField(
                questionContent.value,
                { questionContent.value = it },
                placeholder = { Text("please enter the question") }
            )

        }

    }
}