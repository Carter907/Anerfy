package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carte.anerfy.model.Question
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.update.QuestionRepository
import com.example.carte.anerfy.update.QuizRepository
import com.example.carte.anerfy.view.BackButton
import com.example.carte.anerfy.view.StandardScreen

class EditQuestionsScreen(private val quiz: Quiz) : Screen {


    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {

        val questions = remember { mutableStateListOf(
            *QuizRepository.withTransaction {
                createQuery("select q.questions from Quiz q where q.id = ${quiz.id}").resultList.map { it as Question }
            }.toTypedArray()
        ) }

        StandardScreen(
            title = "Edit Questions in Quiz",
            bottomBar = {
                BackButton {
                    screenShowing.value = QuizzesScreen();
                }
            }

        ) {

            Column(
                modifier = Modifier.height(400.dp).verticalScroll(rememberScrollState(0))
            ) {
                for (question in questions) {
                    Surface(
                        elevation = 10.dp,
                        modifier = Modifier.padding(0.dp, 10.dp)
                    ) {

                        QuestionHolder(
                            question = question,
                            modifier = Modifier.requiredWidth(300.dp),
                            onRemove = {

                                QuizRepository.withTransaction {
                                    val newQuestions = mutableSetOf<Question>();
                                    val questionIterator = quiz.questions.iterator();
                                    questionIterator.forEach {
                                        if (it.id != question.id) {
                                            newQuestions.add(it)
                                        }

                                    }
                                    quiz.questions = newQuestions;

                                    merge(quiz);

                                }

                                questions.remove(question)
                            },
                            onEdit = {
                                screenShowing.value = AddQuestionScreen(quiz = quiz, question = question);


                            }

                        )


                    }
                }

                AddQuestionButton {
                    screenShowing.value = AddQuestionScreen(quiz = quiz)
                }

            }


        }
    }

    @Composable
    fun AddQuestionButton(onAddQuestion: () -> Unit) {
        Button(onClick = {
            onAddQuestion.invoke()
        }) {
            Icon(Icons.Rounded.Add, contentDescription = "Add Question Button")
        }
    }

    @Composable
    fun EditQuestionOption(
        onEdit: () -> Unit
    ) {
        DropdownMenuItem(
            onClick = {
                onEdit.invoke();
            }
        ) {
            Text("edit")
        }
    }

    @Composable
    fun RemoveQuestionOption(onRemove: () -> Unit) {
        DropdownMenuItem(
            onClick = {
                onRemove.invoke();
            }
        ) {
            Text("remove")
        }
    }

    @Composable
    fun QuestionHolder(
        question: Question,
        modifier: Modifier = Modifier,
        onClick: Question.() -> Unit = {},
        onRemove: () -> Unit,
        onEdit: () -> Unit,


        ) {

        var showQuestionOptionsMenu by remember { mutableStateOf(false) }

        Column(
            modifier = modifier.clickable {
                onClick.invoke(question);
                showQuestionOptionsMenu = true;

            }.padding(20.dp)
        ) {
            Text("Question: ${question.content}")
            Text("Correct Answer: ${question.answer}")
            DropdownMenu(
                expanded = showQuestionOptionsMenu,
                onDismissRequest = { showQuestionOptionsMenu = false }
            ) {
                RemoveQuestionOption {
                    onRemove.invoke()
                }
                EditQuestionOption {
                    onEdit.invoke()
                }
            }
        }


    }


}