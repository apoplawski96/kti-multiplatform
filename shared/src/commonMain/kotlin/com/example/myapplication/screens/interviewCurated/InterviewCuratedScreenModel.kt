package com.example.myapplication.screens.interviewCurated

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.example.myapplication._legacy.QuestionsRepository
import com.example.myapplication.model.Question
import com.example.myapplication.model.TopCategory
import com.example.myapplication.screens.interviewCurated.model.InterviewChatItemUiModel
import com.example.myapplication.screens.interviewCurated.model.ProgressObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val interval = 200L

class InterviewCuratedScreenModel(
    private val questionsRepository: QuestionsRepository,
) : ScreenModel {

    data class Scoreboard(
        val questionsAnswered: Int,
        val questionsAsked: Int
    )

    sealed interface ViewStateChat {
        data class InterviewActive(val chatItems: List<InterviewChatItemUiModel>) : ViewStateChat
        object InterviewFinished : ViewStateChat
    }

    private val questionsBase = mutableListOf<Question>()

    private val _screenState = MutableStateFlow<ViewStateChat>(ViewStateChat.InterviewActive(chatItems = emptyList()))
    val screenState = _screenState.asStateFlow()

    private val _scoreboardState = MutableStateFlow(Scoreboard(questionsAnswered = 0, questionsAsked = 0))
    val scoreboardState = _scoreboardState.asStateFlow()

    val inputEnabled = screenState.map { screenState ->
        if (screenState is ViewStateChat.InterviewActive) {
            screenState.chatItems.lastOrNull() is InterviewChatItemUiModel.CandidateMessage.Writing
        } else {
            false
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = false
    )

    fun initQuestions(categories: List<TopCategory>) {
        val questions = questionsRepository.getQuestionsForCategories(categories)

        questionsBase.clear()
        questionsBase.addAll(questions)

        coroutineScope.launch {
            emitInterviewerProgressObject()
            delay(interval)
            emitMessageItemAndUpdateTheState(InterviewChatItemUiModel.InterviewerMessage.OtherMessage("Hello candidate."))
            dropNextQuestion()
        }
    }

    fun questionAnsweredWithPoint() {
        scoreboardState.value.let { scoreboard ->
            _scoreboardState.value = scoreboard.copy(
                questionsAsked = scoreboard.questionsAsked + 1,
                questionsAnswered = scoreboard.questionsAnswered + 1
            )
        }

        coroutineScope.launch {
            emitMessageItemAndUpdateTheState(InterviewChatItemUiModel.CandidateMessage.GoodAnswer)
            emitInterviewerPositiveResponse()
            dropNextQuestion()
        }
    }

    fun questionAnsweredNoPoint() {
        scoreboardState.value.let { scoreboard ->
            _scoreboardState.value = scoreboard.copy(questionsAsked = scoreboard.questionsAsked + 1)
        }

        coroutineScope.launch {
            emitMessageItemAndUpdateTheState(InterviewChatItemUiModel.CandidateMessage.BadAnswer)
            emitInterviewerNegativeResponse()
            dropNextQuestion()
        }
    }

    private suspend fun dropNextQuestion() {
        if (questionsBase.isNotEmpty()) {
            val randomIndex = Random.nextInt(from = 0, until = questionsBase.lastIndex)
            val randomQuestion = questionsBase.removeAt(randomIndex)

            delay(interval)
            emitInterviewerProgressObject()
            delay(interval)
            emitMessageItemAndUpdateTheState(InterviewChatItemUiModel.InterviewerMessage.QuestionAsked(randomQuestion))
            delay(interval)
            emitCandidateProgressObject()
        } else {
            _screenState.value = ViewStateChat.InterviewFinished
        }
    }

    private fun emitMessageItemAndUpdateTheState(item: InterviewChatItemUiModel) {
        val screenState = screenState.value
        if (screenState is ViewStateChat.InterviewActive) {
            val updatedItems = screenState.chatItems.toMutableList().apply { add(item) }.filterNot { it is ProgressObject }
            _screenState.value = ViewStateChat.InterviewActive(updatedItems)
        }
    }

    private suspend fun emitInterviewerPositiveResponse() {
        delay(interval)
        emitInterviewerProgressObject()
        delay(interval)
        emitMessageItemAndUpdateTheState(InterviewChatItemUiModel.InterviewerMessage.OtherMessage("That's a great answer!"))
    }

    private suspend fun emitInterviewerNegativeResponse() {
        delay(interval)
        emitInterviewerProgressObject()
        delay(interval)
        emitMessageItemAndUpdateTheState(InterviewChatItemUiModel.InterviewerMessage.OtherMessage("No worries. Let's try with another question."))
    }

    private fun emitInterviewerProgressObject() {
        addProgressObjectAndUpdateTheState(InterviewChatItemUiModel.InterviewerMessage.Writing)
    }

    private fun emitCandidateProgressObject() {
        addProgressObjectAndUpdateTheState(InterviewChatItemUiModel.CandidateMessage.Writing)
    }

    private fun addProgressObjectAndUpdateTheState(progressObject: InterviewChatItemUiModel) {
        val screenState = screenState.value
        if (screenState is ViewStateChat.InterviewActive) {
            val updatedItems = screenState.chatItems.toMutableList().apply { add(progressObject) }
            _screenState.value = ViewStateChat.InterviewActive(updatedItems)
        }
    }
}