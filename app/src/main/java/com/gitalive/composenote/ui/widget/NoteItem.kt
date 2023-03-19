package com.gitalive.composenote.ui.widget

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gitalive.composenote.R
import com.gitalive.composenote.entities.NoteItemEntity

@Composable
fun NoteItem(
    note: NoteItemEntity,
    onClicked: (NoteItemEntity) -> Unit = {},
    onLongClicked: (NoteItemEntity) -> Unit = {},
) {
    CustomSurface(
        onClicked = { onClicked(note) },
        onLongClicked = { onLongClicked(note) }
    ) {
        NoteCover()
        NoteContent(note.title, note.content)
    }
}

@Composable
internal fun NoteContent(title: String = "", content: String = "") {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(6.dp, 3.dp)
            .wrapContentHeight()
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.wrapContentWidth()
        )
        Text(
            text = content,
            fontSize = 13.sp,
            maxLines = 1,
            fontWeight = FontWeight.Light,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.wrapContentWidth()
        )
    }
}

@Composable
internal fun NoteCover() {
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .padding(6.dp, 3.dp)
            .height(40.dp)
            .width(40.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_jetpack_compose),
            contentDescription = null,
            contentScale = ContentScale.Inside
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CustomSurface(
    onClicked: () -> Unit = {},
    onLongClicked: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(4.dp)
            .combinedClickable(
                enabled = true,
                onClick = onClicked,
                onLongClick = onLongClicked
            ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.Cyan),
        elevation = 5.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

@Preview
@Composable
internal fun NoteItemPreview() {
    CustomSurface {
        NoteCover()
        NoteContent(
            "This is a titleThis is a titleThis is a titleThis is a titleThis is a titleThis is a title",
            "This is contentThis is contentThis is contentThis is contentThis is contentThis is contentThis is content"
        )
    }
}