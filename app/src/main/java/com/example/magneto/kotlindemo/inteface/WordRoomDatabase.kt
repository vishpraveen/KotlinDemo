package com.example.magneto.kotlindemo.inteface

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.magneto.kotlindemo.bean.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.Internal.instance

@Database(entities = [Word::class],version = 1)
public abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao() : WordDao

    companion object {
        @Volatile
        private var INSTANCE : WordRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope : CoroutineScope):WordRoomDatabase{
            val tempInstance = INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "Word_Database"
                ).addCallback(WordDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class WordDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO){
                    populateDatabase(database.wordDao())
                }
            }
        }

        private fun populateDatabase(wordDao: WordDao) {
            wordDao.deleteAll()

            var word =Word("Hello")
            wordDao.insert(word)
            word = Word("Welcome")
            wordDao.insert(word)
        }
    }
}