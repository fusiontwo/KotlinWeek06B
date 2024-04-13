package com.example.week06b.component

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week06b.model.VocData
import com.example.week06b.model.VocDataViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocList3(vocDataViewModel: VocDataViewModel= viewModel()) {  // viewModel의 dependency를 임포트 해야 함.

    val context = LocalContext.current
    var ttsReady by rememberSaveable {
        mutableStateOf(false)
    }

    var tts : TextToSpeech? by rememberSaveable {
        mutableStateOf(null)
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        tts = TextToSpeech(context){status ->
            if(status == TextToSpeech.SUCCESS){  // 성공적으로 서비스를 사용할 준비가 된 경우
                ttsReady = true
                tts!!.language = Locale.US
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val speakWord = {vocData:VocData ->
        if(ttsReady){
            tts?.speak(vocData.word, TextToSpeech.QUEUE_ADD, null, null)
        }
    }

    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showButton by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 0
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = state) {
            itemsIndexed(
                vocDataViewModel.vocList,
                key = { _, voc -> voc.word }) { index: Int, item: VocData ->
                val state = rememberDismissState(  // @OptIn(ExperimentalMaterial3Api::class) 추가 필요
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            vocDataViewModel.vocList.remove(item)
                            true
                        } else
                            false
                    }
                )

                SwipeToDismiss(state = state,
                    background = {
                        val color = when (state.dismissDirection) {
                            DismissDirection.EndToStart -> Color.LightGray
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                                .background(color)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Icon",
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    },
                    dismissContent = {// 어디에 swipe 기능을 적용할 것인지
                        VocItem(vocData = item) {
                            speakWord(item)
                            vocDataViewModel.changeOpenStatus(index)
                        }
                    })

            }
        }

        AnimatedVisibility(visible = showButton) {
            ScrollToTopButton {
                scope.launch {
                    state.scrollToItem(0)
                }
            }
        }
    }
}