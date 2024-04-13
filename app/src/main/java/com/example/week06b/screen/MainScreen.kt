package com.example.week06b.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.week06b.R
import com.example.week06b.component.MainTitle
import com.example.week06b.component.VocList3

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // 단어장 만들기
    Text(text = stringResource(id = R.string.student_info))
    Column {
        MainTitle()
//        VocList()
//        VocList2()
        VocList3()
    }
}