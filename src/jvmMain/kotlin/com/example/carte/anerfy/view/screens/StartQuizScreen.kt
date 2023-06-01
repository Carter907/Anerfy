package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carte.anerfy.model.Question
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.view.RowPair
import com.example.carte.anerfy.view.StandardScreen

class StartQuizScreen(private val quiz: Quiz) : Screen {
    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {
        val quizIterator = quiz.questions.iterator()
        var score by remember { mutableStateOf(0) }
        var questionNumber by remember { mutableStateOf(0) }
        var currentQuestion by remember {
            mutableStateOf(
                if (quizIterator.hasNext()) {
                    questionNumber = 1;
                    quizIterator.next()
                } else {
                    error("no question")
                }

            )
        }


        StandardScreen(
            title = quiz.name,
            bottomBar = {
                RowPair(
                    composOne = {
                        NextQuestionButton {
                            if (quizIterator.hasNext()) {
                                questionNumber++;
                                currentQuestion = quizIterator.next();
                            }
                        }
                    },
                    composTwo = {
                        ExitQuizButton {

                            screenShowing.value = QuizzesScreen()
                        }
                    }
                )

            }

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                Text("#$questionNumber")
                Spacer(modifier = Modifier.width(100.dp))
                QuestionContent(currentQuestion)

            }

        }


    }

    @Composable
    fun ExitQuizButton(onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text("Exit Quiz");
        }
    }

    @Composable
    fun QuestionContent(question: Question) {
        Text(question.content)
    }

    @Composable
    fun NextQuestionButton(onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text("Next Question")
        }
    }
}