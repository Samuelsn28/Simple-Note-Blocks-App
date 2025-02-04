package com.simplenoteblocks.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.simplenoteblocks.ui.components.TextFieldTypes

@Entity(tableName = "note_blocks")
data class NoteBlock(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: NoteBlockType
)

fun serializeNoteBlockToJsonString(noteBlock: NoteBlock): String {
    return Uri.encode(Gson().toJson(noteBlock))
}

fun deserializeJsonStringToNoteBlock(jsonString: String): NoteBlock {
    return Gson().fromJson(jsonString, NoteBlock::class.java)
}

enum class NoteBlockType {
    Text {
        override fun getTextFieldTypeToCopyContent(): TextFieldTypes {
            return TextFieldTypes.Content
        }
    },
    Url {
        override fun getTextFieldTypeToCopyContent(): TextFieldTypes {
            return TextFieldTypes.Url
        }
    },
    Secret {
        override fun getTextFieldTypeToCopyContent(): TextFieldTypes {
            return TextFieldTypes.Secret
        }
    };

    abstract fun getTextFieldTypeToCopyContent(): TextFieldTypes
}
