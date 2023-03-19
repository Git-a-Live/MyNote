package com.gitalive.composenote.ui.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gitalive.composenote.entities.NoteItemEntity

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteDetail(
    note: NoteItemEntity,
    turnBack: () -> Unit,
    saveTitle: (String) -> Unit,
    saveContent: (String) -> Unit
) {
    Scaffold(
        topBar = { CustomTopBar(turnBack) },
        bottomBar = { NoteDetailTimeInfo(note.createTime, note.editTime) },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White),
        drawerGesturesEnabled = false
    ) {
        Column {
            NoteDetailTitle(note.title, saveTitle)
            NoteDetailContent(note.content, saveContent)
        }
    }
}

@Composable
internal fun NoteDetailTimeInfo(create: String = "", edit: String = "") {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp, 5.dp)
    ) {
        Text(
            text = "创建于：$create",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraLight,
            textAlign = TextAlign.Start
        )
        Text(
            text = "编辑于：$edit",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraLight,
            textAlign = TextAlign.End
        )
    }
}

@Composable
internal fun NoteDetailContent(content: String = "", saveContent: (String) -> Unit = {}) {
    var newContent by remember {
        mutableStateOf(content)
    }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .onFocusChanged {
                if (!it.isFocused) {
                    saveContent(newContent)
                }
            },
        textStyle = TextStyle.Default.copy(
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Start
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = newContent,
        onValueChange = {
            newContent = it
        })
}

@Composable
internal fun NoteDetailTitle(title: String = "", saveContent: (String) -> Unit = {}) {
    var newTitle by remember {
        mutableStateOf(title)
    }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .wrapContentHeight(align = Alignment.Top)
            .onFocusChanged {
                if (!it.isFocused) {
                    saveContent(newTitle)
                }
            },
        textStyle = TextStyle.Default.copy(
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = newTitle,
        onValueChange = {
            newTitle = it
        })
}

@Composable
internal fun CustomTopBar(turnBack: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = "详情") },
        navigationIcon = {
            IconButton(onClick = {
                turnBack()
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun NoteDetailPreview() {
    Scaffold(
        topBar = { CustomTopBar() },
        bottomBar = { NoteDetailTimeInfo("2023.03.19 15:15:37", "2023.03.19 15:15:37") },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White),
        drawerGesturesEnabled = false
    ) {
        Column {
            NoteDetailTitle("This is a title")
            NoteDetailContent(
                "This is note content.This is note content." +
                        "This is note content.This is note content.This is note content." +
                        "This is note content.This is note content.This is note content." +
                        "This is note content.This is note content.This is note content." +
                        "This is note content.This is note content.This is note content." +
                        "This is note content.This is note content.This is note content." +
                        "This is note content.This is note content.This is note content." +
                        "This is note content.This is note content.This is note content."
            )
        }
    }
}