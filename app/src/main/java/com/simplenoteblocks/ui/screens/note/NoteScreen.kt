package com.simplenoteblocks.ui.screens.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.simplenoteblocks.ui.components.InputField
import com.simplenoteblocks.ui.components.TextFieldTypes
import com.simplenoteblocks.ui.components.TopBar
import com.simplenoteblocks.ui.components.getTextFieldType
import com.simplenoteblocks.ui.theme.PrimaryColor
import com.simplenoteblocks.ui.theme.SecondaryColor
import com.simplenoteblocks.ui.theme.TertiaryColor
import com.simplenoteblocks.ui.theme.WhiteColor

enum class NoteScreenStates {
    Create,
    Edit
}

@Composable
fun NoteScreen(
    viewModel: NoteViewModel = viewModel(),
    navController: NavHostController,
    noteBlock: NoteBlock,
    note: Note?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryColor)
    ) {
        TopBar(
            backgroundColor = SecondaryColor,
            rightSide = {
                NoteScreenRightSideTopBar(
                    navController = navController,
                    noteBlock = noteBlock
                )
            },
            leftSide = {
                NoteScreenLeftSideTopBar(
                    viewModel = viewModel,
                    navController = navController,
                    screenState = if (note == null) NoteScreenStates.Create else NoteScreenStates.Edit,
                    note = note,
                    noteBlock = noteBlock
                )
            }
        )
        NoteScreenContent(
            viewModel = viewModel,
            note = note,
            noteBlockType = noteBlock.type
        )
    }
}

@Composable
fun NoteScreenRightSideTopBar(
    navController: NavHostController,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    IconButton(
        content = {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.cancel),
                tint = WhiteColor
            )
        },
        onClick = {
            navController.navigate(AppScreens.NoteBlock.route
                .replace(
                    oldValue = NavigationParameters.NOTE_BLOCK_DATAS,
                    newValue = serializeNoteBlockToJsonString(noteBlock)
                )
            )
        },
        modifier = Modifier
            .padding(horizontal = 5.dp)
    )
}

@Composable
fun NoteScreenLeftSideTopBar(
    viewModel: NoteViewModel,
    navController: NavHostController,
    screenState: NoteScreenStates,
    note: Note?,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    IconButton(
        content = {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "Confirm",
                tint = WhiteColor
            )
        },
        onClick = {
            if (screenState == NoteScreenStates.Create) {
                viewModel.insertNewNote(noteBlock)
            }
            if (screenState == NoteScreenStates.Edit) {
                viewModel.updateNote((note as Note).id, noteBlock)
            }
            navController.navigate(AppScreens.NoteBlock.route
                .replace(
                    oldValue = NavigationParameters.NOTE_BLOCK_DATAS,
                    newValue = serializeNoteBlockToJsonString(noteBlock)
                )
            )
        },
        modifier = Modifier
            .padding(horizontal = 10.dp)
    )
}

@Composable
fun NoteScreenContent(
    viewModel: NoteViewModel,
    note: Note?,
    noteBlockType: NoteBlockType,
    modifier: Modifier = Modifier
) {
    if (note != null) {
        FillNoteContentFields(
            viewModel = viewModel,
            note = note
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = 10.dp)
    ) {
        when (noteBlockType) {
            NoteBlockType.Text -> TextTypeFields(viewModel = viewModel)
            NoteBlockType.Url -> UrlTypeFields(viewModel = viewModel)
            NoteBlockType.Secret -> SecretTypeFields(viewModel = viewModel)
        }
    }
}

@Composable
fun FillNoteContentFields(
    viewModel: NoteViewModel,
    note: Note
) {
    val map = deserializeNoteTextFieldDatasJsonToMap(note.noteTextFieldDatasJson)

    map.entries.forEach { entry ->
        val fieldName = entry.key
        val content = entry.value

        FillField(
            viewModel = viewModel,
            textFieldType = getTextFieldType(fieldName) as TextFieldTypes,
            content = content
        )
    }
}

@Composable
fun FillField(
    viewModel: NoteViewModel,
    textFieldType: TextFieldTypes,
    content: String,
    modifier: Modifier = Modifier
) {
    when (textFieldType) {
        TextFieldTypes.Content -> {
            viewModel.updateCurrentContentInputText(content)
        }
        TextFieldTypes.Description -> {
            viewModel.updateCurrentDescriptionInputText(content)
        }
        TextFieldTypes.Secret -> {
            viewModel.updateCurrentSecretInputText(content)
        }
        TextFieldTypes.Title -> {
            viewModel.updateCurrentTitleInputText(content)
        }
        TextFieldTypes.Url -> {
            viewModel.updateCurrentUrlInputText(content)
        }
    }
}

@Composable
fun TextTypeFields(
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier
) {
    InputField(
        currentText = viewModel.currentTitleInputText,
        placeHolder = TextFieldTypes.Title.name,
        imeAction = ImeAction.Next
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputField(
        currentText = viewModel.currentContentInputText,
        placeHolder = TextFieldTypes.Content.name,
        imeAction = ImeAction.Done
    )
}

@Composable
fun UrlTypeFields(
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier
) {
    InputField(
        currentText = viewModel.currentTitleInputText,
        placeHolder = TextFieldTypes.Title.name,
        imeAction = ImeAction.Next
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputField(
        currentText = viewModel.currentUrlInputText,
        placeHolder = TextFieldTypes.Url.name.uppercase(),
        imeAction = ImeAction.Next
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputField(
        currentText = viewModel.currentDescriptionInputText,
        placeHolder = TextFieldTypes.Description.name,
        imeAction = ImeAction.Done
    )
}

@Composable
fun SecretTypeFields(
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier
) {
    InputField(
        currentText = viewModel.currentTitleInputText,
        placeHolder = TextFieldTypes.Title.name,
        imeAction = ImeAction.Next
    )
    Spacer(modifier = Modifier.height(10.dp))

    InputField(
        currentText = viewModel.currentSecretInputText,
        placeHolder = TextFieldTypes.Secret.name,
        imeAction = ImeAction.Next,
        visualTransformation = if (viewModel.isSecretTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            SecretInputFieldTrailingIcon(
                viewModel = viewModel
            )
        }
    )
    Spacer(modifier = Modifier.height(10.dp))

    InputField(
        currentText = viewModel.currentDescriptionInputText,
        placeHolder = TextFieldTypes.Description.name,
        imeAction = ImeAction.Done
    )
}

@Composable
fun SecretInputFieldTrailingIcon(
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier
) {
    val iconImage: ImageVector
    val contentDescription: String

    if (viewModel.isSecretTextVisible) {
        iconImage = ImageVector.vectorResource(R.drawable.ic_visibility)
        contentDescription = "Hide secret"
    } else {
        iconImage = ImageVector.vectorResource(R.drawable.ic_visibility_off)
        contentDescription = "Show secret"
    }

    IconButton(
        onClick = {
            if (viewModel.isSecretTextVisible) {
                viewModel.closeSecretTextVisibility()
            } else {
                viewModel.openSecretTextVisibility()
            }
        },
        content = {
            Icon(
                imageVector = iconImage,
                contentDescription = contentDescription,
                tint = TertiaryColor,
            )
        },
        modifier = Modifier.padding(end = 5.dp)
    )
}


