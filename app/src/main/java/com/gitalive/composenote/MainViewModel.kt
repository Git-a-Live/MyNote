package com.gitalive.composenote

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gitalive.composenote.entities.NoteItemEntity
import com.gitalive.composenote.entities.NoteItemEntity_
import com.gitalive.composenote.utils.DatabaseRepository
import com.gitalive.composenote.utils.DatabaseRepository.deleteByIds
import com.gitalive.composenote.utils.DatabaseRepository.getAllEntities
import com.gitalive.composenote.utils.DatabaseRepository.insertOrReplace
import com.gitalive.composenote.utils.DatabaseRepository.queryEntityWithConditions
import com.gitalive.composenote.utils.getFormattedDateTime
import com.gitalive.composenote.utils.register
import com.orhanobut.logger.Logger
import io.objectbox.Box
import kotlinx.coroutines.delay

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val noteItemBox: Box<NoteItemEntity>? by lazy { DatabaseRepository.getBox(NoteItemEntity::class.java) }
    var noteListState by mutableStateOf(listOf<NoteItemEntity?>())
        private set
    var currentNoteState: NoteItemEntity by mutableStateOf(NoteItemEntity())
        private set
    var openDialog by mutableStateOf(false)
        private set

    fun deleteNote(id: Long) {
        viewModelScope.register("deleteNote") {
            noteItemBox?.deleteByIds(id)
            noteListState = noteItemBox?.getAllEntities() ?: emptyList()
        }
    }

    fun shouldOpenDialog(shouldOpen: Boolean) {
        openDialog = shouldOpen
    }

    fun getCurrentNote(id: Long) {
        viewModelScope.register("getNote") {
            currentNoteState = noteItemBox?.queryEntityWithConditions({
                it.equal(NoteItemEntity_.id, id)
            }) ?: NoteItemEntity()
        }
    }

    fun saveCurrentNote(note: NoteItemEntity, action: () -> Unit = {}) {
        viewModelScope.register("saveNote") {
            Logger.e("当前事项ID = ${note.id}")
            Logger.e("开始保存内容")
            if (note.createTime.isEmpty()) {
                note.createTime = getFormattedDateTime("yyyy.MM.dd HH:mm:ss")
            }
            note.editTime = getFormattedDateTime("yyyy.MM.dd HH:mm:ss")
            Logger.e("当前事项 = $note")
            currentNoteState = note
            noteItemBox?.insertOrReplace(note)
            delay(1000)
            action()
        }
    }

    fun saveCurrentContent(content: String) {
        currentNoteState.content = content
        Logger.e("保存当前事项内容 = ${currentNoteState.content}")
        saveCurrentNote(currentNoteState)
    }

    fun saveCurrentTitle(title: String) {
        currentNoteState.title = title
        Logger.e("保存当前事项标题 = ${currentNoteState.title}")
        saveCurrentNote(currentNoteState)
    }

    fun fetchAllNotes() {
        viewModelScope.register("allNotes") {
            noteListState = noteItemBox?.getAllEntities() ?: emptyList()
        }
    }
}