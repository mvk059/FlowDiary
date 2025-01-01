package fyi.manpreet.flowdiary

import androidx.compose.ui.window.ComposeUIViewController
import fyi.manpreet.flowdiary.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
    content = {
        App()
    }
)