package com.simplenoteblocks.data.repository

import com.simplenoteblocks.data.NoteBlock
import com.simplenoteblocks.data.dao.NoteBlockDao
import kotlinx.coroutines.flow.Flow

class OfflineNoteBlockRepository(
    private val dao: NoteBlockDao
) : NoteBlockRepository {

    override suspend fun insertNoteBlock(noteBlock: NoteBlock) {
        dao.insert(noteBlock)
    }

    override suspend fun updateNoteBlock(noteBlock: NoteBlock) {
        dao.update(noteBlock)
    }

    override suspend fun deleteNoteBlock(noteBlock: NoteBlock) {
        dao.delete(noteBlock)
    }

    override fun getAllNoteBlocks(): Flow<List<NoteBlock>> {
        return dao.getAllNoteBlocks()
    }

    override fun getNoteBlock(id: Int): Flow<NoteBlock> {
        return dao.getNoteBlock(id)
    }
}