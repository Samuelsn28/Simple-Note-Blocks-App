package com.simplenoteblocks.ui.screens.noteblock

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.simplenoteblocks.AppScreens
import com.simplenoteblocks.NavigationParameters
import com.simplenoteblocks.R
import com.simplenoteblocks.data.Note
import com.simplenoteblocks.data.NoteBlock
import com.simplenoteblocks.data.NoteBlockType
import com.simplenoteblocks.data.deserializeNoteTextFieldDatasJsonToMap
import com.simplenoteblocks.data.serializeNoteBlockToJsonString
import com.simplenoteblocks.data.serializeNoteToJsonString
import com.simplenoteblocks.ui.components.TextFieldTypes
import com.simplenoteblocks.ui.components.TopBar
import com.simplenoteblocks.ui.screens.note.NoteScreenStates
import com.simplenoteblocks.ui.theme.DarkFontColor
import com.simplenoteblocks.ui.theme.DefaultFontFamily
import com.simplenoteblocks.ui.theme.DefaultSemiBoldFontFamily
import com.simplenoteblocks.ui.theme.Large
import com.simplenoteblocks.ui.theme.LightFontColor
import com.simplenoteblocks.ui.theme.Medium
import com.simplenoteblocks.ui.theme.PrimaryColor
import com.simplenoteblocks.ui.theme.SecondaryColor
import com.simplenoteblocks.ui.theme.TertiaryColor
import com.simplenoteblocks.ui.theme.WhiteColor

@Composable
fun NoteBlockScreen(
    viewModel: NoteBlockViewModel = viewModel(),
    navController: NavHostController,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryColor)
    ) {
        TopBar(
            backgroundColor = SecondaryColor,
            rightSide = {
                NoteBlockScreenRightSideTopBar(
                    noteBlockName = noteBlock.name,
                    navController = navController
                )
            },
            leftSide = { NoteBlockScreenLeftSideTopBar(
                navController = navController,
                noteBlock = noteBlock
            ) }
        )
        NoteBlockScreenBody(
            viewModel = viewModel,
            uiState = uiState,
            navController = navController,
            noteBlock = noteBlock,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
    }
}

@Composable
fun NoteBlockScreenRightSideTopBar(
    navController: NavHostController,
    noteBlockName: String,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxHeight()
    ) {
        IconButton(
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Go Back",
                    tint = WhiteColor
                )
            },
            onClick = {
                navController.navigate(AppScreens.Home.route)
            },
            modifier = Modifier
                .padding(horizontal = 5.dp)
        )
        Text(
            text = noteBlockName,
            fontSize = Large,
            fontFamily = DefaultSemiBoldFontFamily,
            color = LightFontColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NoteBlockScreenLeftSideTopBar(
    navController: NavHostController,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    IconButton(
        content = {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Create Note",
                tint = WhiteColor
            )
        },
        onClick = {
            navController.navigate(
                AppScreens.EditNote.route
                    .replace(
                        oldValue = NavigationParameters.STATE,
                        newValue = NoteScreenStates.Create.name
                    )
                    .replace(
                        oldValue = NavigationParameters.NOTE_BLOCK_DATAS,
                        newValue = serializeNoteBlockToJsonString(noteBlock)
                    )
                    .replace(
                        oldValue = NavigationParameters.NOTE_DATAS,
                        newValue = "empty"
                    )
            )
        },
        modifier = Modifier.padding(end = 12.dp)
    )
}

@Composable
fun NoteBlockScreenBody(
    viewModel: NoteBlockViewModel,
    uiState: NoteBlockUiState,
    navController: NavHostController,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    NotesList(
        viewModel = viewModel,
        uiState = uiState,
        navController = navController,
        noteBlock = noteBlock,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun NotesList(
    viewModel: NoteBlockViewModel,
    uiState: NoteBlockUiState,
    navController: NavHostController,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    viewModel.getAllNotes(noteBlock.id)

    val notes = uiState.noteList
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp),
        modifier = modifier
    ) {
        items(notes) { note ->
            NotesListItem(
                navController = navController,
                clipboardManager = clipboardManager,
                note = note,
                noteBlock = noteBlock,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun NotesListItem(
    navController: NavHostController,
    clipboardManager: ClipboardManager,
    note: Note,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        border = BorderStroke(1.dp, TertiaryColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(WhiteColor, shape = RoundedCornerShape(15.dp))
    ) {
        NotesListItemContent(
            navController = navController,
            clipboardManager = clipboardManager,
            note = note,
            noteBlock = noteBlock
        )
    }
}

@Composable
fun NotesListItemContent(
    navController: NavHostController,
    clipboardManager: ClipboardManager,
    note: Note,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 10.dp)
    ) {
        Text(
            text = note.name,
            fontSize = Medium,
            fontFamily = DefaultFontFamily,
            color = DarkFontColor,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(end = 15.dp)
                .weight(1f)
        )
        NoteListItemActions(
            navController = navController,
            clipboardManager = clipboardManager,
            note = note,
            noteBlock = noteBlock
        )
    }
}

@Composable
fun NoteListItemActions(
    navController: NavHostController,
    clipboardManager: ClipboardManager,
    note: Note,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    fun getValueOfTextField(textFieldType: TextFieldTypes): String {
        val map = deserializeNoteTextFieldDatasJsonToMap(note.noteTextFieldDatasJson)

        return map[textFieldType.name] ?: "Error: data not found."
    }

    val context = LocalContext.current
    NoteListItemActionButton(
        imageVector = ImageVector.vectorResource(R.drawable.ic_copy),
        contentDescription = "Copy",
        onClick = {
            val contentToCopy = getValueOfTextField(noteBlock.type.getTextFieldTypeToCopyContent())
            clipboardManager.setText(AnnotatedString(contentToCopy))
            Toast.makeText(context, "Copied to Clipboard!", Toast.LENGTH_SHORT).show()
        }
    )

    val uriHandler = LocalUriHandler.current
    if (noteBlock.type == NoteBlockType.Url) {
        NoteListItemActionButton(
            imageVector = ImageVector.vectorResource(R.drawable.ic_explore),
            contentDescription = "Open URL",
            onClick = {
                try {
                    uriHandler.openUri(getValueOfTextField(TextFieldTypes.Url))
                } catch(_: IllegalArgumentException) {
                    Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    NoteListItemActionButton(
        imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
        contentDescription = "Edit",
        onClick = {
            navController.navigate(AppScreens.EditNote.route
                .replace(
                    oldValue = NavigationParameters.STATE,
                    newValue = NoteScreenStates.Edit.name
                )
                .replace(
                    oldValue = NavigationParameters.NOTE_BLOCK_DATAS,
                    newValue = serializeNoteBlockToJsonString(noteBlock)
                ).replace(
                    oldValue = NavigationParameters.NOTE_DATAS,
                    newValue = serializeNoteToJsonString(note)
                ))
        }
    )
}

@Composable
fun NoteListItemActionButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        content = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = TertiaryColor
            )
        },
        onClick = { onClick() },
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = WhiteColor
        )
    )
}
