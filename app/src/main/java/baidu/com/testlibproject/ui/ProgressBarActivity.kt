package baidu.com.testlibproject.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import baidu.com.testlibproject.R

class ProgressBarActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private var animator: ObjectAnimator? = null
    private var countdownTime = 30L
    private val uiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_bar)

        progressBar = findViewById(R.id.choose_topic_progressbar)
        progressBar.max = 100
        progressBar.progress = 100
        animator = ObjectAnimator
                .ofInt(progressBar, "progress", 0)
                .setDuration(countdownTime * 1000)
        progressBar.interpolator = LinearInterpolator()
        animator!!.start()
        uiHandler.postDelayed(object : Runnable {
            override fun run() {
                if (countdownTime > 0) {
                    countdownTime--
                    uiHandler.postDelayed(this, 200)
                }
            }
        }, 200)
    }

    override fun onDestroy() {
        super.onDestroy()
        uiHandler.removeCallbacksAndMessages(null)
    }
}
