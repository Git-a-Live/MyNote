package com.gitalive.composenote.ui.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gitalive.composenote.entities.NoteItemEntity
import com.gitalive.composenote.utils.getFormattedDateTime

@Composable
fun Notes(
    list: List<NoteItemEntity?>,
    onFABClicked: () -> Unit = {},
    onClicked: (NoteItemEntity) -> Unit = {},
    onLongClicked: (NoteItemEntity) -> Unit = {}
) {
    CustomScaffold(onFABClicked = onFABClicked) {
        NoteList(list = list, onClicked, onLongClicked)
    }
}

@Composable
internal fun CustomFloatingActionButton(onClicked: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClicked
    ) {
        Icon(Icons.Filled.AddCircle, contentDescription = null)
    }
}

@Composable
internal fun NoteList(
    list: List<NoteItemEntity?>,
    onClicked: (NoteItemEntity) -> Unit = {},
    onLongClicked: (NoteItemEntity) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        items(list) {
            it?.let { entity ->
                NoteItem(
                    note = entity,
                    onClicked = onClicked,
                    onLongClicked = onLongClicked
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun CustomScaffold(onFABClicked: () -> Unit = {}, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Compose记事本") }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        floatingActionButton = { CustomFloatingActionButton(onFABClicked) },
        drawerGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun NoteListPreview() {
    val list = arrayListOf<NoteItemEntity>()
    repeat(10) {
        list.add(
            NoteItemEntity(
                title = "这是事项${it + 1}",
                content = "芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！" +
                        "芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！" +
                        "芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！" +
                        "芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！" +
                        "芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！芝士雪豹！",
                createTime = getFormattedDateTime("yyyy-MM-dd HH:mm:ss"),
                editTime = getFormattedDateTime("yyyy-MM-dd HH:mm:ss")
            )
        )
    }
    CustomScaffold {
        NoteList(list = list)
    }
}