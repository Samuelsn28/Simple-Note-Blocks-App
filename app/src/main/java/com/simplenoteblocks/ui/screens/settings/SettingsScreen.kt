package com.simplenoteblocks.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.simplenoteblocks.AppScreens
import com.simplenoteblocks.R
import com.simplenoteblocks.ui.components.TopBar
import com.simplenoteblocks.ui.theme.DarkFontColor
import com.simplenoteblocks.ui.theme.Large
import com.simplenoteblocks.ui.theme.LightFontColor
import com.simplenoteblocks.ui.theme.PrimaryColor
import com.simplenoteblocks.ui.theme.SecondaryColor
import com.simplenoteblocks.ui.theme.WhiteColor
import com.simplenoteblocks.ui.theme.Medium
import com.simplenoteblocks.ui.theme.Small

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
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
            rightSide = {
                SettingsScreenRightSideTopBar(
                    navController = navController
                )
            },
            leftSide = {}
        )
        SettingsScreenBody(
            viewModel = viewModel
        )
    }
}

@Composable
fun SettingsScreenRightSideTopBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    IconButton(
        content = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.go_back),
                tint = WhiteColor
            )
        },
        onClick = {
            navController.navigate(AppScreens.Home.route)
        },
        modifier = Modifier
            .padding(horizontal = 5.dp)
    )
}

@Composable
fun SettingsScreenBody(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    ChooseLanguageOption(
        viewModel = viewModel
    )
    AppVersion()
}

@Composable
fun ChooseLanguageOption(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    SetLanguageDialog(
        viewModel = viewModel
    )
    Button(
        onClick = {
           viewModel.openSetLanguageDialog()
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Language:",
                color = LightFontColor,
                fontSize = Medium
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Portuguese",
                color = LightFontColor,
                fontSize = Medium
            )
        }
    }
}

@Composable
fun AppVersion(
    modifier: Modifier = Modifier
) {
    HorizontalDivider()
    Text(
        text = "Simple Note Blocks v0.3 Alpha",
        color = LightFontColor,
        fontSize = Small,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 5.dp)
    )
}

@Composable
fun SetLanguageDialog(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    if (viewModel.isOpenSetLanguageDialog) {
        Dialog(onDismissRequest = { viewModel.closeSetLanguageDialog() }) {
            Card(
                shape = RoundedCornerShape(15.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                modifier = Modifier
            ) {
                SetLanguageDialogBody(
                    viewModel = viewModel
                )
            }
        }
    }
}

enum class Languages {
    English {
        override fun getLocaleTag(): String {
            return "en"
        }
    },
    Portuguese {
        override fun getLocaleTag(): String {
            return "pt-BR"
        }
    },
    Spanish {
        override fun getLocaleTag(): String {
            return "es"
        }
    };

    abstract fun getLocaleTag(): String
}

@Composable
fun SetLanguageDialogBody(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(Languages.entries.toTypedArray()) { language ->
            LanguageItem(
                viewModel = viewModel,
                language = language
            )
        }
    }
}

@Composable
fun LanguageItem(
    viewModel: SettingsViewModel,
    language: Languages,
    modifier: Modifier = Modifier
) {
    Card (
        onClick = {
            viewModel.closeSetLanguageDialog()
            viewModel.updateChosenLanguage(language.getLocaleTag())
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Transparent)
            .padding(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = 12.5.dp)
        ) {
            Text(
                text = language.name,
                color = DarkFontColor,
                fontSize = Large,
            )
            if (viewModel.chosenLanguageTag == language.getLocaleTag()) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Selected",
                    tint = DarkFontColor
                )
            }
        }
    }
}