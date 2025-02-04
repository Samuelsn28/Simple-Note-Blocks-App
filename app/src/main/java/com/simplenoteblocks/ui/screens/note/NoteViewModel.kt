package com.simplenoteblocks.ui.screens.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplenoteblocks.data.Graph
import com.simplenoteblocks.data.Note
import com.simplenoteblocks.data.NoteBlock
import com.simplenoteblocks.data.NoteBlockType
import com.simplenoteblocks.data.repository.OfflineNoteRepository
import com.simplenoteblocks.data.serializeMapToNoteTextFieldDatasJson
import com.simplenoteblocks.ui.components.TextFieldTypes
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteRepository: OfflineNoteRepository = Graph.noteRepository
) : ViewModel() {
    val currentContentInputText = mutableStateOf("")
    val currentDescriptionInputText = mutableStateOf("")
    val currentSecretInputText = mutableStateOf("")
    val currentTitleInputText = mutableStateOf("")
    val currentUrlInputText = mutableStateOf("")

    var isSecretTextVisible by mutableStateOf(false)
        private set

    private fun encapsulateInputTextsInListPair(
        noteBlockType: NoteBlockType
    ): Map<String, String> {
        val map = HashMap<String, String>()

        when(noteBlockType) {
            NoteBlockType.Text -> {
                map[TextFieldTypes.Title.name] = currentTitleInputText.value
                map[TextFieldTypes.Content.name] = currentContentInputText.value
            }
            NoteBlockType.Url -> {
                map[TextFieldTypes.Title.name] = currentTitleInputText.value
                map[TextFieldTypes.Url.name] = currentUrlInputText.value
                map[TextFieldTypes.Description.name] = currentDescriptionInputText.value
            }
            NoteBlockType.Secret -> {
                map[TextFieldTypes.Title.name] = currentTitleInputText.value
                map[TextFieldTypes.Secret.name] = currentSecretInputText.value
                map[TextFieldTypes.Description.name] = currentDescriptionInputText.value
            }
        }
        return map
    }

    fun updateCurrentContentInputText(newContent: String) {
        currentContentInputText.value = newContent
    }

    fun updateCurrentDescriptionInputText(newDescription: String) {
        currentDescriptionInputText.value = newDescription
    }

    fun updateCurrentSecretInputText(newSecret: String) {
        currentSecretInputText.value = newSecret
    }

    fun updateCurrentTitleInputText(newTitle: String) {
        currentTitleInputText.value = newTitle
    }

    fun updateCurrentUrlInputText(newUrl: String) {
        currentUrlInputText.value = newUrl
    }

    fun openSecretTextVisibility(){
        isSecretTextVisible = true
    }

    fun closeSecretTextVisibility() {
        isSecretTextVisible = false
    }

    fun insertNewNote(noteBlock: NoteBlock) {
        val newNote = Note(
            id = 0,
            noteBlockId = noteBlock.id,
            name = currentTitleInputText.value,
            noteTextFieldDatasJson = serializeMapToNoteTextFieldDatasJson(
                encapsulateInputTextsInListPair(noteBlock.type)
            )
        )
        viewModelScope.launch {
            noteRepository.insertNote(newNote)
        }
    }

    fun updateNote(noteId: Int, noteBlock: NoteBlock) {
        val updatedNote = Note(
            id = noteId,
            noteBlockId = noteBlock.id,
            name = currentTitleInputText.value,
            noteTextFieldDatasJson = serializeMapToNoteTextFieldDatasJson(
                encapsulateInputTextsInListPair(noteBlock.type)
            )
        )
        viewModelScope.launch {
            noteRepository.updateNote(updatedNote)
        }
    }
}