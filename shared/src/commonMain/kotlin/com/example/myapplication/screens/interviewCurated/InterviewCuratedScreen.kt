package com.example.myapplication.screens.interviewCurated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.appkickstarter.shared.SharedRes
import com.example.myapplication.compose.KTIButton
import com.example.myapplication.compose.KTIColumnWithGradient
import com.example.myapplication.compose.KTIIllustration
import com.example.myapplication.compose.KTITextNew
import com.example.myapplication.compose.KTITopAppBar
import com.example.myapplication.compose.KTIVerticalSpacer
import com.example.myapplication.compose.clickableNoRipple
import com.example.myapplication.di.getScreenModel
import com.example.myapplication.model.Question
import com.example.myapplication.model.TopCategory
import com.example.myapplication.theme.kti_accent
import com.example.myapplication.theme.kti_divider
import com.example.myapplication.theme.kti_green
import com.example.myapplication.theme.kti_grey
import com.example.myapplication.theme.kti_soft_black
import com.example.myapplication.theme.kti_soft_white

class InterviewCuratedScreen(private val categories: List<TopCategory>) : Screen {

    @Composable
    override fun Content() {
        val screenModel: InterviewCuratedScreenModel = getScreenModel()

        val state = screenModel.viewState.collectAsState().value

        LaunchedEffect(null) { screenModel.initQuestions(categories) }

        InterviewCuratedScreenContent(
            state = state,
            onDropQuestionClick = { screenModel.dropQuestion() }
        )
    }
}

private val horizontalPadding = 8.dp

@Composable
private fun InterviewCuratedScreenContent(
    state: InterviewCuratedScreenModel.ViewState,
    onDropQuestionClick: () -> Unit,
) {
    KTIColumnWithGradient {
        KTITopAppBar(title = "Interview Simulation", iconsSection = { })
        when(state) {
            InterviewCuratedScreenModel.ViewState.Introduction -> {
                KTITextNew("yoyo yo interview costam", fontSize = 16.sp, fontWeight = FontWeight.W700)
                KTIButton(label = "Start interview", onClick = onDropQuestionClick)
            }
            InterviewCuratedScreenModel.ViewState.NoQuestionsLeft -> {
                KTITextNew("no questions left", fontSize = 16.sp, fontWeight = FontWeight.W700)
            }
            is InterviewCuratedScreenModel.ViewState.QuestionDropped -> {
                Column(Modifier.fillMaxSize()) {
                    KTIIllustration(imageResource = SharedRes.images.undraw_interview_re_e5jn)
                    KTIVerticalSpacer(height = 16.dp)
                    QuestionCard(state.question, Modifier.weight(10f))
                    ControlSection(onDropQuestionClick, Modifier.weight(1.5f))
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.ControlSection(
    onDropQuestionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier then Modifier.background(kti_grey)) {
        KTIButton(label = "Drop question", onClick = onDropQuestionClick, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun QuestionCard(
    item: Question,
    modifier: Modifier = Modifier,
//        markAsAnswered: (Question) -> Unit,
//        markAsUnanswered: (Question) -> Unit,
) {
    val isExpanded = rememberSaveable(item) { mutableStateOf(false) }
    val isAnswered = rememberSaveable(item) { mutableStateOf(false) }
    Column(
        modifier = modifier then Modifier
            .fillMaxWidth()
            .background(
//                    if (isAnswered.value) {
//                        kti_green
//                    } else {
//                        kti_soft_white
//                    }
                kti_soft_white
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(8f)
            ) {
                KTIVerticalSpacer(height = 4.dp)
                QuestionTopSection(question = item)
                QuestionTitle(isAnswered = isAnswered.value, question = item.question)
                AnimatedVisibility(visible = isExpanded.value) {
                    Column {
                        KTIVerticalSpacer(height = 2.dp)
                        KTITextNew(
                            text = item.answer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            modifier = Modifier.padding(horizontal = horizontalPadding + 2.dp),
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f)
                    .defaultMinSize(minHeight = 52.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isAnswered.value.not()) {
                    ToggleAnswerButton(
                        shouldDisplayAnswer = isExpanded.value,
                        displayAnswerOnClick = { isExpanded.value = !isExpanded.value },
                    )
                } else {
                    IconButton(onClick = {
                        isAnswered.value = false
                        isExpanded.value = false
//                            markAsUnanswered.invoke(item)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Reopen question",
                            tint = kti_soft_white,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = isExpanded.value || isAnswered.value) {
            Column(modifier = Modifier.fillMaxWidth()) {
                BottomSection(
                    isAnswered = isAnswered.value,
                    markAsAnswered = { question ->
                        isAnswered.value = true
                        isExpanded.value = false
//                            markAsAnswered.invoke(question)
                    },
                    item = item,
                )
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = kti_divider.copy(alpha = 0.4f)
        )
    }
}

@Composable
private fun QuestionTopSection(
    question: Question,
) {
    KTITextNew(
        text = "${question.topCategory.displayName}, ${question.subCategory?.displayName ?: ""}",
        fontSize = 10.sp,
        fontWeight = FontWeight.W300,
        color = kti_soft_black.copy(alpha = 0.6f),
        modifier = Modifier.padding(horizontal = 10.dp),
        lineHeight = 6.sp,
    )
}

@Composable
private fun QuestionTitle(
    isAnswered: Boolean,
    question: String,
) {
    KTIVerticalSpacer(height = if (isAnswered.not()) 2.dp else 0.dp)
    KTITextNew(
        text = question,
        fontSize = 20.sp,
        fontWeight = if (isAnswered.not()) FontWeight.Medium else FontWeight.Normal,
        modifier = Modifier.padding(horizontal = horizontalPadding + 2.dp),
        color = if (isAnswered.not()) kti_soft_black else kti_soft_white,
        lineHeight = 14.sp,
    )
    KTIVerticalSpacer(height = if (isAnswered.not()) 4.dp else 0.dp)
}

@Composable
private fun BottomSection(
    isAnswered: Boolean,
    markAsAnswered: (Question) -> Unit,
    item: Question,
) {
    if (isAnswered.not()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = horizontalPadding + 8.dp,
                    end = horizontalPadding + 8.dp,
                    top = 12.dp,
                    bottom = 2.dp
                )
        ) {
            MarkAsAnsweredButton(
                toggleAsAnswered = { markAsAnswered(item) },
            )
        }
    }
}

@Composable
private fun MarkAsAnsweredButton(
    toggleAsAnswered: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(onClick = toggleAsAnswered) {
            KTITextNew(
                text = "Mark as answered",
                fontSize = 12.sp,
                fontWeight = FontWeight.W600,
                color = kti_green,
            )
        }
    }
}
@Composable
private fun ToggleAnswerButton(
    shouldDisplayAnswer: Boolean,
    displayAnswerOnClick: () -> Unit,
) {
    Icon(
        imageVector = if (shouldDisplayAnswer) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = null,
        tint = if (shouldDisplayAnswer) kti_accent else kti_soft_black,
        modifier = Modifier
            .clickableNoRipple { displayAnswerOnClick() }
            .size(18.dp)
    )
}