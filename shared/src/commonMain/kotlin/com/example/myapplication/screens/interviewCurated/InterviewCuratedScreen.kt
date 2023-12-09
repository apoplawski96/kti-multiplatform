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
import com.example.myapplication.compose.KTICircularProgressIndicator
import com.example.myapplication.compose.KTIColumnWithGradient
import com.example.myapplication.compose.KTIIcon
import com.example.myapplication.compose.KTIIconButton
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
import com.example.myapplication.theme.kti_red
import com.example.myapplication.theme.kti_soft_white
import com.example.myapplication.theme.kti_softblack
import org.jetbrains.compose.resources.ExperimentalResourceApi

class InterviewCuratedScreen(private val categories: List<TopCategory>) : Screen {

    @Composable
    override fun Content() {
        val screenModel: InterviewCuratedScreenModel = getScreenModel()

        val state = screenModel.viewState.collectAsState().value
        val scoreboardState = screenModel.scoreboardState.collectAsState().value

        LaunchedEffect(null) { screenModel.initQuestions(categories) }

        InterviewCuratedScreenContent(
            screenState = state,
            scoreboardState = scoreboardState,
            onAddPointClick = { screenModel.questionAnsweredWithPoint() },
            onNoPointClick = { screenModel.questionAnsweredNoPoint() },
        )
    }
}

private val horizontalPadding = 8.dp

@Composable
private fun InterviewCuratedScreenContent(
    screenState: InterviewCuratedScreenModel.ViewState,
    scoreboardState: InterviewCuratedScreenModel.Scoreboard,
    onAddPointClick: () -> Unit,
    onNoPointClick: () -> Unit,
) {
    KTIColumnWithGradient {
        KTITopAppBar(title = "Interview Simulation, " + "Score: ${scoreboardState.questionsAnswered}/${scoreboardState.questionsAsked}", iconsSection = { })
        when (screenState) {
            InterviewCuratedScreenModel.ViewState.Idle -> {
                KTICircularProgressIndicator()
            }

            InterviewCuratedScreenModel.ViewState.NoQuestionsLeft -> {
                KTITextNew("no questions left", fontSize = 16.sp, fontWeight = FontWeight.W700)
            }

            is InterviewCuratedScreenModel.ViewState.QuestionDropped -> {
                Column(Modifier.fillMaxSize()) {
                    KTIIllustration(imageResource = SharedRes.images.undraw_interview_re_e5jn)
                    KTIVerticalSpacer(height = 16.dp)
                    QuestionCard(screenState.question, Modifier.weight(10f))
                    ControlSection(
                        addPointClick = onAddPointClick,
                        noPointClick = onNoPointClick,
                        modifier = Modifier.weight(1.5f),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ColumnScope.ControlSection(
    addPointClick: () -> Unit,
    noPointClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier then Modifier.fillMaxWidth().background(kti_soft_white)) {
        Divider(color = kti_grey, thickness = 1.5.dp)
        KTIVerticalSpacer(8.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KTIIconButton(onClick = addPointClick, modifier = Modifier.weight(1f)) {
                KTIIcon(imageResource = SharedRes.images.ic_check, tint = kti_green, size = 44.dp)
            }
            KTIIconButton(onClick = noPointClick, modifier = Modifier.weight(1f)) {
                KTIIcon(imageResource = SharedRes.images.ic_cross, tint = kti_red, size = 26.dp)
            }
        }
    }
}

@Composable
private fun InterviewButton(onClick: () -> Unit, label: String, iconResId: Int) {

}

@Composable
private fun QuestionCard(
    item: Question,
    modifier: Modifier = Modifier,
) {
    val isExpanded = rememberSaveable(item) { mutableStateOf(false) }
    val isAnswered = rememberSaveable(item) { mutableStateOf(false) }
    Column(
        modifier = modifier then Modifier
            .fillMaxWidth()
            .background(kti_soft_white)
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
                KTIVerticalSpacer(height = 8.dp)
                QuestionTopSection(question = item)
                QuestionTitle(isAnswered = isAnswered.value, question = item.question)
                AnimatedVisibility(visible = isExpanded.value) {
                    Column {
                        KTIVerticalSpacer(height = 2.dp)
                        KTITextNew(
                            text = item.answer,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400,
                            modifier = Modifier.padding(horizontal = horizontalPadding + 2.dp),
                        )
                        KTIVerticalSpacer(height = 16.dp)
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
//                BottomSection(
//                    isAnswered = isAnswered.value,
//                    markAsAnswered = { question ->
//                        isAnswered.value = true
//                        isExpanded.value = false
////                            markAsAnswered.invoke(question)
//                    },
//                    item = item,
//                )
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
        text = "${question.topCategory.displayName}. ${question.subCategory?.displayName ?: ""}",
        fontSize = 14.sp,
        fontWeight = FontWeight.W300,
        color = kti_softblack.copy(alpha = 0.8f),
        modifier = Modifier.padding(horizontal = 10.dp),
        lineHeight = 6.sp,
    )
}

@Composable
private fun QuestionTitle(
    isAnswered: Boolean,
    question: String,
) {
    KTIVerticalSpacer(height = if (isAnswered.not()) 4.dp else 0.dp)
    KTITextNew(
        text = question,
        fontSize = 20.sp,
        fontWeight = if (isAnswered.not()) FontWeight.Medium else FontWeight.Normal,
        modifier = Modifier.padding(horizontal = horizontalPadding + 2.dp),
        color = if (isAnswered.not()) kti_softblack else kti_soft_white,
        lineHeight = 14.sp,
    )
    KTIVerticalSpacer(height = if (isAnswered.not()) 8.dp else 0.dp)
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
        tint = if (shouldDisplayAnswer) kti_accent else kti_softblack,
        modifier = Modifier
            .clickableNoRipple { displayAnswerOnClick() }
            .size(18.dp)
    )
}