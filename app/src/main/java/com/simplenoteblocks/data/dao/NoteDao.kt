package com.simplenoteblocks.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.simplenoteblocks.data.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from notes WHERE note_block_id = :noteBlockId ORDER BY name ASC")
    fun getAllINotesOfNoteBlock(noteBlockId: Int): Flow<List<Note>>

    @Query("SELECT * from notes WHERE id = :id")
    fun getNote(id: Int): Flow<Note>
}