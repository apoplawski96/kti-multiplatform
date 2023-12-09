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

    sealed interface ViewState {
        object Introduction : ViewState
        object NoQuestionsLeft : ViewState
        data class QuestionDropped(val question: Question, val isAnswered: Boolean) : ViewState
    }

    private val questionsBase = mutableListOf<Question>()

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Introduction)
    val viewState = _viewState.asStateFlow()

    fun initQuestions(categories: List<TopCategory>) {
        val questions = questionsRepository.getQuestionsForCategories(categories)

        questionsBase.clear()
        questionsBase.addAll(questions)
    }

    fun setQuestionAnswered() {
        val currentViewState = viewState.value
        if (currentViewState is ViewState.QuestionDropped) {
            _viewState.value = currentViewState.copy(isAnswered = true)
        }
    }

    fun dropQuestion() {
        if (questionsBase.isNotEmpty()) {
            val randomIndex = Random.nextInt(from = 0, until = questionsBase.lastIndex)
            val randomQuestion = questionsBase.removeAt(randomIndex)

            _viewState.value = ViewState.QuestionDropped(randomQuestion, isAnswered = false)
        } else {

            _viewState.value = ViewState.NoQuestionsLeft
        }
    }
}