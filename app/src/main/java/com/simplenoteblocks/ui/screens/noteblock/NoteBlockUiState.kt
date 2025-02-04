package com.simplenoteblocks.ui.screens.noteblock

import com.simplenoteblocks.data.Note

data class NoteBlockUiState (
    val noteList: List<Note> = emptyList()
)