package com.gitalive.composenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.gitalive.composenote.entities.NoteItemEntity
import com.gitalive.composenote.ui.widget.NoteDetail
import com.gitalive.composenote.ui.widget.SaveAlertDialog

class NoteActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]

        setContent {
            NoteDetail(
                note = viewModel.currentNoteState,
                turnBack = { onBackPressed() },
                saveTitle = { viewModel.saveCurrentTitle(it) },
                saveContent = { viewModel.saveCurrentContent(it) }
            )
            if (viewModel.openDialog) {
                SaveAlertDialog(
                    onDismissRequest = {
                        viewModel.shouldOpenDialog(false)
                        finish()
                    },
                    onConfirmed = {
                        saveCurrentNote()
                    }
                )
            }
        }
    }

    override fun onResume() {
        intent.getLongExtra("noteId", 0L).apply {
            if (this != 0L) {
                viewModel.getCurrentNote(this)
            }
        }
        super.onResume()
    }

    override fun onBackPressed() {
        viewModel.shouldOpenDialog(true)
    }

    private fun saveCurrentNote() {
        val action = {
            viewModel.shouldOpenDialog(false)
            finish()
        }
        intent.getLongExtra("noteId", 0L).apply {
            if (this == 0L) {
                viewModel.saveCurrentNote(NoteItemEntity(), action)
            } else {
                viewModel.currentNoteState.let { viewModel.saveCurrentNote(it, action) }
            }
        }
    }
}