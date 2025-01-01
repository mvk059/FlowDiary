package fyi.manpreet.flowdiary

import android.app.Application
import fyi.manpreet.flowdiary.di.initKoin

class FlowDiaryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}