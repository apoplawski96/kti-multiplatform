package com.example.myapplication._legacy

import com.example.myapplication.data.QuestionsDataSource
import com.example.myapplication.feature.list.data.QuestionsMapper
import com.example.myapplication.model.Question
import com.example.myapplication.model.TopCategory

class QuestionsRepository(
    private val questionsDataSource: QuestionsDataSource,
    private val questionsMapper: QuestionsMapper,
) {

    fun getAllQuestions(): List<Question> {
        return fetchAndMapQuestions()
    }

    fun getQuestionsForCategories(categories: List<TopCategory>): List<Question> {

        return fetchAndMapQuestions().filter { question ->
//            categories.contains(question.topCategory)
            categories.any { it == question.topCategory }
        }
    }

    private fun fetchAndMapQuestions(): List<Question> {
        return questionsMapper.map(questionsDataSource.getAll())
    }
}