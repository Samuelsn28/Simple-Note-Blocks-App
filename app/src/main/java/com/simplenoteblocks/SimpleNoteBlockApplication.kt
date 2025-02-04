package com.simplenoteblocks

import android.app.Application
import com.simplenoteblocks.data.Graph

class SimpleNoteBlockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provideDatabase(this)
    }
}