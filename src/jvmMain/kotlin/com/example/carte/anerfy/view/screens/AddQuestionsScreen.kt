package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            title = "Add a Question to your Quiz!",
            bottomBar = {

                AddQuestionButton();

            }

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


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun AddQuestionButton() {

        var questionAlertDialogShowing = remember { mutableStateOf(false) }
        Button(onClick = {
            questionAlertDialogShowing.value = true;
        }) {
            Text("Add Question")

            if (questionAlertDialogShowing.value)
                AlertDialog(

                    onDismissRequest = { questionAlertDialogShowing.value = false },
                    buttons = {
                              Button(onClick = {questionAlertDialogShowing.value = false}) {
                                  Text("OK")

                              }

                    },
                    text = {
                        Text("Question has been added");
                    }

                )
        }

    }
}