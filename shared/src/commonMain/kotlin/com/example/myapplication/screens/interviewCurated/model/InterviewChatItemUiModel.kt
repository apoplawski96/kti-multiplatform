package com.example.myapplication.screens.interviewCurated.model

import com.example.myapplication.model.Question

sealed class InterviewChatItemUiModel {

    sealed class InterviewerMessage : InterviewChatItemUiModel() {
        data class QuestionAsked(val question: Question) : InterviewerMessage()
        data class OtherMessage(val message: String) : InterviewerMessage()
        object Writing : InterviewerMessage()
    }

    sealed class CandidateMessage : InterviewChatItemUiModel() {
        object GoodAnswer : CandidateMessage()
        object BadAnswer: CandidateMessage()
        object Writing : CandidateMessage()
    }
}
