package baidu.com.testlibproject.service

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ToggleButton

import baidu.com.testlibproject.R

class AudioMgrActivity : Activity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var mPlayBtn: Button? = null
    private var mVolumeUpBtn: Button? = null
    private var mVolumeDownBtn: Button? = null
    private var mMuteBtn: ToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_mgr_layout)
        initView()
    }

    private fun initView() {
        mPlayBtn = findViewById(R.id.play_btn)
        mVolumeUpBtn = findViewById(R.id.volume_up)
        mVolumeDownBtn = findViewById(R.id.volume_down)
        mMuteBtn = findViewById(R.id.mute_btn)
        mPlayBtn!!.setOnClickListener(this)
        mVolumeUpBtn!!.setOnClickListener(this)
        mVolumeDownBtn!!.setOnClickListener(this)
        mMuteBtn!!.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View) {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (v === mPlayBtn) {
            val mp = MediaPlayer.create(this, R.raw.train)
            mp.isLooping = true
            mp.start()
        } else if (v === mVolumeUpBtn) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
        } else if (v === mVolumeDownBtn) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (buttonView === mMuteBtn) {
            val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                @Suppress("DEPRECATION")
                am.setStreamMute(AudioManager.STREAM_MUSIC, isChecked)
            } else {
                val direction = if (isChecked) AudioManager.ADJUST_MUTE else AudioManager.ADJUST_UNMUTE
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, AudioManager.FLAG_SHOW_UI)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
