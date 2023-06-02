package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carte.anerfy.model.Question
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.update.QuestionRepository
import com.example.carte.anerfy.view.BackButton
import com.example.carte.anerfy.view.StandardScreen

class EditQuestionsScreen(private val quiz: Quiz) : Screen {



    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {

        var showQuestionOptionsMenu by remember { mutableStateOf(false) };

        val questions = remember { mutableStateListOf(*quiz.questions.toTypedArray()) }



        StandardScreen(
            title = "Edit Questions in Quiz",
            bottomBar = {
                BackButton {
                    screenShowing.value = QuizzesScreen();
                }
            }

        ) {

            Column {
                for (question in questions) {
                    Surface(
                        elevation = 10.dp
                    ) {

                        QuestionHolder(
                            question = question,
                            modifier = Modifier.requiredWidth(300.dp),
                            onClick = {
                                showQuestionOptionsMenu = true
                            }

                        )
                        DropdownMenu(
                            expanded = showQuestionOptionsMenu,
                            onDismissRequest = { showQuestionOptionsMenu = false }
                        ) {
                            RemoveQuestionOption {
                                QuestionRepository.withTransaction {
                                    createQuery("delete from Question q where q.id = ${question.id}")
                                }
                                questions.remove(question)
                            }
                            EditQuestionOption {
                                screenShowing.value = AddQuestionScreen(quiz = quiz, question = question);
                            }
                        }


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
    fun SaveButton() {}

    @Composable
    fun CancelButton() {}

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
        onClick: Question.() -> Unit,

    ) {

        Column(
            modifier = modifier.clickable {
                onClick.invoke(question);

            }.padding(20.dp)
        ) {
            Text("Question: ${question.content}")
            Text("Correct Answer: ${question.answer}")
        }


    }


}