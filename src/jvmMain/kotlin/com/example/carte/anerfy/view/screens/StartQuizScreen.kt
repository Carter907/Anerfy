package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.carte.anerfy.model.Question
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.view.RowPair
import com.example.carte.anerfy.view.StandardScreen

class StartQuizScreen(private val quiz: Quiz) : Screen {
    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {
        val quizIterator = quiz.questions.iterator()
        var answeredCorrectly by remember { mutableStateOf(0) }
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
        var answerSelected by remember { mutableStateOf("") }
        var isAnswerSelected by remember { mutableStateOf(false) }


        StandardScreen(
            title = quiz.name,
            bottomBar = {
                RowPair(
                    composOne = {
                        ExitQuizButton {

                            screenShowing.value = QuizzesScreen()
                        }

                    },
                    composTwo = {
                        NextQuestionButton {
                            if (answerSelected == currentQuestion.answer)
                                answeredCorrectly++;

                            if (quizIterator.hasNext()) {
                                questionNumber++;

                                currentQuestion = quizIterator.next();
                            } else {
                                screenShowing.value =
                                    QuizResultScreen(quiz, answeredCorrectly / questionNumber.toFloat())
                            }
                        }
                    }
                )

            }

        ) {
            Column {

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
                AnswerChoices(
                    question = currentQuestion,
                    onAnswerSelected = {

                        answerSelected = it;
                        isAnswerSelected = true;
                    },
                    answerSelected = answerSelected
                );

            }


        }


    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun AnswerChoices(question: Question, onAnswerSelected: (answer: String) -> Unit, answerSelected: String) {
        Column {
            for (possibleAnswer in question.possibleAnswers) {
                val isSelectedAnswer = possibleAnswer == answerSelected;
                ListItem(
                    modifier = if (isSelectedAnswer) {
                        Modifier.border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(5.dp))
                    } else {
                        Modifier
                    }.selectable(selected = isSelectedAnswer) {
                        onAnswerSelected.invoke(possibleAnswer);
                    }
                ) {
                    Text(possibleAnswer)
                }
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