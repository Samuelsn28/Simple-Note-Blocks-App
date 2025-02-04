package com.simplenoteblocks.ui.screens.noteblock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplenoteblocks.data.Graph
import com.simplenoteblocks.data.repository.OfflineNoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteBlockViewModel(
    private val noteRepository: OfflineNoteRepository = Graph.noteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteBlockUiState())
    val uiState: StateFlow<NoteBlockUiState> = _uiState.asStateFlow()

    fun getAllNotes(noteBlockId: Int) {
        viewModelScope.launch {
            noteRepository.getAllNotesOfNoteBlock(noteBlockId).collectLatest { receivedNoteList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        noteList = receivedNoteList
                    )
                }
            }
        }
    }
}