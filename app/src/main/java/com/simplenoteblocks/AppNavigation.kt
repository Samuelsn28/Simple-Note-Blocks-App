package com.simplenoteblocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.simplenoteblocks.data.Note
import com.simplenoteblocks.data.deserializeJsonStringToNote
import com.simplenoteblocks.data.deserializeJsonStringToNoteBlock
import com.simplenoteblocks.ui.screens.settings.SettingsScreen
import com.simplenoteblocks.ui.screens.home.HomeScreen
import com.simplenoteblocks.ui.screens.noteblock.NoteBlockScreen
import com.simplenoteblocks.ui.screens.note.NoteScreen
import com.simplenoteblocks.ui.screens.note.NoteScreenStates

object NavigationParameters {
    const val KEY_NOTE_BLOCK_DATAS = "noteBlockDatas"
    const val NOTE_BLOCK_DATAS = "{$KEY_NOTE_BLOCK_DATAS}"
    const val KEY_NOTE_DATAS = "noteDatas"
    const val NOTE_DATAS = "{$KEY_NOTE_DATAS}"
    const val KEY_STATE = "state"
    const val STATE = "{$KEY_STATE}"
}

enum class AppScreens(val route: String) {
    Home("home"),
    NoteBlock("noteBlock/${NavigationParameters.NOTE_BLOCK_DATAS}"),
    EditNote("editNote/${NavigationParameters.STATE}" +
                           "/${NavigationParameters.NOTE_BLOCK_DATAS}" +
                           "/${NavigationParameters.NOTE_DATAS}"),
    Settings("settings")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Home.route
    ) {
        composable(route = AppScreens.Home.route) { backStackEntry ->
            HomeRoute(
                navController = navController,
                backStackEntry = backStackEntry,
                modifier = modifier
            )
        }
        composable(
            route = AppScreens.NoteBlock.route,
            arguments = listOf(
                navArgument(NavigationParameters.KEY_NOTE_BLOCK_DATAS) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            NoteBlockRoute(
                navController = navController,
                backStackEntry = backStackEntry,
                modifier = modifier
            )
        }
        composable(
            route = AppScreens.EditNote.route,
            arguments = listOf(
                navArgument(NavigationParameters.KEY_STATE) { type = NavType.StringType },
                navArgument(NavigationParameters.KEY_NOTE_BLOCK_DATAS) { type = NavType.StringType },
                navArgument(NavigationParameters.KEY_NOTE_DATAS) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            NoteRoute(
                navController = navController,
                backStackEntry = backStackEntry,
                modifier = modifier
            )
        }
        composable(
            route = AppScreens.Settings.route,
        ) { backStackEntry ->
            SettingsRoute(
                navController = navController,
                modifier = modifier
            )
        }
    }
}

@Composable
fun HomeRoute(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier
) {
    HomeScreen(
        navController = navController,
        modifier = modifier
    )
}

@Composable
fun NoteBlockRoute(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier
) {
    val noteBlockDatas = backStackEntry
        .arguments?.getString(NavigationParameters.KEY_NOTE_BLOCK_DATAS) as String
    val noteBlock = deserializeJsonStringToNoteBlock(noteBlockDatas)

    NoteBlockScreen(
        navController = navController,
        noteBlock = noteBlock,
        modifier = modifier
    )
}

@Composable
fun NoteRoute(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier
) {
    val state = backStackEntry
        .arguments?.getString(NavigationParameters.KEY_STATE) as String

    val noteBlockDatas = backStackEntry
        .arguments?.getString(NavigationParameters.KEY_NOTE_BLOCK_DATAS) as String
    val noteBlock = deserializeJsonStringToNoteBlock(noteBlockDatas)

    var note: Note? = null
    if (state == NoteScreenStates.Edit.name) {
        val noteDatas = backStackEntry
            .arguments?.getString(NavigationParameters.KEY_NOTE_DATAS) as String
        note = deserializeJsonStringToNote(noteDatas)
    }

    NoteScreen(
        navController = navController,
        noteBlock = noteBlock,
        note = note,
        modifier = modifier
    )
}

@Composable
fun SettingsRoute(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    SettingsScreen(
        navController = navController,
        modifier = modifier
    )
}