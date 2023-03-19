package com.gitalive.composenote.entities

import com.gitalive.composenote.utils.getFormattedDateTime
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class NoteItemEntity(
    @Id(assignable = true)
    var id: Long = getFormattedDateTime().toLong(),
    var cover: String = "",
    var title: String = "",
    var content: String = "",
    var createTime: String = "",
    var editTime: String = ""
)
