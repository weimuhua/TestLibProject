package baidu.com.testlibproject.service

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.telephony.SmsManager
import android.widget.Toast

import baidu.com.testlibproject.R

class SmsMgrActivity : Activity(), Handler.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sms_mgr_layout)
        //        sendMessage();
    }

    private fun sendMessage() {
        val sm = SmsManager.getDefault()
        val pi = PendingIntent.getActivity(this, 0, Intent(), 0)
        sm.sendTextMessage("+8618500388463", null, "I love you! from Android code !", pi, null)
        Toast.makeText(this, "send message to +8618500388463!", Toast.LENGTH_SHORT).show()
        val handler = Handler(this)
        handler.postDelayed({ finish() }, 5000)
    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
