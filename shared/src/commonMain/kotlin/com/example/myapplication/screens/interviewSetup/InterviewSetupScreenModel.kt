package com.example.myapplication.screens.interviewSetup

import com.example.myapplication.feature.categories.data.CategoriesRepository
import com.example.myapplication.screens.interviewSetup.model.SelectableCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InterviewSetupScreenModel(private val categoriesRepository: CategoriesRepository) {

    private val _viewState = MutableStateFlow<List<SelectableCategory>>(emptyList())
    val viewState = _viewState.asStateFlow()

    init {
        _viewState.value = categoriesRepository.getTopCategories().map { category ->
            SelectableCategory(category = category, isSelected = false)
        }
    }

    fun toggleCategory(toggledCategory: SelectableCategory) {
        val currentState = viewState.value
        val stateModified = currentState.map { currentCategory ->
            if (currentCategory.category.id == toggledCategory.category.id) {
                currentCategory.copy(isSelected = !currentCategory.isSelected)
            } else {
                currentCategory
            }
        }
        _viewState.value = stateModified
    }
}