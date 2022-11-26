﻿package com.example.samnotes.features.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.samnotes.features.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM noteentity")
    fun getNotes(): List<NoteEntity>

    @Query("SELECT * FROM noteentity WHERE noteId = :noteId")
    fun getSingleNote(noteId: Int): NoteEntity

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM noteentity WHERE noteId = :noteId")
    fun deleteNoteId(noteId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity):Long

    @Update
    suspend fun updateNote(note:NoteEntity)

    @Query("SELECT noteId from noteentity WHERE :rowId")
    suspend fun getIdFromRow(rowId: Long):Int
}