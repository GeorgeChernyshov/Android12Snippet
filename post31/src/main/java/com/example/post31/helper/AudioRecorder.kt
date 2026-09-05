package com.example.post31.helper

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioRecorder {

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    private var recorder: AudioRecord? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    @RequiresPermission(android.Manifest.permission.RECORD_AUDIO)
    fun start() {
        if (recorder != null) stop()

        scope.launch {
            val sampleRate = 44100
            val channelConfig = AudioFormat.CHANNEL_IN_MONO
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            val bufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                channelConfig,
                audioFormat
            )

            val rec = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )

            recorder = rec

            rec.startRecording()
            _isRecording.value = true

            withContext(Dispatchers.IO) {
                val buffer = ShortArray(bufferSize)
                while (isRecording.value) {
                    rec.read(
                        buffer,
                        0,
                        buffer.size
                    )
                }
            }
        }
    }

    fun stop() {
        recorder?.stop()
        recorder?.release()
        recorder = null
        _isRecording.value = false
    }
}