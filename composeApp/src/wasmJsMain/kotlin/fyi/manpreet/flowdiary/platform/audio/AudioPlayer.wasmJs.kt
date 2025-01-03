package fyi.manpreet.flowdiary.platform.audio

import kotlinx.browser.document
import kotlinx.dom.appendElement
import org.w3c.dom.HTMLAudioElement

actual class AudioPlayer(private val htmlId: String) {
    actual fun play(url: String) {
        release()
        document.body?.appendElement("audio") {
            this as HTMLAudioElement
            this.id = htmlId
            this.src = url
        }

        val playerEl = getPlayerElement()
        playerEl?.play()
    }

    actual fun stop() {
        val playerEl = getPlayerElement()
        playerEl?.pause()
    }

    actual fun release() {
        val playerEl = getPlayerElement()
        playerEl?.pause()
        playerEl?.remove()
    }

    private fun getPlayerElement(): HTMLAudioElement? {
        return document.getElementById(htmlId) as? HTMLAudioElement
    }
}
