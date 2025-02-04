package com.simplenoteblocks.data

import android.net.Uri
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = NoteBlock::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("note_block_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "note_block_id")
    val noteBlockId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "note_datas_json")
    val noteTextFieldDatasJson: String
)

fun serializeNoteToJsonString(note: Note): String {
    return Uri.encode(Gson().toJson(note))
}

fun deserializeJsonStringToNote(jsonString: String): Note {
    return Gson().fromJson(jsonString, Note::class.java)
}

fun serializeMapToNoteTextFieldDatasJson(
    map: Map<String, String>
): String {
    return Uri.encode(Gson().toJson(map))
}

fun deserializeNoteTextFieldDatasJsonToMap(
    noteDatasJson: String
): Map<String, String> {
    val gson = GsonBuilder().create()
    val typeOfMap = object : TypeToken<Map<String, String>>() {}.type

    return gson.fromJson(Uri.decode(noteDatasJson), typeOfMap)
}