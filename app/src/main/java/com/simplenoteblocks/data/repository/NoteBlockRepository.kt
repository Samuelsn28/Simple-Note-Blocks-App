package com.simplenoteblocks.data.repository

import com.simplenoteblocks.data.NoteBlock
import kotlinx.coroutines.flow.Flow

interface NoteBlockRepository {
    suspend fun insertNoteBlock(noteBlock: NoteBlock)
    suspend fun updateNoteBlock(noteBlock: NoteBlock)
    suspend fun deleteNoteBlock(noteBlock: NoteBlock)
    fun getAllNoteBlocks(): Flow<List<NoteBlock>>
    fun getNoteBlock(id: Int): Flow<NoteBlock>
}