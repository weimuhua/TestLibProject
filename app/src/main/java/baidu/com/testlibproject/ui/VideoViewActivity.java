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
//        String path = "android.resource://" + getPackageName() + "/" + R.raw.vid_bigbuckbunny;
        String path = "http://mediatest.qq.com/qqstocdnd?filekey=828a9844a4f1369517949a9e1b4bc0ac" +
                "&fileid=3062020103045b3059020100041231343431313531393838353039313631363102030f424" +
                "1020424253db702045a1e7917042038323861393834346134663133363935313739343961396531623" +
                "4626330616302010002020902020300c3520201000400&bid=10011&setnum=50002&authkey=30400" +
                "201010439303702010102010102040dc3f35002030f4241020424253db7020424253db702030f4df90" +
                "20429253db702010002045a26621502047ec0a34a0400&filetype=2306";
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
