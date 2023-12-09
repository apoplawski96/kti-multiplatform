package com.example.myapplication.screens.interviewCurated

import cafe.adriel.voyager.core.model.ScreenModel
import com.example.myapplication._legacy.QuestionsRepository
import com.example.myapplication.model.Question
import com.example.myapplication.model.TopCategory
import com.example.myapplication.screens.interviewCurated.model.InterviewChatItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

private const val delayValue = 200

class InterviewCuratedScreenModel(
    private val questionsRepository: QuestionsRepository,
) : ScreenModel {

    data class Scoreboard(val questionsAnswered: Int, val questionsAsked: Int)

    sealed interface ViewStateChat {
        data class InterviewActive(val chatItems: List<InterviewChatItemUiModel>) : ViewStateChat
        object InterviewFinished : ViewStateChat
    }

    private val _viewStateNew =
        MutableStateFlow<ViewStateChat>(ViewStateChat.InterviewActive(chatItems = emptyList()))
    val screenState = _viewStateNew.asStateFlow()

    private val questionsBase = mutableListOf<Question>()

    private val _scoreboardState =
        MutableStateFlow(Scoreboard(questionsAnswered = 0, questionsAsked = 0))
    val scoreboardState = _scoreboardState.asStateFlow()

    fun initQuestions(categories: List<TopCategory>) {
        val questions = questionsRepository.getQuestionsForCategories(categories)

        questionsBase.clear()
        questionsBase.addAll(questions)

        dropNextQuestionNew()
    }

    fun questionAnsweredWithPoint() {
        val screenState = screenState.value
        val currentScore = scoreboardState.value

        if (screenState !is ViewStateChat.InterviewActive) return

        _scoreboardState.value = currentScore.copy(
            questionsAsked = currentScore.questionsAsked + 1,
            questionsAnswered = currentScore.questionsAnswered + 1
        )

        val updatedChatItems = screenState.chatItems.toMutableList().apply {
            add(InterviewChatItemUiModel.Answer.GoodAnswer)
        }

        _viewStateNew.value = screenState.copy(updatedChatItems)

        dropNextQuestionNew()
    }

    fun questionAnsweredNoPoint() {
        val screenState = screenState.value
        val currentScore = scoreboardState.value

        if (screenState !is ViewStateChat.InterviewActive) return

        _scoreboardState.value = currentScore.copy(questionsAsked = currentScore.questionsAsked + 1)

        val updatedChatItems = addChatItem(
            item = InterviewChatItemUiModel.Answer.BadAnswer,
            screenState = screenState,
        )

        _viewStateNew.value = screenState.copy(updatedChatItems)

        dropNextQuestionNew()
    }

    private fun dropNextQuestionNew() {
        val screenState = screenState.value
        if (questionsBase.isNotEmpty() && screenState is ViewStateChat.InterviewActive) {
            val randomIndex = Random.nextInt(from = 0, until = questionsBase.lastIndex)
            val randomQuestion = questionsBase.removeAt(randomIndex)

            val updatedChatItems = addChatItem(
                item = InterviewChatItemUiModel.InterviewerQuestion(randomQuestion),
                screenState = screenState
            )

            _viewStateNew.value = ViewStateChat.InterviewActive(updatedChatItems)
        } else {
            _viewStateNew.value = ViewStateChat.InterviewFinished
        }
    }

    private fun addChatItem(
        item: InterviewChatItemUiModel,
        screenState: ViewStateChat.InterviewActive
    ): List<InterviewChatItemUiModel> {
        return screenState.chatItems.toMutableList().apply { add(item) }
    }
}