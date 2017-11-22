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

                String path = "android.resource://" + getPackageName() + "/" + R.raw.vid_bigbuckbunny;
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
