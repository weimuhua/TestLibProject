package baidu.com.testlibproject.audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import baidu.com.commontools.threadpool.MhThreadPool
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.R

class AudioRecordDemoActivity : AppCompatActivity() {

    private var hasPermission = false
    private lateinit var recordBtn: Button
    private lateinit var stopBtn: Button

    private var bufferSize = 0
    private var isRecording = false
    private lateinit var recorder: AudioRecord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record_demo)
        initView()
        checkPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE == requestCode) {
            hasPermission = checkHasPermission()
            if (hasPermission) {
                initRecorder()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        recordBtn = findViewById<Button>(R.id.record_btn).apply {
            text = "RECORD"
            setOnClickListener {
                if (!hasPermission) {
                    throw IllegalStateException("cannot record cause has no permission...")
                }
                if (isRecording) {
                    return@setOnClickListener
                }
                try {
                    recorder.startRecording()
                    isRecording = true

                    MhThreadPool.getInstance().addBkgTask {
                        val audioData = ByteArray(bufferSize)
                        // 持续读取音频数据到字节数组, 再将字节数组写入文件
                        while (isRecording) {
                            val time = System.currentTimeMillis()
                            recorder.read(audioData, 0, audioData.size)
                            LogHelper.v(TAG, "read PCM data, cost=${System.currentTimeMillis() - time}")
                        }
                    }
                } catch (e: java.lang.IllegalStateException) {
                    LogHelper.w(TAG, "startRecording fail", e)
                }
            }
        }
        stopBtn = findViewById<Button>(R.id.stop_btn).apply {
            text = "STOP"
            setOnClickListener {
                if (!isRecording) {
                    return@setOnClickListener
                }
                try {
                    isRecording = false
                    recorder.stop()
                } catch (e: java.lang.IllegalStateException) {
                    LogHelper.w(TAG, "stop fail", e)
                }
            }
        }
    }

    private fun checkPermission() {
        hasPermission = checkHasPermission()

        if (hasPermission) {
            initRecorder()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE)
            }
        }
    }

    private fun checkHasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun initRecorder() {
        try {
            bufferSize =
                AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_IN_MONO, ENCODING_PCM_16BIT)
            recorder =
                AudioRecord(SOURCE, SAMPLE_RATE, CHANNEL_IN_MONO, ENCODING_PCM_16BIT, bufferSize)
        } catch (e: IllegalArgumentException) {
            LogHelper.e(TAG, "create recorder fail ", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::recorder.isInitialized) {
            recorder.release()
        }
    }

    companion object {
        private const val TAG = "AudioRecordDemoActivity"
        private const val REQUEST_CODE = 1001
        private const val SOURCE = MediaRecorder.AudioSource.MIC
        private const val SAMPLE_RATE = 16000
        private const val CHANNEL_IN_MONO = AudioFormat.CHANNEL_IN_MONO // 单声道
        private const val ENCODING_PCM_16BIT = AudioFormat.ENCODING_PCM_16BIT //量化精度为 16 位
    }
}