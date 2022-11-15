package com.example.samnotes.presentation.note_edit_screen


sealed class NoteEvent{
    data class UpdateNoteTitle(val titleEntered:String = "Enter title"): NoteEvent()
    data class UpdateNoteContent(val contentEntered:String = "Enter Content" ): NoteEvent()
}