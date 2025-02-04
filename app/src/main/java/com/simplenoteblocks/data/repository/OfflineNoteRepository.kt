package com.simplenoteblocks.data.repository

import com.simplenoteblocks.data.Note
import com.simplenoteblocks.data.dao.NoteDao
import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(
    private val dao: NoteDao
) : NoteRepository {

    override suspend fun insertNote(note: Note) {
        dao.insert(note)
    }

    override suspend fun updateNote(note: Note) {
        dao.update(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.delete(note)
    }

    override fun getAllNotesOfNoteBlock(noteBlockId: Int): Flow<List<Note>> {
        return dao.getAllINotesOfNoteBlock(noteBlockId)
    }

    override fun getNote(id: Int): Flow<Note> {
        return dao.getNote(id)
    }
}