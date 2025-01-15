package fyi.manpreet.flowdiary.preview

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

object MockAmplitudeData {
    private const val SIZE = 50
    // Smooth sine wave pattern
    val sineWavePattern = List(SIZE) { index ->
        val x = (index.toFloat() / SIZE) * 2 * PI
        (sin(x) * 0.5 + 0.5).toFloat()  // Normalized to 0..1
    }

    // Ascending pattern
    val ascendingPattern = List(SIZE) { index ->
        (index.toFloat() / SIZE)
    }

    // Descending pattern
    val descendingPattern = List(SIZE) { index ->
        (1 - index.toFloat() / SIZE)
    }

    // Random pattern (simulating natural speech)
    val naturalPattern = List(SIZE) {
        Random.nextFloat() * 0.6f + 0.2f  // Range 0.2..0.8
    }

    // Pattern with sudden spikes (like music beats)
    val spikeyPattern = List(SIZE) { index ->
        if (index % 10 == 0) 0.9f else 0.2f
    }

    // Gradual build-up pattern
    val buildupPattern = List(SIZE) { index ->
        val progress = index.toFloat() / SIZE
        progress * progress  // Quadratic increase
    }

    // Mixed frequencies pattern
    val mixedPattern = List(SIZE) { index ->
        val x = (index.toFloat() / SIZE) * 2 * PI
        val highFreq = sin(x * 10) * 0.3
        val lowFreq = sin(x * 2) * 0.7
        ((highFreq + lowFreq) * 0.5 + 0.5).toFloat().coerceIn(0f, 1f)
    }

    // Short pattern (test with fewer points)
    val shortPattern = List(20) { 
        Random.nextFloat()
    }

    // Long pattern (test performance)
    val longPattern = List(500) { 
        Random.nextFloat()
    }

    // Edge case patterns
    val allZeros = List(SIZE) { 0f }
    val allOnes = List(SIZE) { 1f }
    val alternating = List(SIZE) { index -> if (index % 2 == 0) 0.2f else 0.8f }
    
    // Real-world simulation (voice recording)
    val voiceSimulation = List(SIZE) { index ->
        when {
            index < 20 -> Random.nextFloat() * 0.3f  // Low amplitude start
            index < 40 -> Random.nextFloat() * 0.8f  // Speaking
            index < 45 -> Random.nextFloat() * 0.2f  // Brief pause
            index < 75 -> Random.nextFloat() * 0.9f  // Louder speaking
            else -> Random.nextFloat() * 0.4f        // Trailing off
        }
    }
}