package com.example.carte.anerfy.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carte.anerfy.model.Difficulty
import com.example.carte.anerfy.model.Question
import com.example.carte.anerfy.model.Quiz
import com.example.carte.anerfy.update.QuizRepository
import com.example.carte.anerfy.view.BasicAlert
import com.example.carte.anerfy.view.StandardScreen

class AddQuestionScreen(
    private val quiz: Quiz =
        Quiz(
            name = "",
            description = "",
            difficulty = Difficulty.EASY,
            questions = hashSetOf()
        )
) : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Screen(screenShowing: MutableState<Screen>) {


        val answerChoices = hashMapOf(

            'A' to remember { mutableStateOf("") },
            'B' to remember { mutableStateOf("") },
            'C' to remember { mutableStateOf("") },
            'D' to remember { mutableStateOf("") },
        )

        var questionContent by remember { mutableStateOf("") }

        var correctAnswer by remember { mutableStateOf("") };

        var showAnswerMenu by remember { mutableStateOf(false) }

        var showAlertAnswerEmpty by remember { mutableStateOf(false) }

        var showAlertCorrectAnswerMissing by remember { mutableStateOf(false) }




        StandardScreen(
            title = "Add a Question to your Quiz!",
            bottomBar = {
                Row(
                    modifier = Modifier.requiredHeight(30.dp).width(300.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AddQuestionButton {
                        if (correctAnswer !in answerChoices.values.map { it.value }) {
                            showAlertCorrectAnswerMissing = true
                        }
                        addQuestionToQuiz(questionContent, answerChoices, correctAnswer)
                    }
                    ResetFormDataButton {
                        questionContent = "";
                        correctAnswer = "";
                        showAnswerMenu = false;

                        for (letter in ('A'..'D')) {
                            answerChoices[letter]?.value = "";
                        }

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
                        showAlertAnswerEmpty = true;
                    }


                }
                if (showAlertCorrectAnswerMissing) {
                    BasicAlert(
                        text = "please make sure you have at least one answer that equals \"$correctAnswer\" or change your current answer.",
                        onDismissRequest = { showAlertCorrectAnswerMissing = false }

                    )

                }

                if (showAlertAnswerEmpty)
                    BasicAlert(
                        text = "Please pick an answer that is not empty",
                        onDismissRequest = { showAlertAnswerEmpty = false }
                    )

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

            quiz.questions + Question(
                content = questionContent,
                possibleAnswers = answerChoices.values.map { it.value }.toTypedArray(),
                answer = correctAnswer

            );

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
            Text("Add Question")

        }

    }
}