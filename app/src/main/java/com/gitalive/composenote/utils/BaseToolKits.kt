package com.gitalive.composenote.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDateTime(
    format: String = "yyyyMMddHHmmss",
    locale: Locale = Locale.CHINA,
    date: Date = Date()
): String = SimpleDateFormat(format, locale).format(date)