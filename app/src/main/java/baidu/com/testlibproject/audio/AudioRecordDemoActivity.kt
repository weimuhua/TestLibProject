package baidu.com.testlibproject.audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import baidu.com.testlibproject.R

class AudioRecordDemoActivity : AppCompatActivity() {

    private var hasPermission = false
    private lateinit var recordBtn: Button
    private lateinit var stopBtn: Button

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
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        recordBtn = findViewById<Button>(R.id.record_btn).apply {
            text = "RECORD"
        }
        stopBtn = findViewById<Button>(R.id.stop_btn).apply {
            text = "STOP"
        }
    }

    private fun checkPermission() {
        hasPermission = checkHasPermission()

        if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE)
        }
    }

    private fun checkHasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE = 1001
    }
}