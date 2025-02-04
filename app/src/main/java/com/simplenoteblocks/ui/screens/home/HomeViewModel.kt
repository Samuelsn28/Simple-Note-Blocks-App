package com.simplenoteblocks.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplenoteblocks.data.Graph
import com.simplenoteblocks.data.NoteBlock
import com.simplenoteblocks.data.NoteBlockType
import com.simplenoteblocks.data.repository.OfflineNoteBlockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val noteBlockRepository: OfflineNoteBlockRepository = Graph.noteBlockRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    var currentNewNoteBlockName by mutableStateOf("")
        private set
    var currentEditNoteBlockName by mutableStateOf("")
        private set
    var currentSelectedRadioButton by mutableStateOf(NoteBlockType.Text)
        private set
    var isOpenedCreateDialog by mutableStateOf(false)
        private set
    var isOpenedEditDialog by mutableStateOf(false)
        private set
    var noteBlockToEdit by mutableStateOf(NoteBlock(0, "null", NoteBlockType.Text))
        private set

    fun getAllNoteBlocks() {
        viewModelScope.launch {
            noteBlockRepository.getAllNoteBlocks().collectLatest { noteBlockList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        noteBlockList = noteBlockList
                    )
                }
            }
        }
    }

    fun addNoteBlock(noteBlock: NoteBlock) {
        viewModelScope.launch {
            noteBlockRepository.insertNoteBlock(noteBlock)
        }
    }

    fun updateNoteBlock(updatedNoteBlock: NoteBlock) {
        viewModelScope.launch {
            noteBlockRepository.updateNoteBlock(updatedNoteBlock)
        }
    }

    fun deleteNoteBlock(noteBlockToDelete: NoteBlock){
        viewModelScope.launch {
            noteBlockRepository.deleteNoteBlock(noteBlockToDelete)
        }
    }

    fun onCurrentNewNoteBlockNameUpdate(newName: String){
        currentNewNoteBlockName = newName
    }

    fun onCurrentEditNoteBlockNameUpdate(newName: String) {
        currentEditNoteBlockName = newName
    }

    fun updateCurrentSelectedRadioButton(newSelected: NoteBlockType) {
        currentSelectedRadioButton = newSelected
    }

    fun openCreateDialog() {
        isOpenedCreateDialog = true
    }

    fun closeCreateDialog() {
        isOpenedCreateDialog = false
    }

    fun openEditDialog() {
        isOpenedEditDialog = true
    }

    fun closeEditDialog() {
        isOpenedEditDialog = false
    }

    fun setNewNoteBlockToEdit(newNoteBlockToEdit: NoteBlock) {
        noteBlockToEdit = newNoteBlockToEdit
    }
}