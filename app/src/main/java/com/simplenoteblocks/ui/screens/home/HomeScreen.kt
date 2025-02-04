package com.simplenoteblocks.ui.screens.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.simplenoteblocks.AppScreens
import com.simplenoteblocks.NavigationParameters
import com.simplenoteblocks.R
import com.simplenoteblocks.data.NoteBlock
import com.simplenoteblocks.data.NoteBlockType
import com.simplenoteblocks.data.serializeNoteBlockToJsonString
import com.simplenoteblocks.ui.components.TopBar
import com.simplenoteblocks.ui.theme.DarkFontColor
import com.simplenoteblocks.ui.theme.DefaultSemiBoldFontFamily
import com.simplenoteblocks.ui.theme.DefaultFontFamily
import com.simplenoteblocks.ui.theme.Large
import com.simplenoteblocks.ui.theme.LightFontColor
import com.simplenoteblocks.ui.theme.Medium
import com.simplenoteblocks.ui.theme.PrimaryColor
import com.simplenoteblocks.ui.theme.PrimaryVariationColor
import com.simplenoteblocks.ui.theme.RedFontColor
import com.simplenoteblocks.ui.theme.SecondaryColor
import com.simplenoteblocks.ui.theme.TertiaryColor
import com.simplenoteblocks.ui.theme.WhiteColor

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val homeUiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            HomeScreenFab(homeViewModel = homeViewModel)
        }
    ) { innerPadding ->
        HomeScreenRoot(
            homeViewModel = homeViewModel,
            homeUiState = homeUiState,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeScreenRoot(
    homeViewModel: HomeViewModel,
    homeUiState: HomeUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryColor)
    ) {
        TopBar(
            backgroundColor = SecondaryColor,
            rightSide = { HomeScreenRightSideTopBar() },
            leftSide = { HomeScreenLeftSideTopBar() }
        )
        HomeScreenContent(
            homeViewModel = homeViewModel,
            homeUiState = homeUiState,
            navController = navController,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
    }
}

@Composable
fun HomeScreenFab(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    if (homeViewModel.isOpenedCreateDialog) {
        OpenDialogAddBlockNotes(
            homeViewModel = homeViewModel,
            onConfirmation = { newNoteBlock ->
                homeViewModel.addNoteBlock(noteBlock = newNoteBlock)
                homeViewModel.closeCreateDialog()
            }
        )
    }
    FloatingActionButton(
        containerColor = PrimaryVariationColor,
        contentColor = WhiteColor,
        onClick = {
            homeViewModel.openCreateDialog()
        }
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = stringResource(R.string.add_note_block_icon_description)
        )
    }
}

@Composable
fun OpenDialogAddBlockNotes(
    homeViewModel: HomeViewModel,
    onConfirmation: (NoteBlock) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = { homeViewModel.isOpenedCreateDialog }) {
        homeViewModel.onCurrentNewNoteBlockNameUpdate("")
        NewBlockNotesDialog(
            homeViewModel = homeViewModel,
            onConfirmation = onConfirmation,
            onCancel = {
                homeViewModel.closeCreateDialog()
            }
        )
    }
}

@Composable
fun NewBlockNotesDialog(
    homeViewModel: HomeViewModel,
    onConfirmation: (NoteBlock) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
    ) {
        NewBlockNotesDialogContent(
            homeViewModel = homeViewModel,
            onConfirmation = onConfirmation,
            onCancel = onCancel
        )
    }
}

@Composable
fun NewBlockNotesDialogContent(
    homeViewModel: HomeViewModel,
    onConfirmation: (NoteBlock) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(400.dp)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp)
    ) {
        Text(
            text = stringResource(R.string.create_note_block_dialog_title),
            fontSize = Large,
            fontFamily = DefaultFontFamily,
            color = DarkFontColor
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = homeViewModel.currentNewNoteBlockName,
            onValueChange = { homeViewModel.onCurrentNewNoteBlockNameUpdate(it) },
            singleLine = true,
            label = {
                Text(
                    text = stringResource(R.string.create_note_block_dialog_label_name),
                    fontSize = Medium,
                    fontFamily = DefaultFontFamily,
                    color = DarkFontColor
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        TypeSelector(
            homeViewModel = homeViewModel
        )
        DialogBottomButtons(
            homeViewModel = homeViewModel,
            onConfirmation = onConfirmation,
            onCancel = onCancel
        )
    }
}

@Composable
fun TypeSelector(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.create_note_block_dialog_field_name_type) + ":",
        fontSize = Large,
        fontFamily = DefaultFontFamily,
        color = DarkFontColor,
        modifier = Modifier.padding(vertical = 10.dp)
    )
    Column {
        DialogTypeRadioButton(
            homeViewModel = homeViewModel,
            selectedRadioButton = homeViewModel.currentSelectedRadioButton,
            type = NoteBlockType.Text
        )
        DialogTypeRadioButton(
            homeViewModel = homeViewModel,
            selectedRadioButton = homeViewModel.currentSelectedRadioButton,
            type = NoteBlockType.Url
        )
        DialogTypeRadioButton(
            homeViewModel = homeViewModel,
            selectedRadioButton = homeViewModel.currentSelectedRadioButton,
            type = NoteBlockType.Secret
        )
    }
}

@Composable
fun DialogTypeRadioButton(
    homeViewModel: HomeViewModel,
    selectedRadioButton: NoteBlockType,
    type: NoteBlockType,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedRadioButton == type,
            onClick = { homeViewModel.updateCurrentSelectedRadioButton(type) }
        )
        Text(
            text = type.name,
            fontSize = Medium,
            fontFamily = DefaultFontFamily,
            color = DarkFontColor
        )
    }
}

@Composable
fun DialogBottomButtons(
    homeViewModel: HomeViewModel,
    onConfirmation: (NoteBlock) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        OutlinedButton(
            content = {
                Text(
                    text = stringResource(R.string.cancel),
                    fontSize = Large,
                    fontFamily = DefaultFontFamily,
                    color = RedFontColor
                )
            },
            onClick = onCancel,
            shape = RoundedCornerShape(5.dp)
        )
        Spacer(Modifier.weight(1f))
        OutlinedButton(
            content = {
                Text(
                    text = stringResource(R.string.create),
                    fontSize = Large,
                    fontFamily = DefaultFontFamily,
                    color = DarkFontColor
                )
            },
            onClick = {
                onConfirmation(
                    NoteBlock(
                        id = 0,
                        name = homeViewModel.currentNewNoteBlockName,
                        type = homeViewModel.currentSelectedRadioButton
                    )
                )
            },
            shape = RoundedCornerShape(5.dp)
        )
    }
}

@Composable
fun EditNoteBlockDialog(
    homeViewModel: HomeViewModel,
    onConfirmation: (NoteBlock) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = { homeViewModel.closeEditDialog() }
    ) {
        homeViewModel.onCurrentEditNoteBlockNameUpdate(homeViewModel.noteBlockToEdit.name)
        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
        ) {
            EditNoteBlockDialogContent(
                homeViewModel = homeViewModel,
                onConfirmation = onConfirmation
            )
        }
    }
}

@Composable
fun EditNoteBlockDialogContent(
    homeViewModel: HomeViewModel,
    onConfirmation: (NoteBlock) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(400.dp)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp)
    ) {
        Text(
            text = stringResource(R.string.edit_note_block_dialog_title),
            fontSize = Large,
            fontFamily = DefaultFontFamily,
            color = DarkFontColor
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = homeViewModel.currentEditNoteBlockName,
            onValueChange = { homeViewModel.onCurrentEditNoteBlockNameUpdate(it) },
            singleLine = true,
            label = {
                Text(
                    text = stringResource(R.string.edit_note_block_dialog_label_name),
                    fontSize = Medium,
                    fontFamily = DefaultFontFamily,
                    color = DarkFontColor
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        EditNoteBlockDialogBottomButtons(
            homeViewModel = homeViewModel,
            onConfirmation = onConfirmation
        )
    }
}

@Composable
fun EditNoteBlockDialogBottomButtons(
    homeViewModel: HomeViewModel,
    onConfirmation: (NoteBlock) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        OutlinedButton(
            content = {
                Text(
                    text = stringResource(R.string.cancel),
                    fontSize = Large,
                    fontFamily = DefaultFontFamily,
                    color = RedFontColor
                )
            },
            onClick = {
                homeViewModel.closeEditDialog()
            },
            shape = RoundedCornerShape(5.dp)
        )
        Spacer(Modifier.weight(1f))
        OutlinedButton(
            content = {
                Text(
                    text = stringResource(R.string.confirm),
                    fontSize = Large,
                    fontFamily = DefaultFontFamily,
                    color = DarkFontColor
                )
            },
            onClick = {
                val updatedNoteBlock = NoteBlock(
                    id = homeViewModel.noteBlockToEdit.id,
                    name = homeViewModel.currentEditNoteBlockName,
                    type = homeViewModel.noteBlockToEdit.type
                )
                onConfirmation(updatedNoteBlock)
            },
            shape = RoundedCornerShape(5.dp)
        )
    }
}

@Composable
fun HomeScreenRightSideTopBar(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.app_name),
        fontSize = Large,
        fontFamily = DefaultSemiBoldFontFamily,
        color = LightFontColor,
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
fun HomeScreenLeftSideTopBar(modifier: Modifier = Modifier) {
    IconButton(
        modifier = Modifier.padding(end = 12.dp),
        content = {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = stringResource(R.string.settings),
                tint = WhiteColor,
                modifier = Modifier
            )
        },
        onClick = {}
    )
}

@Composable
fun HomeScreenContent(
    homeViewModel: HomeViewModel,
    homeUiState: HomeUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    homeViewModel.getAllNoteBlocks()

    Column(
        modifier = modifier
    ) {
        NoteBlocksList(
            homeViewModel = homeViewModel,
            navController = navController,
            items = homeUiState.noteBlockList,
            modifier = modifier
        )
    }
}

@Composable
fun NoteBlocksList(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    items: List<NoteBlock>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items) { noteBlock ->
            NoteBlockItem(
                homeViewModel = homeViewModel,
                navController = navController,
                noteBlock = noteBlock,
                modifier = Modifier
            )
        }
    }
    OpenEditDialog(homeViewModel = homeViewModel)
}

@Composable
fun OpenEditDialog(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    if (homeViewModel.isOpenedEditDialog) {
        Log.i("Aqui", "")
        EditNoteBlockDialog(
            homeViewModel = homeViewModel,
            onConfirmation = { updatedNoteBlock ->
                homeViewModel.updateNoteBlock(updatedNoteBlock)
                homeViewModel.closeEditDialog()
            }
        )
    }
}

@Composable
fun NoteBlockItem(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = {
            navController.navigate(
                AppScreens.NoteBlock.route
                    .replace(
                        oldValue = NavigationParameters.NOTE_BLOCK_DATAS,
                        newValue = serializeNoteBlockToJsonString(noteBlock)
                    )
            )
        },
        border = BorderStroke(1.dp, TertiaryColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(WhiteColor, shape = RoundedCornerShape(15.dp))
    ) {
        NoteBlockItemContent(
            homeViewModel = homeViewModel,
            noteBlock = noteBlock,
            modifier = Modifier
        )
    }
}

@Composable
fun NoteBlockItemContent(
    homeViewModel: HomeViewModel,
    noteBlock: NoteBlock,
    modifier: Modifier = Modifier
) {
    val isOpenDropDownOptions = rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = noteBlock.name,
            fontSize = Large,
            fontFamily = DefaultFontFamily,
            color = DarkFontColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 20.dp, end = 10.dp)
                .weight(1f)
        )
        IconButton(
            content = {
                IconButtonContent(
                    homeViewModel = homeViewModel,
                    noteBlock = noteBlock,
                    isOpenDropDownOptions = isOpenDropDownOptions
                )
            },
            onClick = {
                isOpenDropDownOptions.value = true
            }
        )
    }
}

@Composable
fun IconButtonContent(
    homeViewModel: HomeViewModel,
    noteBlock: NoteBlock,
    isOpenDropDownOptions: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Rounded.MoreVert,
        contentDescription = stringResource(R.string.options)
    )
    NoteBlockOptionsDropDown(
        isOpenDropDownOptions = isOpenDropDownOptions,
        options = getDropDownOptions(
            homeViewModel = homeViewModel,
            noteBlock = noteBlock,
            isOpenDropDownOptions = isOpenDropDownOptions
        )
    )
}

@Composable
fun getDropDownOptions(
    homeViewModel: HomeViewModel,
    noteBlock: NoteBlock,
    isOpenDropDownOptions: MutableState<Boolean>
): List<Triple<Pair<String, String>, ImageVector, () -> Unit>> {
    return listOf(
        Triple(
            first = Pair(
                stringResource(R.string.note_block_item_dropdown_option_edit),
                stringResource(R.string.note_block_item_dropdown_option_edit_description)
            ),
            second = Icons.Rounded.Edit,
            third = {
                homeViewModel.setNewNoteBlockToEdit(noteBlock)
                homeViewModel.openEditDialog()
                isOpenDropDownOptions.value = false
            }
        ),
        Triple(
            first = Pair(
                stringResource(R.string.note_block_item_dropdown_option_delete),
                stringResource(R.string.note_block_item_dropdown_option_delete_description)
            ),
            second = Icons.Rounded.Delete,
            third = {
                homeViewModel.deleteNoteBlock(noteBlock)
                isOpenDropDownOptions.value = false
            }
        )
    )
}

@Composable
fun NoteBlockOptionsDropDown(
    isOpenDropDownOptions: MutableState<Boolean>,
    options: List<Triple<Pair<String, String>, ImageVector, () -> Unit>>,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = isOpenDropDownOptions.value,
        onDismissRequest = {
            isOpenDropDownOptions.value = false
        },
        content = {
            options.forEach { option ->
                ShowDropDownOption(option = option)
            }
        }
    )
}

@Composable                           //  Title, Description |  Icon  | onClickListener
fun ShowDropDownOption(option: Triple<Pair<String, String>, ImageVector, () -> Unit>) {
    DropdownMenuItem(
        text = {
            Text(
                text = option.first.first,
                fontSize = Medium,
                fontFamily = DefaultFontFamily,
                color = DarkFontColor,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = option.second,
                contentDescription = option.first.second
            )
        },
        onClick = { option.third() }
    )
}