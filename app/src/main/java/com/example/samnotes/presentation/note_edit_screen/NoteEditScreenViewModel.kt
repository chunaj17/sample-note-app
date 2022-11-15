package com.example.samnotes.presentation.note_edit_screen

import androidx.lifecycle.*
import com.example.samnotes.features.domain.model.Note
import com.example.samnotes.features.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NoteEditScreenViewModel
@Inject
constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _state = MutableLiveData<NoteEditScreenState>()
    val state: LiveData<NoteEditScreenState> = _state

    private var noteId: Int? = savedStateHandle.get<Int>("noteId")

    private var insertTitleState: Long? = null

    init {
        if (noteId == -1) {
            // you create a n note id
            val noteState = NoteEditScreenState(0, "Enter Title", "Enter content")
            _state.value = noteState
        } else {
            //get from db
            viewModelScope.launch(Dispatchers.IO) {
                val noteData = noteUseCases.getSingleNote(noteId!!)
                this.launch(Dispatchers.Main) {

                _state.value = NoteEditScreenState(
                    id = noteData?.id,
                    title = noteData?.title,
                    content = noteData?.content
                )
                }
            }
        }
    }

    fun handleNoteEvent(
        noteEvent: NoteEvent
    ) = viewModelScope.launch {
        when (noteEvent) {
            is NoteEvent.UpdateNoteTitle -> {
                _state.value = state.value?.copy(
                    title = noteEvent.titleEntered
                )
                if (
                    noteId == -1
                    && state.value!!.title!!.isNotBlank()
                    && state.value!!.content!!.isNotBlank()
                ) {
                    val randomId = randomIdNum()
                    insertNote(randomId, title = state.value?.title, content = state.value?.content)
                    noteId = randomId
                } else {
                    noteUseCases.updateNote(
                        Note(
                            id = noteId,
                            title = state.value?.title,
                            content = state.value?.content
                        )
                    )
                }
            }
            is NoteEvent.UpdateNoteContent -> {
                _state.value = state.value?.copy(
                    content = noteEvent.contentEntered
                )
                if (
                    noteId == -1
                    && state.value!!.title!!.isNotBlank()
                    && state.value!!.content!!.isNotBlank()
                ) {
                    val randomId = randomIdNum()
                    insertNote(randomId, title = state.value?.title, content = state.value?.content)
                    noteId = randomId
                } else {
                    noteUseCases.updateNote(
                        Note(
                            id = noteId,
                            title = state.value?.title,
                            content = state.value?.content
                        )
                    )
                }
            }

        }

    }

    private fun insertNote(id: Int?, title: String?, content: String?) = viewModelScope.launch {
        val rowId = noteUseCases.insertNote(
            Note(
                id = id,
                title = title,
                content = content
            )
        )
        insertTitleState = rowId
    }

    private fun randomIdNum(): Int {
        return Random.nextInt(100000, 999999)
    }
}