package com.example.samnotes.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.samnotes.presentation.camera_view.presentation.CameraView
import com.example.samnotes.presentation.note_edit_screen.NoteEditScreen
import com.example.samnotes.presentation.note_screen.NotesScreen
import java.io.File
import java.util.concurrent.Executor

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    outPutDirectory: File,
    executor: Executor,
) {
    NavHost(navController = navController, startDestination = Screen.NotesScreen.route) {
        composable(
            route = Screen.NotesScreen.route
        ) {
            NotesScreen(
                onNavigateToNOteEditScreenWithParam = { noteId ->
                    navController.navigate(
                        Screen.NotesEditScreen.route + "?noteId=$noteId"
                    )
                },
                onNavigateToNoteEditScreenWithoutParam = {
                    navController.navigate(
                        Screen.NotesEditScreen.route
                    )
                }
            )
        }
        composable(
            route = Screen.NotesEditScreen.route + "?noteId={noteId}&photoUri={photoUri}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "photoUri"
                ) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            NoteEditScreen(
                onNavigateToCamera = { noteId ->
                    navController.navigate(
                        Screen.CameraView.route + "?noteId=$noteId"
                    ) {
                        popUpTo(Screen.CameraView.route)
                    }
                },
                onNavigateToNoteScreen = {
                    navController.navigate(
                        Screen.NotesScreen.route
                    )
                }
            )
        }
        composable(
            route = Screen.CameraView.route + "?noteId={noteId}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            CameraView(
                onNavCameraToNoteEditScreen = { noteId, photoUri ->
                    navController.navigate(
                        Screen.NotesEditScreen.route +
                                "?noteId=${noteId}&photoUri=${photoUri}"
                    ) {
                        popUpTo(Screen.CameraView.route) {
                            inclusive = true
                        }
                    }
                },
                outPutDirectory = outPutDirectory,
                executor = executor,
                onError = { imageCaptureException ->
                    Log.e("sam", "CameraViewError", imageCaptureException)
                })
        }

    }
}