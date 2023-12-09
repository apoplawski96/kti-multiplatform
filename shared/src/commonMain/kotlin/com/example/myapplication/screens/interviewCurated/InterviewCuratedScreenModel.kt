package com.example.myapplication.screens.interviewCurated

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.example.myapplication._legacy.QuestionsRepository
import com.example.myapplication.model.Question
import com.example.myapplication.model.TopCategory
import com.example.myapplication.screens.interviewCurated.model.InterviewChatItemUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val INTERVAL = 800L

class InterviewCuratedScreenModel(
    private val questionsRepository: QuestionsRepository,
) : ScreenModel {

    data class Scoreboard(val questionsAnswered: Int, val questionsAsked: Int)

    sealed interface ViewStateChat {
        data class InterviewActive(val chatItems: List<InterviewChatItemUiModel>) : ViewStateChat
        object InterviewFinished : ViewStateChat
    }

    private val questionsBase = mutableListOf<Question>()

    private val _screenState = MutableStateFlow<ViewStateChat>(ViewStateChat.InterviewActive(chatItems = emptyList()))
    val screenState = _screenState.asStateFlow()

    private val _scoreboardState = MutableStateFlow(Scoreboard(questionsAnswered = 0, questionsAsked = 0))
    val scoreboardState = _scoreboardState.asStateFlow()

    fun initQuestions(categories: List<TopCategory>) {
        val questions = questionsRepository.getQuestionsForCategories(categories)

        questionsBase.clear()
        questionsBase.addAll(questions)

        coroutineScope.launch {
            delay(INTERVAL)
            addChatItemAndUpdateTheState(InterviewChatItemUiModel.InterviewerMessage.OtherMessage("Hello candidate."))
            delay(INTERVAL)
            dropNextQuestion()
        }
    }

    fun questionAnsweredWithPoint() {
        val currentScore = scoreboardState.value
        _scoreboardState.value = currentScore.copy(
            questionsAsked = currentScore.questionsAsked + 1,
            questionsAnswered = currentScore.questionsAnswered + 1
        )

        addChatItemAndUpdateTheState(InterviewChatItemUiModel.CandidateMessage.GoodAnswer)

        dropNextQuestion()
    }

    fun questionAnsweredNoPoint() {
        val currentScore = scoreboardState.value
        _scoreboardState.value = currentScore.copy(questionsAsked = currentScore.questionsAsked + 1)

        addChatItemAndUpdateTheState(InterviewChatItemUiModel.CandidateMessage.BadAnswer)

        dropNextQuestion()
    }

    private fun dropNextQuestion() {
        if (questionsBase.isNotEmpty()) {
            val randomIndex = Random.nextInt(from = 0, until = questionsBase.lastIndex)
            val randomQuestion = questionsBase.removeAt(randomIndex)

            addChatItemAndUpdateTheState(InterviewChatItemUiModel.InterviewerMessage.QuestionAsked(randomQuestion))
        } else {
            _screenState.value = ViewStateChat.InterviewFinished
        }
    }

    private fun addChatItemAndUpdateTheState(item: InterviewChatItemUiModel) {
        val screenState = screenState.value
        if (screenState is ViewStateChat.InterviewActive) {
            val updatedItems = screenState.chatItems.toMutableList().apply { add(item) }
            _screenState.value = ViewStateChat.InterviewActive(updatedItems)
        } else {
            _screenState.value = ViewStateChat.InterviewActive(listOf(item))
        }
    }
}