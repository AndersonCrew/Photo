package com.dazone.crewphoto.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun getTimezoneOffsetInMinutes(): Int {
        val tz = TimeZone.getDefault()
        return tz.rawOffset / 60000
    }

    fun showTimeWithoutTimeZone(
        date: Long,
        defaultPattern: String?
    ): String? {
        val simpleDateFormat =
            SimpleDateFormat(defaultPattern, Locale.getDefault())
        return simpleDateFormat.format(Date(date))
    }
}