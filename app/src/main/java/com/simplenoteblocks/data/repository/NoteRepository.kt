package com.simplenoteblocks.data.repository

import com.simplenoteblocks.data.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getAllNotesOfNoteBlock(noteBlockId: Int): Flow<List<Note>>
    fun getNote(id: Int): Flow<Note>
}