package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;

import baidu.com.testlibproject.R;

public class VideoViewActivity extends Activity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoView = new VideoView(this);
        setContentView(mVideoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.vid_bigbuckbunny;
        mVideoView.setVideoPath(path);
        mVideoView.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
