package com.example.week06b.model

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.example.week06b.R
import java.util.Scanner

class VocDataViewModel(private val application: Application) : AndroidViewModel(application) {

    var vocList = mutableStateListOf<VocData>()
        private set

    // 초기화 블록
    init {
         vocList.addAll(readWordFile())  // readWordFile() 함수가 읽어온 모든 단어를 vocList에 추가
    }

    private fun readWordFile():MutableList<VocData>{
        val context = application.applicationContext
        val scan = Scanner(context.resources.openRawResource(R.raw.words))
        val wordList = mutableListOf<VocData>()
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            wordList.add(VocData(word, meaning))
        }
        scan.close()
        return wordList
    }

    fun changeOpenStatus(index:Int){
        vocList[index] = vocList[index].copy(isOpen = !vocList[index].isOpen)
    }
}