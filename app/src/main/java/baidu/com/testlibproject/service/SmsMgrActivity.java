package baidu.com.testlibproject.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import baidu.com.testlibproject.R;

public class SmsMgrActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_mgr_layout);
        sendMessage();
    }

    private void sendMessage() {
        SmsManager sm = SmsManager.getDefault();
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(), 0);
        sm.sendTextMessage("+8618500388463", null, "I love you! from Android code !", pi, null);
        Toast.makeText(this, "send message to +8618500388463!", Toast.LENGTH_SHORT).show();
    }
}
