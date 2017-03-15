package com.shinemo.widget.audio.progress;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.imdemo.event.EventVoiceFinish;
import com.shinemo.openim.utils.CollectionUtil;
import com.shinemo.utils.audio.AudioManagers;
import com.shinemo.utils.audio.OnPlayListener;
import com.shinemo.widget.audio.VoiceWaveView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;


/**
 * Created by zale on 2015/8/20.
 */
public class RecordProgressView extends LinearLayout {


    private LinearLayout mRecordBgLayout;
    private RoundProgressBar roundProgressBar;
    private ImageView playStateImage;
    private ProgressBar progressForLoading;
    private TextView recordTime;
    private VoiceWaveView audioView;
    private ImageView deleteViewImage;
    private View playLayout;

    private AudioManagers mAudioManagers;
    private int playState;
    private String mRecordPath;
    private int recordIntTime;
    private boolean isNeedAdjustWave= false;

    private int modeType = 0;
    private int pauseIcon = R.drawable.xx_qp_yy_zt_red;
    private int playIcon = R.drawable.xx_qp_yy_bf_red;

    private View rootView;
    private int mCurrentPosition = -1;
    private OnPlayListener mListener;

    public final int chat_need_back_type = 1;//一直都是红色for im
    public final int chat_normal_type = 2;//一直都是灰色for im
    public final int youban_type = 3;//链接色
    public final int chat_normal_type_white = 4;//我发出的颜色

    public RecordProgressView(Context context) {
        super(context);
        initView();
    }

    public RecordProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //导入布局
        rootView = LayoutInflater.from(context).inflate(R.layout.note_audio_item, this, true);
        initView();
    }

    /**
     * 初始化控件
     */
    public void initView() {
        mRecordBgLayout = (LinearLayout) findViewById(R.id.record_background);
        roundProgressBar = (RoundProgressBar) findViewById(R.id.progress_for_audio_left);
        playStateImage = (ImageView) findViewById(R.id.play_state);
        progressForLoading = (ProgressBar) findViewById(R.id.progressBar_for_loading);
        recordTime = (TextView) findViewById(R.id.recode_time_small);
        audioView = (VoiceWaveView) findViewById(R.id.audio_wave);
        deleteViewImage = (ImageView) findViewById(R.id.delete_record);
        playLayout = findViewById(R.id.record_background);

        deleteViewImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
                rootView.setVisibility(GONE);
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick();
                }
            }
        });
        playLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    /**
     * 设置语音的背景气泡图片
     * @param resource
     */
    public void setRecordBackground(int resource){
        mRecordBgLayout.setBackgroundResource(resource);
    }

    public String getmRecordPath(){
        return mRecordPath;
    }

    public void setMeMode(){
        modeType = chat_normal_type_white;
        playStateImage.setImageResource(R.drawable.xx_qp_yy_bf_white);
        audioView.setPlayColor(getResources().getColor(R.color.c_ff), getResources().getColor(R.color.c_voice_white));
        recordTime.setTextColor(getResources().getColor(R.color.c_ff));
        roundProgressBar.setRoundProgressColor(getResources().getColor(R.color.c_ff));
    }

    public void setNormalMode(){
        modeType = chat_normal_type;
        playStateImage.setImageResource(R.drawable.xx_qp_yy_bf_hui);
        audioView.setPlayColor(getResources().getColor(R.color.recordProgress_gray), getResources().getColor(R.color.c_voice_grey));
        recordTime.setTextColor(getResources().getColor(R.color.recordProgress_gray));
        roundProgressBar.setRoundProgressColor(getResources().getColor(R.color.recordProgress_gray));
    }

    public void setChatNeedBackMode(){
        modeType = chat_need_back_type;
        playStateImage.setImageResource(R.drawable.xx_qp_yy_bf_red);
        audioView.setPlayColor(getResources().getColor(R.color.c_brand), getResources().getColor(R.color.c_voice_brand));
        recordTime.setTextColor(getResources().getColor(R.color.c_brand));
        roundProgressBar.setRoundProgressColor(getResources().getColor(R.color.c_brand));
    }

    /**
     * 建议使用oadRecordUrl(recordPath, duration, voice)
     * @param recordPath
     * @param duration
     */
    @Deprecated
    public void loadRecordUrl(final String recordPath, int duration) {
        loadRecordUrl(recordPath, duration, (int[]) null);
    }

    public void loadRecordUrl(final String recordPath, int duration, List<Integer> voice) {
        loadRecordUrl(recordPath, duration, CollectionUtil.toIntArray(voice));
    }

    /**
     * 初始化值
     *
     * @param recordPath
     */
    public void loadRecordUrl(final String recordPath, int duration, int[] voice) {
        mAudioManagers = AudioManagers.getInstance();
        mRecordPath = recordPath;
        roundProgressBar.setMax(100);
        roundProgressBar.setProgress(100);
        playState = OnPlayListener.PLAY_STOPED;
        recordIntTime = duration;
        audioView.setVoiceArray(voice, duration);
        setRecordTime(recordIntTime);
        progressForLoading.setVisibility(View.GONE);
    }

    public void loadRecordUrl(int position, String recordPath, int duration, int[] voice, OnPlayListener listener) {
        mCurrentPosition = position;
        mListener = listener;
        progressForLoading.setVisibility(View.GONE);
        loadRecordUrl(recordPath, duration, voice);
    }

    /**
     * 停止播放语音
     */
    public void stopPlay(){
        mAudioManagers.stopPlay();
    }

    public void play(){
        progressForLoading.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mRecordPath)) {
            if (playState == OnPlayListener.PLAY_STOPED
                    || playState == OnPlayListener.PLAY_COMPLEMENTED) {
                if(mListener != null){
                    mAudioManagers.play(mRecordPath, mListener);
                }else{
                    mAudioManagers.play(mRecordPath, mPlayListener);
                }
            } else if (playState == OnPlayListener.PLAY_PAUSED) {
                mAudioManagers.resume(mRecordPath);
            } else if (playState == OnPlayListener.PLAY_START) {
                mAudioManagers.pause(mRecordPath);
            }
        }
    }

    /**
     * 设置是否显示删除按钮
     *
     * @param isShow
     */
    public void setDeleteViewVisible(Boolean isShow) {
        if (isShow) {
            deleteViewImage.setVisibility(View.VISIBLE);
        } else {
            deleteViewImage.setVisibility(View.GONE);
        }
    }

    /**
     * 设置播放的时间
     *
     * @param recordLength
     */
    private void setRecordTime(int recordLength) {


        if (recordLength < 10) {
            recordTime.setText("00:0" + recordLength);
        } else {
            recordTime.setText("00:" + recordLength);
        }
        if (recordLength<=1){
            recordTime.setText("00:01");
        }
    }

    private void setRes(){
        switch (modeType) {
            case chat_need_back_type:
                pauseIcon = R.drawable.xx_qp_yy_zt_red;
                playIcon = R.drawable.xx_qp_yy_bf_red;
                break;
            case chat_normal_type:
                pauseIcon = R.drawable.xx_qp_yy_zt_hui;
                playIcon = R.drawable.xx_qp_yy_bf_hui;
                break;
            case youban_type:
                pauseIcon = R.drawable.xx_qp_yy_zt_red;
                playIcon = R.drawable.xx_qp_yy_bf_red;
                break;
            case chat_normal_type_white:
                pauseIcon = R.drawable.xx_qp_yy_zt_white;
                playIcon = R.drawable.xx_qp_yy_bf_white;
                break;
        }
    }

    public void setState(int state){
        progressForLoading.setVisibility(View.GONE);
        setRes();
        switch (state) {
            case OnPlayListener.PLAY_START:
                playStateImage.setImageResource(pauseIcon);
                playState = OnPlayListener.PLAY_START;
                break;
            case OnPlayListener.PLAY_STOPED:
                playStateImage.setImageResource(playIcon);
                playState = OnPlayListener.PLAY_STOPED;
                roundProgressBar.setProgress(100);
                audioView.onComplete();
                setRecordTime(recordIntTime);
                break;
            case OnPlayListener.PLAY_COMPLEMENTED:
                playStateImage.setImageResource(playIcon);
                playState = OnPlayListener.PLAY_COMPLEMENTED;
                roundProgressBar.setProgress(100);
                setRecordTime(recordIntTime);
                audioView.onComplete();
                if(mCurrentPosition != -1){
                    EventBus.getDefault().post(new EventVoiceFinish(mCurrentPosition));
                }
                break;
            case OnPlayListener.PLAY_PAUSED:
                playStateImage.setImageResource(playIcon);
                playState = OnPlayListener.PLAY_PAUSED;
                break;
        }
    }

    public void setProgress(int progress){
        if (progress > 100) {
            progress = 100;
        }
        if(progress < 0){
            progress = 0;
        }
        roundProgressBar.setProgress(100 - progress);
        audioView.onProgress(progress);
        setRecordTime(recordIntTime * progress / 100);
    }

    public void setProgressGone(){
        progressForLoading.setVisibility(View.GONE);
    }

    /**
     * 播放状态的回调
     */
    private OnPlayListener mPlayListener = new OnPlayListener() {
        @Override
        public void onPlayStateListener(String url, int state) {
            setState(state);
        }

        @Override
        public void onProgressListener(String url, int progress) {
            setProgress(progress);

        }

        @Override
        public void onPlayErrorListener(String url, int error) {
            setProgressGone();
        }
    };

    private OnDeleteClickListener mDeleteClickListener;

    public void setDeleteClickListener(OnDeleteClickListener listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick();
    }

    /**
     * 删除已存在的录音
     */
    private void deleteRecord() {
        if (mRecordPath != null) {
            File file = new File(mRecordPath);
            if (file.exists())
                file.delete();
        }
    }


}
