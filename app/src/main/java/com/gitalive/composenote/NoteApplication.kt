package com.gitalive.composenote

import android.app.Application
import com.gitalive.composenote.utils.DatabaseRepository
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class NoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initObjectBox(this)
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}