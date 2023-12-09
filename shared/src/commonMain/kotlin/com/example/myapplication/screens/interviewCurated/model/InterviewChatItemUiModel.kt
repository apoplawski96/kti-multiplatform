package com.example.myapplication.screens.interviewCurated.model

import com.example.myapplication.model.Question

sealed class InterviewChatItemUiModel {
    data class InterviewerQuestion(val question: Question) : InterviewChatItemUiModel()
    sealed class Answer : InterviewChatItemUiModel() {
        object GoodAnswer : Answer()
        object BadAnswer: Answer()
    }
}
