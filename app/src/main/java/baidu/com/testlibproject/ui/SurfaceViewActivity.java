package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import baidu.com.testlibproject.R;

public class SurfaceViewActivity extends Activity {

    public static final String TAG = SurfaceViewActivity.class.getSimpleName();
    private MediaPlayer mMediaPlayer;

    private boolean mResumed = false;
    private boolean mSurfaceCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView surfaceView = new SurfaceView(this);
        setContentView(surfaceView);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setSurface(holder.getSurface());

//                String path = "android.resource://" + getPackageName() + "/" + R.raw.vid_bigbuckbunny;
                String path = "http://mediatest.qq.com/qqstocdnd?filekey=828a9844a4f1369517949a9e1b4bc0ac" +
                        "&fileid=3062020103045b3059020100041231343431313531393838353039313631363102030f424" +
                        "1020424253db702045a1e7917042038323861393834346134663133363935313739343961396531623" +
                        "4626330616302010002020902020300c3520201000400&bid=10011&setnum=50002&authkey=30400" +
                        "201010439303702010102010102040dc3f35002030f4241020424253db7020424253db702030f4df90" +
                        "20429253db702010002045a26621502047ec0a34a0400&filetype=2306";
                try {
                    mMediaPlayer.setDataSource(SurfaceViewActivity.this, Uri.parse(path));
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mSurfaceCreated = true;

                startPlayIfAvailable();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        startPlayIfAvailable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void startPlayIfAvailable() {
        if (mResumed && mSurfaceCreated) {
            mMediaPlayer.start();
        }
    }
}
