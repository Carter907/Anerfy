package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carte.anerfy.model.Difficulty
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.update.QuizRepository
import com.example.carte.anerfy.view.StandardScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateQuizScreen(
    private val quiz: Quiz =

        Quiz(
            name = "",
            description = "",
            difficulty = Difficulty.EASY,
            questions = emptySet()
        )
) :
    Screen {

    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {
        val quizName = remember { mutableStateOf(quiz.name) }
        val quizDescription = remember { mutableStateOf(quiz.description) }
        val quizDifficulty = remember { mutableStateOf(quiz.difficulty) }
        val showDifficultyMenu = remember { mutableStateOf(false) }

        StandardScreen(
            title = "Create a New Quiz!",
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(50.dp)
                ) {

                    SubmitButton {
                        GlobalScope.launch {
                            submitQuiz(
                                quizName.value,
                                quizDescription.value,
                                quizDifficulty.value,
                            )
                        }


                        screenShowing.value = QuizzesScreen();
                    }


                }

            }) {

            FormContent(
                quizName,
                quizDescription,
                quizDifficulty,
                showDifficultyMenu,
            )


        }
    }

    private fun submitQuiz(quizName: String, quizDescription: String, quizDifficulty: Difficulty) {
        QuizRepository.withTransaction {

            quiz.name = quizName
            quiz.description = quizDescription
            quiz.difficulty = quizDifficulty

            if (quiz.id != null && find(Quiz::class.java, quiz.id) is Quiz) {

                merge(quiz);

            } else {
                persist(quiz)
            }


        }

    }

    @Composable
    fun FormContent(
        quizName: MutableState<String>,
        quizDescription: MutableState<String>,
        quizDifficulty: MutableState<Difficulty>,
        showDifficultyMenu: MutableState<Boolean>
    ) {
        val formWidth = 300.dp

        Column(
            modifier = Modifier.width(formWidth).height(300.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {

            OutlinedTextField(
                quizName.value,
                { quizName.value = it },
                modifier = Modifier.width(formWidth),
                placeholder = { Text("enter in a quiz name") })
            OutlinedTextField(
                quizDescription.value,
                { quizDescription.value = it },
                modifier = Modifier.height(150.dp).width(formWidth),
                placeholder = { Text("enter in a quiz description") })
            DifficultyOptions(quizDifficulty, showDifficultyMenu)

        }
    }


    @Composable
    fun SubmitButton(onSubmit: () -> Unit) {
        Button(onClick = {

            onSubmit.invoke();

        }) {
            Text("Submit Quiz");
        }
    }


    @Composable
    fun DifficultyOptions(difficulty: MutableState<Difficulty>, showDifficultyMenu: MutableState<Boolean>) {

        TextButton(
            onClick = {
                showDifficultyMenu.value = true
            }
        ) {
            Text("choose difficulty: ${difficulty.value}")
            DropdownMenu(showDifficultyMenu.value, { showDifficultyMenu.value = false }) {
                for (diff in Difficulty.values()) {
                    DropdownMenuItem(
                        onClick = {
                            difficulty.value = diff
                            showDifficultyMenu.value = false;
                        }
                    ) {
                        Text(diff.name.lowercase())
                    }
                }

            }
        }
    }

}