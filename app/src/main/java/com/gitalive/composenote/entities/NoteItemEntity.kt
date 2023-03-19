package com.gitalive.composenote.entities

import com.gitalive.composenote.utils.getFormattedDateTime
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class NoteItemEntity(
    @Id(assignable = true)
    var id: Long = getFormattedDateTime().toLong(),
    var cover: String = "",
    var title: String = "暂无标题",
    var content: String = "请输入事项内容",
    var createTime: String = getFormattedDateTime("yyyy.MM.dd HH:mm:ss"),
    var editTime: String = getFormattedDateTime("yyyy.MM.dd HH:mm:ss")
)
