package com.example.magneto.kotlindemo.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.magneto.kotlindemo.bean.Word
import com.example.magneto.kotlindemo.inteface.WordDao

class WordRepository(private val wordDao: WordDao) {
    val allWords : LiveData<List<Word>> =wordDao.getAllWords()

    @WorkerThread
    suspend fun insert(word: Word){
        wordDao.insert(word)
    }
}