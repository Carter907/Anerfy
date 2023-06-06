package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carte.anerfy.model.Difficulty
import com.example.carte.anerfy.model.Question
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.update.QuizRepository
import com.example.carte.anerfy.view.BasicAlert
import com.example.carte.anerfy.view.AlertMessages
import com.example.carte.anerfy.view.BackButton
import com.example.carte.anerfy.view.StandardScreen
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection

const val NUM_OF_QUESTIONS = 4;

class AddQuestionScreen(
    private var quiz: Quiz =
        Quiz(
            name = "",
            description = "",
            difficulty = Difficulty.EASY,
            questions = hashSetOf()
        ),
    private val question: Question =
        Question(
            content = "",
            answer = "",
            possibleAnswers = Array(NUM_OF_QUESTIONS) { "" }
        )
) : Screen {

    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {


        val answerChoices = HashMap<Char, MutableState<String>>();

        for (letter in 'A'..'D') {
            answerChoices.put(letter, remember { mutableStateOf(question.possibleAnswers[letter.code-65])})

        }

        var questionContent by remember { mutableStateOf(question.content) }

        var correctAnswer by remember { mutableStateOf(question.answer) };

        var showAnswerMenu by remember { mutableStateOf(false) }

        var showAlert by remember { mutableStateOf(false) }
        var alertMessage by remember { mutableStateOf("") }


        StandardScreen(
            title = "Add a Question to your Quiz!",
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,

                    ) {
                    AddQuestionButton {
                        when {
                            questionContent.isEmpty() -> {
                                alertMessage = AlertMessages.QuestionContentEmpty
                            }

                            correctAnswer.isEmpty() -> {
                                alertMessage = AlertMessages.AnswerIsNotSelected

                            }

                            correctAnswer !in answerChoices.values.map { it.value } -> {
                                alertMessage = AlertMessages.AnswerNotInPossibleAnswers


                            }

                            else -> {
                                addQuestionToQuiz(questionContent, answerChoices, correctAnswer)
                                alertMessage = AlertMessages.QuestionAdded
                            }
                        }
                        showAlert = true;

                    }
                    ResetFormDataButton {

                        quiz = QuizRepository.withTransaction {

                            createQuery("select q from Quiz q where q.id = ${quiz.id}").singleResult as Quiz
                        }
                        questionContent = "";
                        correctAnswer = "";
                        for (letter in 'A'..'D') {
                            answerChoices.get(letter)?.value = "";

                        }


                    }
                    BackButton {
                        screenShowing.value = EditQuestionsScreen(quiz);
                    }

                }


            }

        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,

                ) {

                // get the question itself

                OutlinedTextField(
                    questionContent,
                    { questionContent = it },
                    modifier = Modifier.padding(vertical = 20.dp),
                    placeholder = { Text("please enter the question") }
                )

                AnswerTextboxes(answerChoices, correctAnswer);

                MarkCorrectAnswerButton {
                    showAnswerMenu = true;
                }
                MarkCorrectAnswerMenu(
                    showMenu = showAnswerMenu,
                    onDismissRequest = { showAnswerMenu = false },
                    answerChoices
                ) {

                    if (it.isNotEmpty()) {
                        correctAnswer = it;
                    } else {
                        alertMessage = AlertMessages.AnswerIsNotSelected
                        showAlert = true;
                    }


                }
                if (showAlert) {
                    BasicAlert(
                        text = alertMessage,
                        onDismissRequest = { showAlert = false }
                    )

                }

            }

        }

    }


    @Composable
    fun ResetFormDataButton(onClick: () -> Unit) {
        Button(onClick) {
            Text("Clear Form");
        }
    }

    @Composable
    fun AnswerTextboxes(answerChoices: HashMap<Char, MutableState<String>>, correctAnswer: String) {
        for (letterChoicePair in answerChoices) {
            val answer = letterChoicePair.value;
            val letter = letterChoicePair.key;

            val answerIcon: @Composable () -> Unit =
                when {
                    correctAnswer.isNotEmpty() && answer.value == correctAnswer -> {
                        { Icon(Icons.Rounded.Check, "Correct") }
                    }

                    correctAnswer.isNotEmpty() -> {
                        { Icon(Icons.Rounded.Close, "Incorrect") }

                    }

                    else -> {
                        {}
                    }
                }
            OutlinedTextField(
                value = answer.value,
                onValueChange = { answer.value = it },
                placeholder = {

                    Text("Answer $letter: ")


                },
                label = { Text("$letter") },
                leadingIcon = answerIcon
            )


        }
    }

    @Composable
    fun MarkCorrectAnswerButton(onMarkCorrectAnswerMenuRequested: () -> Unit) {
        Button(onClick = onMarkCorrectAnswerMenuRequested) {
            Text("Choose an Answer")

        }

    }


    @Composable
    fun MarkCorrectAnswerMenu(
        showMenu: Boolean,
        onDismissRequest: () -> Unit,
        answerChoices: HashMap<Char,
                MutableState<String>>,
        onChoiceClicked: (answer: String) -> Unit,


        ) {
        DropdownMenu(showMenu, onDismissRequest) {
            Text(modifier = Modifier.padding(10.dp), text = "Mark which of the following as the answer: ")
            for (answerChoice in answerChoices)
                DropdownMenuItem(
                    onClick = {
                        onChoiceClicked.invoke(answerChoice.value.value)
                        onDismissRequest.invoke();
                    }
                ) {
                    Text("Choice ${answerChoice.key}")
                }
        }
    }


    private fun addQuestionToQuiz(
        questionContent: String,
        answerChoices: HashMap<Char, MutableState<String>>,
        correctAnswer: String
    ) {
        QuizRepository.withTransaction {

            val newQuestion = Question(
                id = question.id,
                content = questionContent,
                answer = correctAnswer,
                possibleAnswers = answerChoices.values.map { it.value }.toTypedArray()
            )
            println("before: ${quiz.questions}")
            quiz.questions = hashSetOf(
                *quiz.questions.toMutableList().run {

                    val replacedQuestion = find { it.id == newQuestion.id }
                    if (replacedQuestion != null) {
                        set(indexOf(replacedQuestion), newQuestion)
                    } else {
                        add(newQuestion)
                    }



                    toTypedArray()
                }
            )
            println("after ${quiz.questions}")

            if (quiz.id != null && find(Quiz::class.java, quiz.id) != null) {

                merge(quiz);

            } else {

                persist(quiz)
            }


        }
    }


    @Composable
    fun AddQuestionButton(onSubmit: () -> Unit) {

        var questionAlertDialogShowing = remember { mutableStateOf(false) }
        Button(onClick = {

            onSubmit.invoke()
            questionAlertDialogShowing.value = true;

        }) {
            Text("Save Changes")

        }

    }
}