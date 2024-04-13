package com.example.week06b.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week06b.model.VocData
import com.example.week06b.model.VocDataViewModel

@Composable
fun VocList(vocDataViewModel: VocDataViewModel= viewModel()) {  // viewModel의 dependency를 임포트 해야 함.

    LazyColumn {
        itemsIndexed(vocDataViewModel.vocList){index: Int, item: VocData ->
            VocItem(vocData = item){
                vocDataViewModel.changeOpenStatus(index)
            }
        }
    }
}