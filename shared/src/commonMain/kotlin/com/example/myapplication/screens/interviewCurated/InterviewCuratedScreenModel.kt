package com.example.myapplication.screens.interviewCurated

import cafe.adriel.voyager.core.model.ScreenModel
import com.example.myapplication._legacy.QuestionsRepository
import com.example.myapplication.model.Question
import com.example.myapplication.model.TopCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class InterviewCuratedScreenModel(
    private val questionsRepository: QuestionsRepository,
) : ScreenModel {

    data class Scoreboard(val questionsAnswered: Int, val questionsAsked: Int)

    sealed interface ViewState {
        object Idle : ViewState
        object NoQuestionsLeft : ViewState
        data class QuestionDropped(val question: Question) : ViewState
    }

    private val questionsBase = mutableListOf<Question>()

    private val _scoreboardState = MutableStateFlow(Scoreboard(questionsAnswered = 0, questionsAsked = 0))
    val scoreboardState = _scoreboardState.asStateFlow()

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Idle)
    val viewState = _viewState.asStateFlow()

    fun initQuestions(categories: List<TopCategory>) {
        val questions = questionsRepository.getQuestionsForCategories(categories)

        questionsBase.clear()
        questionsBase.addAll(questions)

        dropNextQuestion()
    }

    fun questionAnsweredWithPoint() {
        val currentScore = scoreboardState.value
        val newScore = currentScore.copy(
            questionsAsked = currentScore.questionsAsked + 1,
            questionsAnswered = currentScore.questionsAnswered + 1
        )

        _scoreboardState.value = newScore
        dropNextQuestion()
    }

    fun questionAnsweredNoPoint() {
        val currentScore = scoreboardState.value
        val newScore = currentScore.copy(questionsAsked = currentScore.questionsAsked + 1)

        _scoreboardState.value = newScore
        dropNextQuestion()
    }

    private fun dropNextQuestion() {
        if (questionsBase.isNotEmpty()) {
            val randomIndex = Random.nextInt(from = 0, until = questionsBase.lastIndex)
            val randomQuestion = questionsBase.removeAt(randomIndex)

            _viewState.value = ViewState.QuestionDropped(randomQuestion)
        } else {

            _viewState.value = ViewState.NoQuestionsLeft
        }
    }
}