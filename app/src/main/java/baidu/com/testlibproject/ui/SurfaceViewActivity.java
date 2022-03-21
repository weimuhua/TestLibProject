package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import baidu.com.commontools.utils.LogHelper;
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
                LogHelper.i(TAG, "surfaceCreated: " + holder);
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setSurface(holder.getSurface());

//                String path = "android.resource://" + getPackageName() + "/" + R.raw.vid_bigbuckbunny;
                String path = "http://adsmind.gdtimg.com/ads_svp_video__0b6bwyaaeaaaw4aclqlymrpbvnqeak3aaasa.f40.mp4?dis_k=79c09f652c171a890b45e0c443cf3a48&dis_t=1584932735";
                try {
                    mMediaPlayer.setDataSource(SurfaceViewActivity.this, Uri.parse(path));
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                   LogHelper.w(TAG, "prepare fail ", e);
                }

                mSurfaceCreated = true;

                startPlayIfAvailable();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                LogHelper.i(TAG, "surfaceChanged: " + holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                LogHelper.i(TAG, "surfaceDestroyed: " + holder);
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
