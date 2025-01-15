package fyi.manpreet.flowdiary.preview.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fyi.manpreet.flowdiary.data.model.AmplitudeData
import fyi.manpreet.flowdiary.preview.DevicePreview
import fyi.manpreet.flowdiary.preview.MockAmplitudeData
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.player.AudioPlayer
import fyi.manpreet.flowdiary.ui.theme.FlowTheme
import kotlin.time.Duration

@DevicePreview
@Composable
fun AudioPlayerPreview(
    @PreviewParameter(AudioPlayerPreviewProvider::class) pattern: Pair<String, List<AmplitudeData>>
) {
    FlowTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(pattern.first)
            AudioPlayer(
                modifier = Modifier,
                amplitudeData = pattern.second,
                isPlaying = false,
                emotionType = EmotionType.Excited,
                currentPosition = Duration.ZERO,
                totalDuration = Duration.ZERO,
                onPlayPauseClick = { },
                onSeek = { }
            )
        }
    }
}

class AudioPlayerPreviewProvider : PreviewParameterProvider<Pair<String, List<AmplitudeData>>> {
    override val values: Sequence<Pair<String, List<AmplitudeData>>>
        get() = sequenceOf(
            "Sine Wave" to MockAmplitudeData.sineWavePattern.map { AmplitudeData(it) },
            "Ascending" to MockAmplitudeData.ascendingPattern.map { AmplitudeData(it) },
            "Descending" to MockAmplitudeData.descendingPattern.map { AmplitudeData(it) },
            "Natural" to MockAmplitudeData.naturalPattern.map { AmplitudeData(it) },
            "Spikey" to MockAmplitudeData.spikeyPattern.map { AmplitudeData(it) },
            "Buildup" to MockAmplitudeData.buildupPattern.map { AmplitudeData(it) },
            "Mixed" to MockAmplitudeData.mixedPattern.map { AmplitudeData(it) },
            "Voice Simulation" to MockAmplitudeData.voiceSimulation.map { AmplitudeData(it) },
            "Short" to MockAmplitudeData.shortPattern.map { AmplitudeData(it) },
            "Long" to MockAmplitudeData.longPattern.map { AmplitudeData(it) },
            "allZeros" to MockAmplitudeData.allZeros.map { AmplitudeData(it) },
            "allOnes" to MockAmplitudeData.allOnes.map { AmplitudeData(it) },
            "alternating" to MockAmplitudeData.alternating.map { AmplitudeData(it) },
        )
}
