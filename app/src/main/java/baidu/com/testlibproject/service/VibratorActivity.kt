package baidu.com.testlibproject.service

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.MotionEvent
import android.widget.Toast

import baidu.com.testlibproject.R

class VibratorActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vibrator_layout)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                Toast.makeText(this, "vibrate!", Toast.LENGTH_SHORT).show()
                vibrator.vibrate(500)
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
