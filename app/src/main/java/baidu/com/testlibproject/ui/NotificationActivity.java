package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import baidu.com.testlibproject.MainActivity;
import baidu.com.testlibproject.R;

public class NotificationActivity extends Activity implements View.OnClickListener {

    private static final int NOTIFICATION_ID = 0x123;

    private Button mSendBtn;
    private Button mCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_notification_layout);
        initView();
    }

    private void initView() {
        mSendBtn = (Button) findViewById(R.id.send_btn);
        mCancelBtn = (Button) findViewById(R.id.cancel_btn);
        mSendBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSendBtn) {
            sendNotification();
        } else if (v == mCancelBtn) {
            cancelNotification();
        }
    }

    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Builder builder = new Builder(this);
        builder.setTicker(getString(R.string.notify_ticker));
        builder.setContentTitle(getString(R.string.notify_title));
        builder.setContentText(getString(R.string.notify_text));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pi);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        Notification notify = builder.build();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, notify);
    }

    private void cancelNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }
}
