package com.example.magneto.kotlindemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.magneto.kotlindemo.bean.Word
import com.example.magneto.kotlindemo.inteface.WordRoomDatabase
import com.example.magneto.kotlindemo.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


public class WordViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext : CoroutineContext
        get()=parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository : WordRepository
    val allWords : LiveData<List<Word>>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application,scope).wordDao()
        repository= WordRepository(wordsDao)
        allWords= repository.allWords
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(word: Word) = scope.launch(Dispatchers.IO){
        repository.insert(word)
    }
}