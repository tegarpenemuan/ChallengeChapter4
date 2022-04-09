package com.tegarpenemuan.challengechapter4.data.local.dao

import androidx.room.*
import com.tegarpenemuan.challengechapter4.data.local.entity.NoteEntity

@Dao
interface NoteDAO {

    @Query("SELECT * FROM note")
    fun getNotes(): List<NoteEntity>

    @Query("SELECT * FROM note WHERE id=:noteId")
    fun getNote(noteId: Int): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteEntity: NoteEntity): Long

    @Update
    fun updateNote(noteEntity: NoteEntity): Int

    @Delete
    fun deleteNote(noteEntity: NoteEntity): Int
}