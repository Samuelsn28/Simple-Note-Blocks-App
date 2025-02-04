package com.simplenoteblocks.data

import android.content.Context
import com.simplenoteblocks.data.repository.OfflineNoteBlockRepository
import com.simplenoteblocks.data.repository.OfflineNoteRepository

object Graph {
    private lateinit var db: AppDatabase

    val noteBlockRepository by lazy {
        OfflineNoteBlockRepository(
            dao = db.noteBlockDao()
        )
    }

    val noteRepository by lazy {
        OfflineNoteRepository(
            dao = db.noteDao()
        )
    }

    fun provideDatabase(context: Context) {
        db = AppDatabase.getDatabase(context)
    }
}