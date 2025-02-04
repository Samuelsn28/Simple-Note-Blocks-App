package com.simplenoteblocks.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import com.simplenoteblocks.ui.theme.DarkFontColor
import com.simplenoteblocks.ui.theme.DefaultFontFamily
import com.simplenoteblocks.ui.theme.Medium
import com.simplenoteblocks.ui.theme.PrimaryColor
import com.simplenoteblocks.ui.theme.TertiaryColor

enum class TextFieldTypes {
    Content,
    Description,
    Secret,
    Title,
    Url;
}

fun getTextFieldType(name: String): TextFieldTypes? {
    return TextFieldTypes.entries.find { textFieldType ->
        name == textFieldType.name
    }
}

@Composable
fun InputField(
    currentText: MutableState<String>,
    placeHolder: String,
    imeAction: ImeAction,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = currentText.value,
        onValueChange = { currentText.value = it },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        placeholder = {
            Text(
                text = placeHolder,
                fontSize = Medium,
                fontFamily = DefaultFontFamily
            )
        },
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedPlaceholderColor = PrimaryColor,
            focusedPlaceholderColor = PrimaryColor,
            unfocusedBorderColor = TertiaryColor,
            focusedBorderColor = TertiaryColor,
            cursorColor = DarkFontColor
        ),
        modifier = Modifier.fillMaxWidth()
    )
}