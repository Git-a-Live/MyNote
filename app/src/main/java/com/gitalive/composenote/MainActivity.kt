package com.gitalive.composenote

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.gitalive.composenote.ui.widget.Notes
import com.gitalive.composenote.utils.DatabaseRepository

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]

        setContent {
            Notes(
                list = viewModel.noteListState,
                onFABClicked = {
                    goToNoteActivity()
                },
                onClicked = {
                    goToNoteActivity(it.id)
                },
                onLongClicked = {
                    viewModel.deleteNote(it.id)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAllNotes()
    }

    override fun onDestroy() {
        DatabaseRepository.deInitObjectBox()
        super.onDestroy()
    }

    private fun goToNoteActivity(entityId: Long = 0L) {
        startActivity(Intent(this, NoteActivity::class.java).apply {
            if (entityId != 0L) {
                putExtra("noteId", entityId)
            }
        })
    }
}