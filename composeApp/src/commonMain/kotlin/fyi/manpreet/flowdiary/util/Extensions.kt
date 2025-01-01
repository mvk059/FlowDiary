package fyi.manpreet.flowdiary.util

import com.composables.core.SheetDetent

val Peek = SheetDetent(identifier = "peek") { containerHeight, sheetHeight ->
    containerHeight * 0.4f
}
