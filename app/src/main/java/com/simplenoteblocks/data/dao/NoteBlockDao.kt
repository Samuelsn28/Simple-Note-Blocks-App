package com.simplenoteblocks.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.simplenoteblocks.data.NoteBlock
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteBlockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteBlock: NoteBlock)

    @Update
    suspend fun update(noteBlock: NoteBlock)

    @Delete
    suspend fun delete(noteBlock: NoteBlock)

    @Query("SELECT * from note_blocks ORDER BY name ASC")
    fun getAllNoteBlocks(): Flow<List<NoteBlock>>

    @Query("SELECT * from note_blocks WHERE id = :id")
    fun getNoteBlock(id: Int): Flow<NoteBlock>
}