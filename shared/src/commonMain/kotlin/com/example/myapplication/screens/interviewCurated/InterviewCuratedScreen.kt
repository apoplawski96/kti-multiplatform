@file:OptIn(ExperimentalFoundationApi::class)

package com.example.myapplication.screens.interviewCurated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.appkickstarter.shared.SharedRes
import com.example.myapplication.compose.KTIButtonShared
import com.example.myapplication.compose.KTIColumnWithGradient
import com.example.myapplication.compose.KTIHorizontalSpacer
import com.example.myapplication.compose.KTIIcon
import com.example.myapplication.compose.KTITextNew
import com.example.myapplication.compose.KTITopAppBar
import com.example.myapplication.compose.KTIVerticalSpacer
import com.example.myapplication.di.getScreenModel
import com.example.myapplication.model.Question
import com.example.myapplication.model.TopCategory
import com.example.myapplication.screens.interviewCurated.model.InterviewChatItemUiModel
import com.example.myapplication.theme.kti_accent
import com.example.myapplication.theme.kti_dark_blue
import com.example.myapplication.theme.kti_green
import com.example.myapplication.theme.kti_grey
import com.example.myapplication.theme.kti_light_blue
import com.example.myapplication.theme.kti_red
import com.example.myapplication.theme.kti_softblack
import com.example.myapplication.theme.kti_softwhite

class InterviewCuratedScreen(private val categories: List<TopCategory>) : Screen {

    @Composable
    override fun Content() {
        val screenModel: InterviewCuratedScreenModel = getScreenModel()

        val scoreboardState = screenModel.scoreboardState.collectAsState().value
        val chatState = screenModel.screenState.collectAsState().value
        val inputEnabledState = screenModel.inputEnabled.collectAsState().value
        val currentQuestion = screenModel.currentQuestion.collectAsState().value

        val chatListState = rememberLazyListState()

        LaunchedEffect(null) { screenModel.initQuestions(categories) }

        LaunchedEffect(chatState, inputEnabledState) {
            if (chatState is InterviewCuratedScreenModel.ViewStateChat.InterviewActive && chatState.chatItems.isNotEmpty()) {
                chatListState.animateScrollToItem(chatState.chatItems.lastIndex)
            }
        }

        InterviewChatScreenContent(
            scoreboardState = scoreboardState,
            onAddPointClick = { screenModel.questionAnsweredWithPoint() },
            onNoPointClick = { screenModel.questionAnsweredNoPoint() },
            screenStateChat = chatState,
            inputEnabled = inputEnabledState,
            chatListState = chatListState,
            currentQuestion = currentQuestion,
        )
    }
}

private val horizontalPadding = 8.dp

@Composable
private fun InterviewChatScreenContent(
    screenStateChat: InterviewCuratedScreenModel.ViewStateChat,
    scoreboardState: InterviewCuratedScreenModel.Scoreboard,
    onAddPointClick: () -> Unit,
    onNoPointClick: () -> Unit,
    inputEnabled: Boolean,
    chatListState: LazyListState,
    currentQuestion: Question?,
) {
    val isAnswerExpanded = rememberSaveable(screenStateChat) { mutableStateOf(false) }
    KTIColumnWithGradient {
        KTITopAppBar(
            title = "Interview Simulation, " + "Score: ${scoreboardState.questionsAnswered}/${scoreboardState.questionsAsked}",
            iconsSection = { },
        )
        when (screenStateChat) {
            is InterviewCuratedScreenModel.ViewStateChat.InterviewActive -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier.weight(10f)) {
                        LazyColumn(
                            modifier = Modifier.align(Alignment.TopCenter),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            state = chatListState,
                        ) {
                            item { KTIVerticalSpacer(height = 8.dp) }
                            itemsIndexed(
                                items = screenStateChat.chatItems,
                                key = { i, it -> "${it.hashCode()}, index: $i" }) { _, chatItem ->
                                when (chatItem) {
                                    is InterviewChatItemUiModel.CandidateMessage -> {
                                        CandidateBubbleChatItem(chatItem)
                                    }

                                    is InterviewChatItemUiModel.InterviewerMessage -> {
                                        InterviewerBubbleChatItem(chatItem)
                                    }
                                }
                            }
                            item { KTIVerticalSpacer(height = 8.dp) }
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            isAnswerExpanded.value,
                            modifier = Modifier.align(Alignment.BottomEnd).padding(start = 32.dp, end = 16.dp, bottom = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topEnd = radius,
                                            topStart = radius,
                                            bottomStart = radius,
                                            bottomEnd = 0.dp,
                                        )
                                    )
                                    .background(kti_softwhite)
                                    .padding(vertical = 8.dp, horizontal = 12.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                KTIVerticalSpacer(4.dp)
                                KTIIcon(SharedRes.images.ic_info, size = 16.dp)
                                KTIVerticalSpacer(4.dp)
                                KTITextNew(text = currentQuestion?.answer ?: "Current question is null")
                            }
                        }
                    }
                    ControlSection(
                        addPointClick = onAddPointClick,
                        noPointClick = onNoPointClick,
                        modifier = Modifier.weight(1f),
                        inputEnabled = inputEnabled,
                        showAnswerClick = { isAnswerExpanded.value = !isAnswerExpanded.value },
                        isAnswerExpanded = isAnswerExpanded.value,
                    )
                }
            }

            is InterviewCuratedScreenModel.ViewStateChat.InterviewFinished -> {
                KTITextNew("no questions left", fontSize = 16.sp, fontWeight = FontWeight.W700)
            }
        }
    }
}

private val radius = 24.dp

@Composable
private fun LazyItemScope.InterviewerBubbleChatItem(chatItem: InterviewChatItemUiModel.InterviewerMessage) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        KTIHorizontalSpacer(44.dp)
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topEnd = radius,
                        topStart = radius,
                        bottomStart = radius,
                        bottomEnd = 0.dp,
                    )
                )
                .background(kti_dark_blue)
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            when (chatItem) {
                is InterviewChatItemUiModel.InterviewerMessage.OtherMessage -> {
                    KTITextNew(
                        text = chatItem.message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        color = kti_softwhite,
                    )
                }

                is InterviewChatItemUiModel.InterviewerMessage.QuestionAsked -> {
                    KTITextNew(
                        text = chatItem.question.question,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        color = kti_softwhite,
                    )
                }

                InterviewChatItemUiModel.InterviewerMessage.Writing -> {
                    KTITextNew("...")
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.CandidateBubbleChatItem(chatItem: InterviewChatItemUiModel.CandidateMessage) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topEnd = radius,
                        topStart = radius,
                        bottomStart = 0.dp,
                        bottomEnd = radius,
                    )
                )
                .background(kti_light_blue)
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            when (chatItem) {
                InterviewChatItemUiModel.CandidateMessage.BadAnswer -> {
                    KTITextNew(
                        text = "I don't know. :(",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400
                    )
                }

                InterviewChatItemUiModel.CandidateMessage.GoodAnswer -> {
                    KTITextNew(
                        text = "Sure, I know!",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400
                    )
                }

                InterviewChatItemUiModel.CandidateMessage.Writing -> {
                    KTITextNew("...")
                }
            }
        }
        KTIHorizontalSpacer(44.dp)
    }
}

@Composable
private fun ColumnScope.ControlSection(
    addPointClick: () -> Unit,
    noPointClick: () -> Unit,
    showAnswerClick: () -> Unit,
    inputEnabled: Boolean,
    isAnswerExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier then Modifier.fillMaxWidth().background(kti_softwhite)) {
        Divider(color = kti_grey, thickness = 1.5.dp)
        AnimatedVisibility(visible = inputEnabled) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KTIButtonShared(
                    label = "I knew it!",
                    onClick = addPointClick,
                    icon = SharedRes.images.check_new,
                    iconColor = kti_green,
                    enabled = inputEnabled,
                    backgroundColor = kti_softwhite,
                )
                KTIButtonShared(
                    label = "I was confused :(",
                    onClick = noPointClick,
                    icon = SharedRes.images.cross_new,
                    iconColor = kti_red,
                    enabled = inputEnabled,
                    backgroundColor = kti_softwhite,
                )
                KTIButtonShared(
                    label = if (isAnswerExpanded.not()) "Show answer" else "Hide answer",
                    onClick = showAnswerClick,
                    icon = if (isAnswerExpanded.not()) SharedRes.images.arrow_up else SharedRes.images.arrow_down,
                    iconColor = if (isAnswerExpanded.not()) kti_softblack else kti_softwhite,
                    enabled = inputEnabled,
                    backgroundColor = if (isAnswerExpanded.not()) kti_softwhite else kti_accent,
                    labelColor = if (isAnswerExpanded.not()) kti_softblack else kti_softwhite
                )
            }
        }

    }
}