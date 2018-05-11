package ct7liang.android.phonehelper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ct7liang.tangyuan.utils.LogUtils;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;

import java.util.ArrayList;

import ct7liang.android.phonehelper.base.BaseActivity;

public class SetRingtoneActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private Switch sIsMute;
    private TextView tvIsMute;
    private Switch sIsShake;
    private TextView tvIsShake;
    private SeekBar sbRingTong;
    private AudioManager mAudioManager;
    private TextView ringToneName;

    @Override
    public int setLayout() {
        return R.layout.activity_set_ringtone;
    }

    @Override
    public void findView() {
        initStatusBar();
        findViewById(R.id.system_setting).setOnClickListener(this);
        sIsMute = (Switch) findViewById(R.id.switch_isMute);
        tvIsMute = (TextView) findViewById(R.id.tv_isMute);
        sIsShake = (Switch) findViewById(R.id.switch_isShake);
        tvIsShake = (TextView) findViewById(R.id.tv_isShake);
        sbRingTong = (SeekBar) findViewById(R.id.sb_ringTong);
        ringToneName = (TextView) findViewById(R.id.tv_ringtone_name);
    }

    @Override
    protected void setStatusBar() {
        findViewById(R.id.left_image).setOnClickListener(this);
        ((TextView)findViewById(R.id.center_text)).setText("设置铃声");
        findViewById(R.id.title_back_ground).setBackgroundResource(R.color.colorPrimary);
        findViewById(R.id.title_back_ground).setPadding(0, ScreenInfoUtil.getStatusHeight(this), 0, 0);
    }

    @Override
    public void initData() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void initView() {
        sbRingTong.setOnSeekBarChangeListener(this);
        sIsMute.setOnCheckedChangeListener(this);
        sIsShake.setOnCheckedChangeListener(this);
    }

    @Override
    public void initFinish() {
        scannerMediaFile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_image:
                finish();
                break;
            case R.id.system_setting:
                startActivity(new Intent(Settings.ACTION_SOUND_SETTINGS));
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser){
            return;
        }
        //设置具体音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.FLAG_PLAY_SOUND);  //调整时显示音量条
//        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.FLAG_SHOW_UI);  //调整时显示音量条
//        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.FLAG_PLAY_SOUND);  //调整音量时播放声音, 0表示什么也没有
//        seekBar.setProgress(progress);
        if (progress==0){
            sIsMute.setChecked(false);
//            mAudioManager.setStreamMute(AudioManager.STREAM_RING, true); //设置为静音模式
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT); //设置为静音模式
        }else {
            sIsMute.setChecked(true);
//            mAudioManager.setStreamMute(AudioManager.STREAM_RING, false); //设置为普通模式
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL); //设置为普通模式
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_isMute:
                if (isChecked){
                    sbRingTong.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_RING));
//                    mAudioManager.setStreamMute(AudioManager.STREAM_RING, false); //设置为普通模式
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    tvIsMute.setText("来电时响铃");
                    tvIsMute.setTextColor(Color.parseColor("#2F2F2F"));
                }else{
//                    mAudioManager.setStreamMute(AudioManager.STREAM_RING, true); //设置为静音模式
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    tvIsMute.setText("手机当前处于静音状态!");
                    tvIsMute.setTextColor(Color.parseColor("#B0B0B0"));
                }
                break;
            case R.id.switch_isShake:
                if (isChecked){
                    Settings.System.putInt(getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 1);
                    tvIsShake.setText("响铃时震动已开启");
                    tvIsShake.setTextColor(Color.parseColor("#2F2F2F"));
                }else{
                    Settings.System.putInt(getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 0);
                    tvIsShake.setText("响铃时震动未开启");
                    tvIsShake.setTextColor(Color.parseColor("#B0B0B0"));
                }
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
            int current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
            sbRingTong.setMax(max);
            sbRingTong.setProgress(current);
            if (current==0){
                sIsMute.setChecked(false);
                tvIsMute.setText("手机当前处于静音状态!");
                tvIsMute.setTextColor(Color.parseColor("#B0B0B0"));
            }else{
                sIsMute.setChecked(true);
                tvIsMute.setText("来电时响铃");
                tvIsMute.setTextColor(Color.parseColor("#2F2F2F"));
            }
            int anInt = Settings.System.getInt(getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 0);
            if (anInt==0){
                sIsShake.setChecked(false);
                tvIsShake.setText("响铃时震动未开启");
                tvIsShake.setTextColor(Color.parseColor("#B0B0B0"));
            }else{
                sIsShake.setChecked(true);
                tvIsShake.setText("响铃时震动已开启");
                tvIsShake.setTextColor(Color.parseColor("#2F2F2F"));
            }
            ringToneName.setText(RingtoneManager.getRingtone(this, Settings.System.DEFAULT_RINGTONE_URI).getTitle(this));
        }
    }

//        mAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
//        mAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
//        mAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_SAME, AudioManager.FLAG_PLAY_SOUND);
//        ADJUST_LOWER 降低音量、ADJUST_RAISE 升高音量、ADJUST_SAME 音量保持不变,这个主要用于向用户展示当前的音量

    /**
     * 通过分析Android中的自带Setting App源代码，我找到了设置此选项的方法：
     * Settings.System.putInt(getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, val ? 1 : 0)
     * Settings.System.getInt(getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 0)
     * 可以看到,在 com.android.settings.notification.NotificationSettings 类中，
     * 是通过调用 Settings.System.putInt() 方法来设置此选项值，通过 Settings.System.getInt() 来得到此选项的值。
     * 在 android.provider.Settings 类中，可以看到 Settings.System.VIBRATE_WHEN_RINGING 的值为 vibrate_when_ringing ，
     * 是一个字符串类型的静态常量，但是在代码中是不能访问到的。
     */

    /**
     * 扫描系统内部通知铃声
     * _id, _data, _display_name, _size,  mime_type,
     * date_added, is_drm, date_modified, title,
     * title_key, duration, artist_id, composer,
     * album_id, track, year, date, is_ringtone, is_music,
     * is_alarm, is_notification, is_podcast,
     * bookmark, album_artist, album_artist_key,
     * parent, bucket_display_name, drm_content_uri,
     * drm_offset, drm_dataLen, drm_rights_issuer,
     * drm_content_name, drm_content_description, drm_content_vendor,
     * drm_icon_uri, drm_method, title_pinyin_key,
     * is_record, storage_id, artist_id:1,
     * artist_key, artist, artist_pinyin_key,
     * album_id:1, album_key, album, album_pinyin_key
     */
    private void scannerMediaFile() {
        ArrayList<String> list = new ArrayList<>();
        ContentResolver cr = this.getContentResolver();
        Cursor cursor = cr.query(
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                null, null, null, null
        );
        if (cursor == null) {
            return;
        }
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            list.add(cursor.getColumnName(i));
        }
        LogUtils.write(list.toString());
//        while (cursor.moveToNext()) {
//            list.add(cursor.getString(1));
//        }
    }
}