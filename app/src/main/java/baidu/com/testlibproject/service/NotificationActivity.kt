package baidu.com.testlibproject.service

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat.Builder
import baidu.com.testlibproject.MainActivity
import baidu.com.testlibproject.R

class NotificationActivity : Activity(), View.OnClickListener {

    private var mSendBtn: Button? = null
    private var mCancelBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_notification_layout)
        initView()
    }

    private fun initView() {
        mSendBtn = findViewById(R.id.send_btn)
        mCancelBtn = findViewById(R.id.cancel_btn)
        mSendBtn!!.setOnClickListener(this)
        mCancelBtn!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v === mSendBtn) {
            sendNotification()
        } else if (v === mCancelBtn) {
            cancelNotification()
        }
    }

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = Builder(this)
        builder.setTicker(getString(R.string.notify_ticker))
        builder.setContentTitle(getString(R.string.notify_title))
        builder.setContentText(getString(R.string.notify_text))
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentIntent(pi)
        builder.setAutoCancel(true)
        builder.setWhen(System.currentTimeMillis())
        val notify = builder.build()
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, notify)
    }

    private fun cancelNotification() {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_ID)
    }

    companion object {

        private val NOTIFICATION_ID = 0x123
    }
}
