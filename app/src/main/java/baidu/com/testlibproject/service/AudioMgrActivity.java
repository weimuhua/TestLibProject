package baidu.com.testlibproject.service;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import baidu.com.testlibproject.R;

public class AudioMgrActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button mPlayBtn;
    private Button mVolumeUpBtn;
    private Button mVolumeDownBtn;
    private ToggleButton mMuteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_mgr_layout);
        initView();
    }

    private void initView() {
        mPlayBtn = (Button) findViewById(R.id.play_btn);
        mVolumeUpBtn = (Button) findViewById(R.id.volume_up);
        mVolumeDownBtn = (Button) findViewById(R.id.volume_down);
        mMuteBtn = (ToggleButton) findViewById(R.id.mute_btn);
        mPlayBtn.setOnClickListener(this);
        mVolumeUpBtn.setOnClickListener(this);
        mVolumeDownBtn.setOnClickListener(this);
        mMuteBtn.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (v == mPlayBtn) {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.train);
            mp.setLooping(true);
            mp.start();
        } else if (v == mVolumeUpBtn) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        } else if (v == mVolumeDownBtn) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mMuteBtn) {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                am.setStreamMute(AudioManager.STREAM_MUSIC, isChecked);
            } else {
                int direction = isChecked ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE;
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, AudioManager.FLAG_SHOW_UI);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
