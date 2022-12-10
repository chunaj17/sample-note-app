package com.example.samnotes.presentation.note_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.samnotes.presentation.note_edit_screen.component.SelectionModeItems
import com.example.samnotes.presentation.note_screen.component.NoteItem
import com.example.samnotes.presentation.note_screen.component.SelectItemIcon

@Composable
fun NotesScreen(
    viewModel: NoteScreenViewModel = hiltViewModel(),
    onNavigateToNoteEditScreen:(Int?) -> Unit,
) {

    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.getNotes()
        })
    val noteScreenState by viewModel.state.observeAsState()

    if (noteScreenState?.data?.isEmpty()!!) {
        viewModel.noteScreenEvent(
            NoteScreenEvent.LongPressClicked(
                clickable = true
            )
        )
    }
    BackHandler {
        viewModel.noteScreenEvent(
            NoteScreenEvent.LongPressClicked(
                clickable = true
            )
        )
        viewModel.noteScreenEvent(NoteScreenEvent.BackPressed)

    }
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToNoteEditScreen(null)
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        floatingActionButtonPosition = FabPosition.End, content = { paddingValues ->
            Column {
                Text(
                    text = "Notes",
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(4.dp),
                    fontSize = 32.sp,
                    color = MaterialTheme.colors.primary
                )
                if (!noteScreenState?.clicked!! && noteScreenState!!.data.isNotEmpty()) {
                    SelectionModeItems(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End),
                        count = noteScreenState?.selectedNoteId?.size ?: 0
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    /* contentPadding = PaddingValues(
                         start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
                     ),*/ content = {
                        items(items = noteScreenState?.data ?: listOf()) { item ->
                            if (item.noteId != null) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 4.dp, start = 4.dp, bottom = 8.dp),
                                    content = {
                                        NoteItem(
                                            modifier = Modifier
                                                .fillMaxWidth(1f)
                                                .height(100.dp)
                                                .padding(8.dp),
                                            title = item.title?.take(20),
                                            content = item.content?.take(40),
                                            id = item.noteId,
                                            viewModel = viewModel,
                                            click = { noteId ->
                                                viewModel.noteScreenEvent(
                                                    NoteScreenEvent.SelectOrDeselectNote(
                                                        noteId
                                                    )
                                                )
                                            },
                                            longPress = { noteId ->
                                                viewModel.noteScreenEvent(
                                                    NoteScreenEvent.LongPressClicked(
                                                        clickable = false
                                                    )
                                                )
                                                viewModel.noteScreenEvent(
                                                    NoteScreenEvent.SelectNote(noteId)
                                                )
                                            },
                                            onNavigateToNoteEditScreen = { noteId ->
                                                onNavigateToNoteEditScreen(noteId)
                                            }
                                        )

                                        if (noteScreenState?.selectedNoteId?.contains(item.noteId)!!) {
                                            SelectItemIcon(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    })

            }
        })


}