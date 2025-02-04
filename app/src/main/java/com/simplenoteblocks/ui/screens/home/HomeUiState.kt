package com.simplenoteblocks.ui.screens.home

import com.simplenoteblocks.data.NoteBlock

data class HomeUiState(
    val noteBlockList: List<NoteBlock> = emptyList()
)