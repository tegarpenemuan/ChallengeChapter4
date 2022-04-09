package com.tegarpenemuan.challengechapter4.data.local.entity

import android.os.Parcelable
import android.text.Editable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "note")
@Parcelize
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "titleNote") var titleNote: String,
    @ColumnInfo(name = "destNote") var descNote: String
) : Parcelable