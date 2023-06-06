package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.update.QuizRepository
import com.example.carte.anerfy.view.animate.LoadingVisibilityAnimation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class QuizzesScreen : Screen {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {
        val quizzesList = remember {
            mutableStateListOf<Quiz>()
        }

        var waitingOnQuizzes = remember {
            mutableStateOf(true)
        }
        val allQuizzes = GlobalScope.async {
            QuizRepository.findAll();
        }
        val backgroundScope = rememberCoroutineScope()

        allQuizzes.invokeOnCompletion {
            backgroundScope.launch {
                quizzesList.clear()
                quizzesList.addAll(allQuizzes.getCompleted());
                waitingOnQuizzes.value = false;
                println(quizzesList.joinToString("\n"))
            }


        }
        LoadingVisibilityAnimation(waitingOnQuizzes)

        Column(
            modifier = Modifier.padding(20.dp).fillMaxSize()
        ) {
            QuizListView(quizzesList,
                modifier = Modifier.heightIn(0.dp, 460.dp).verticalScroll(rememberScrollState(0)),
                editQuestionsOnClick = {
                    screenShowing.value = EditQuestionsScreen(this)
                },
                removeOnClick = {
                    backgroundScope.launch {
                        QuizRepository.remove(QuizRepository.find(id ?: error("invalid entity in list?")))
                        quizzesList.remove(this@QuizListView)
                    }
                },
                updateOnClick = {
                    screenShowing.value = CreateQuizScreen(this)
                },
                startQuizOnClick = {
                    screenShowing.value = StartQuizScreen(this)
                }
            )

            Button(
                onClick = {
                    screenShowing.value = CreateQuizScreen()

                }
            ) {
                Icon(Icons.Rounded.Add, "add new quiz")

            }
        }


    }


    @Composable
    fun QuizListView(
        quizzesList: SnapshotStateList<Quiz>,
        modifier: Modifier = Modifier,
        editQuestionsOnClick: Quiz.() -> Unit,
        removeOnClick: Quiz.() -> Unit,
        updateOnClick: Quiz.() -> Unit,
        startQuizOnClick: Quiz.() -> Unit,
    ) {
        Column(
            modifier = modifier
        ) {
            for (quiz in quizzesList) {


                Surface(
                    elevation = 10.dp,
                    modifier = Modifier.padding(0.dp, 10.dp)
                ) {
                    QuizHolder(
                        quiz,
                        modifier = Modifier
                            .wrapContentHeight()
                            .requiredWidth(300.dp),
                        editQuestionsOnClick = editQuestionsOnClick,
                        removeOnClick = removeOnClick,
                        updateOnClick = updateOnClick,
                        startQuizOnClick = startQuizOnClick
                    )
                }


            }
        }
    }

    @Composable
    fun QuizHolder(
        quiz: Quiz, modifier: Modifier = Modifier,
        editQuestionsOnClick: Quiz.() -> Unit,
        removeOnClick: Quiz.() -> Unit,
        updateOnClick: Quiz.() -> Unit,
        startQuizOnClick: Quiz.() -> Unit,
    ) {
        var showQuizOptions = remember { mutableStateOf(false) }



        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = modifier.clickable {
                showQuizOptions.value = true;
            }.padding(20.dp)
        ) {
            Text("name: ${quiz.name}")
            Text("description: ${quiz.description}")
            Text("difficulty: ${quiz.difficulty.toString().lowercase()}")

            QuizOptionsMenu(
                showQuizOptions = showQuizOptions,
                editQuestionsOnClick = editQuestionsOnClick,
                removeOnClick = removeOnClick,
                updateOnClick = updateOnClick,
                startQuizOnClick = startQuizOnClick,
                quiz = quiz
            );
        }
    }

    @Composable
    fun QuizOptionsMenu(
        showQuizOptions: MutableState<Boolean>,
        editQuestionsOnClick: Quiz.() -> Unit,
        removeOnClick: Quiz.() -> Unit,
        updateOnClick: Quiz.() -> Unit,
        quiz: Quiz,
        startQuizOnClick: Quiz.() -> Unit
    ) {
        DropdownMenu(
            expanded = showQuizOptions.value,
            onDismissRequest = { showQuizOptions.value = false },
        ) {
            DropdownMenuItem(
                onClick = {
                    showQuizOptions.value = false;
                    editQuestionsOnClick.invoke(quiz)
                }
            ) {
                Text("edit questions")
            }
            DropdownMenuItem(
                onClick = {
                    showQuizOptions.value = false;
                    removeOnClick.invoke(quiz)

                }
            ) {
                Text("remove")
            }
            DropdownMenuItem(
                onClick = {
                    showQuizOptions.value = false;
                    updateOnClick.invoke(quiz);
                }
            ) {
                Text("update")
            }
            DropdownMenuItem(
                onClick = {
                    showQuizOptions.value = false;
                    startQuizOnClick.invoke(quiz)
                }
            ) {
                Text("start quiz")
            }
        }
    }
}