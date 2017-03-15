package com.shinemo.widget.audio;

import android.Manifest;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.widget.dialog.TipsDialog;
import com.shinemo.widget.round.CommonRound;
import com.shinemo.utils.audio.AudioManagers;
import com.shinemo.utils.audio.OnPlayListener;
import com.shinemo.utils.audio.OnRecordListener;
import com.shinemo.utils.audio.OnRecordVoiceListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yetwish on 2015/8/19.
 */
public class AudioView extends RelativeLayout {

    private static final int STATE_NONE = -1;

    private static final int POS_BG = -1;
    private static final int POS_RECORD = 0x01;
    private static final int POS_PLAY = 0x02;
    private static final int POS_CANCEL = 0x03;

    /**
     * 最短录音时间
     */
    private static final int MIN_RECORD_TIME = 1;
    RelativeLayout.LayoutParams lpBig = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.youban_record_big),
            (int) getResources().getDimension(R.dimen.youban_record_big));
    RelativeLayout.LayoutParams lpSmall = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.youban_record_small),
            (int) getResources().getDimension(R.dimen.youban_record_small));
    RelativeLayout.LayoutParams lpText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private Context mContext;
    private TextView tvTips;
    private CommonRound ivRecord;
    private ImageView ivPlay;
    private ImageView ivCancel;
    private VoiceView mLeftVoice;
    private VoiceView mRightVoice;
    private RelativeLayout llCenter;
    /**
     * 记录当前播放状态
     */
    private int mPlayState = STATE_NONE;
    /**
     * 记录当前录音状态
     */
    private int mRecordState = STATE_NONE;
    /**
     * 标志是否点击了cancel按钮
     */
    private boolean toCancel;
    /**
     * 标志是否点击了play按钮
     */
    private boolean toPlay;
    /**
     * 标志是否点击了send按钮
     */
    private boolean toSend;
    /**
     * 音频管理器，用于播放音频 和录制音频
     */
    private AudioManagers mAudioManagers;
    /**
     * 录音完成监听器，将录音结果传递给监听者
     */
    private OnRecordCompleteListener mRecordCompleteListener;
    /**
     * 录音文件保存的绝对路径
     */
    private String mRecordFilePath;
    /**
     * 录音时长
     */
    private int mRecordTime;
    /**
     * 是否松开自动发送  两种模式
     */
    private boolean isAutoSend;
    /**
     * 最长录音时间 单位s
     */
    private int mMaxRecordTime;

    /**
     * 是不是必达消息
     */
    private boolean isBiDa=false;
    /**
     * 自定义开始颜色
     */
    private String startColor = "-1";
    /**
     * 自定义结束颜色
     */
    private String endColor = "-1";


    /**
     * 提示dialog
     */
    private TipsDialog mTipsDialog;
    /**
     * 计时器
     */
    private Timer mTimer;

    private TextView recordStateTV;
    private View voiceActionLayout;
    private View audioRootView;

    /**
     * 计时器执行的任务
     */
    private TimerTask mTimerTask;
    private OnRecordListener mRecordListener = new OnRecordListener() {
        @Override
        public void onRecordStateListener(int state) {
            mRecordState = state;
            notifyUiSetChanged();
            if (mRecordState == RECORD_START) { //开始录音
                tvTips.setTextColor(getResources().getColor(R.color.c_brand));
                tvTips.setText("0:00");
                if (isAutoSend) {
                    setBtnVisibility(true);
                    showTipsDialog(TipsDialog.STYLE_LINE2, null);
                }
                mLeftVoice.setVisibility(VISIBLE);
                mRightVoice.setVisibility(VISIBLE);
            } else if (mRecordState == RECORD_COMPLEMENTED) { //录音完成
                tvTips.setText("0:" + String.format("%02d", mRecordTime));
                if (mRecordTime < 1) {//录音时间太短
                    showTipsDialog(TipsDialog.STYLE_LINE1, null);
                    clearState();
                    delayToDismissDialog(500);
                } else {
                    dismissDialog();
                    if (!isAutoSend)
                        setBtnVisibility(true);
                    else if (mRecordTime == mMaxRecordTime)
                        completeRecord();

                }

            }

        }

        @Override
        public void onRecordErrorListener(int error) {

        }

        @Override
        public void onRecordProgressListener(int recordTime) {
            if (recordTime >= mMaxRecordTime) {
                //完成录音
                mRecordTime = mMaxRecordTime;
                stopRecord();
                return;
            }
            if (mRecordTime != recordTime) {
                mRecordTime = recordTime;
                if (mMaxRecordTime - mRecordTime < 10) {
                    showTipsDialog(TipsDialog.STYLE_NUM, (mMaxRecordTime - mRecordTime) + "");
//                    delayToDismissDialog(1000);
                }
                tvTips.setText("0:" + String.format("%02d", mRecordTime));
            }
        }
    };
    private OnRecordVoiceListener mVoiceListener = new OnRecordVoiceListener() {
        @Override
        public void onRecordVoice(int voiceLevel) {
            mRightVoice.addVoice(voiceLevel);
            mLeftVoice.addVoice(voiceLevel);
        }
    };
    private OnPlayListener mPlayListener = new OnPlayListener() {
        @Override
        public void onPlayStateListener(String url, int state) {
            if (state == PLAY_STOPED) return; //不保存停止状态
            mPlayState = state;
            notifyUiSetChanged();
            if (mPlayState == PLAY_COMPLEMENTED) {
//                mLeftVoice.setVisibility(VISIBLE);
//                mRightVoice.setVisibility(VISIBLE);
                mLeftVoice.onComplete();
                mRightVoice.onComplete();
                tvTips.setText("0:" + String.format("%02d", mRecordTime));
            }
            if (mPlayState == PLAY_START) {
                mLeftVoice.listen();
                mRightVoice.listen();
            }
        }

        @Override
        public void onProgressListener(String url, int progress) {
            if (progress > 100)
                progress = 100;
            int time = mRecordTime - Math.round(mRecordTime * progress * 1.0f / 100);
            if (tvTips != null) {
                tvTips.setText("0:" + String.format("%02d", time));
            }
            if (mLeftVoice != null) {
                mLeftVoice.onProgress(progress);
            }
            if (mRightVoice != null) {
                mRightVoice.onProgress(progress);
            }
        }

        @Override
        public void onPlayErrorListener(String url, int error) {
            //播放错误
        }
    };

    public AudioView(Context context, boolean isAutoSend) {
        super(context);
        this.mContext = context;
        this.isAutoSend = isAutoSend;
        init();
    }

    public AudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.AudioView);
        this.isAutoSend = ta.getBoolean(R.styleable.AudioView_isAutoSend, false);
        this.mMaxRecordTime = ta.getInt(R.styleable.AudioView_recordMaxLength, 60);
        this.startColor = ta.getString(R.styleable.AudioView_startColor);
        this.endColor = ta.getString(R.styleable.AudioView_endColor);
        this.isBiDa=ta.getBoolean(R.styleable.AudioView_isBiDa,false);
        ta.recycle();
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        mAudioManagers = AudioManagers.getInstance();
        if(isBiDa){
            LayoutInflater.from(mContext).inflate(R.layout.bida_record_layout, this);
        }else{
            LayoutInflater.from(mContext).inflate(R.layout.record_layout, this);
        }
        voiceActionLayout = findViewById(R.id.voice_action_layout);
        audioRootView = findViewById(R.id.audio_root_view);
        tvTips = (TextView) findViewById(R.id.tv_record_tips);
        ivRecord = (CommonRound) findViewById(R.id.iv_record);
        int stcolor = -1, ecolor = -1;
        if (startColor != null && endColor != null && !startColor.equals("-1") && !endColor.equals("-1")) {
            try {
                stcolor = Color.parseColor(startColor);
                ecolor = Color.parseColor(endColor);
                ivRecord.setStartColor(stcolor);
                ivRecord.setEndColor(ecolor);
                ivRecord.setGradient(true);
            } catch (Exception e) {
                e.printStackTrace();
                stcolor = -1;
                ecolor = -1;
            }
        }

        ivPlay = (ImageView) findViewById(R.id.iv_record_play);
        if(isBiDa){
            ivPlay.setBackgroundResource(R.drawable.yb_btn_yy_bida_st);
        }
        ivCancel = (ImageView) findViewById(R.id.iv_record_cancel);
        mLeftVoice = (VoiceView) findViewById(R.id.left_voice);
        mLeftVoice.setMode(VoiceView.MODE_LEFT);
        mRightVoice = (VoiceView) findViewById(R.id.right_voice);
        mRightVoice.setMode(VoiceView.MODE_RIGHT);
        llCenter = (RelativeLayout) findViewById(R.id.ll_center);
        recordStateTV = (TextView) findViewById(R.id.recordState);
        lpText.addRule(CENTER_IN_PARENT);
        setBtnVisibility(false);
    }

    public void setThemeColor(int color) {
        audioRootView.setBackgroundColor(color);
    }

    /**
     * 设置必达消息的显示界面
     */
    public void setBidaMode() {
//        ViewGroup.LayoutParams rootLayout = audioRootView.getLayoutParams();
//        rootLayout.height += CommonUtils2.dip2px(getContext(), 30);
        setThemeColor(getResources().getColor(R.color.c_ff));
//        RelativeLayout.LayoutParams actionLayoutLayoutParams = (LayoutParams) voiceActionLayout.getLayoutParams();
//        actionLayoutLayoutParams.topMargin = CommonUtils2.dip2px(getContext(), 30);
    }

    /**
     * 设置监听
     *
     * @param listener 录音完成监听器
     */
    public void setRecordCompleteListener(OnRecordCompleteListener listener) {
        this.mRecordCompleteListener = listener;
    }

    public void setAutoSend(boolean autoSend) {
        this.isAutoSend = autoSend;
    }

    /**
     * 设置左右两个btn的可见性 {@link #ivPlay} {@link #ivCancel}
     *
     * @param visibility boolean
     */
    private void setBtnVisibility(boolean visibility) {
        if (visibility) {
            ivPlay.setVisibility(VISIBLE);
            ivCancel.setVisibility(VISIBLE);
        } else {
            ivPlay.setVisibility(GONE);
            ivCancel.setVisibility(GONE);
        }
    }

    /**
     * 清除状态
     */
    private void clearState() {
        toSend = false;
        toPlay = false;
        toCancel = false;
        mPlayState = STATE_NONE;
        mRecordState = STATE_NONE;
        mRecordFilePath = null;
        mRecordTime = 0;
        setBtnVisibility(false);
        tvTips.setTextColor(getResources().getColor(R.color.voice_normal));
        tvTips.setText(getResources().getString(R.string.max_record_length));
        mLeftVoice.clear();
        mRightVoice.clear();
        mLeftVoice.setVisibility(VISIBLE);
        mRightVoice.setVisibility(VISIBLE);
        notifyUiSetChanged();
    }

    public boolean hasRecord() {
        return !TextUtils.isEmpty(mRecordFilePath);
    }

    /**
     * 开始播放
     */
    private void play() {
        if (mRecordFilePath != null) {
//            if (mLeftVoice.isShown()) {
//                mLeftVoice.setVisibility(GONE);
//                mRightVoice.setVisibility(GONE);
//            }
            if (mPlayState == OnPlayListener.PLAY_PAUSED) {
                mAudioManagers.resume(mRecordFilePath);
            } else if (mPlayState == STATE_NONE || mPlayState == OnPlayListener.PLAY_COMPLEMENTED) {
                dismissDialog();
                mAudioManagers.play(mRecordFilePath, mPlayListener);
            }
        }
    }

    /**
     * 暂停播放
     */
    private void pause() {
        if (mRecordFilePath != null && mPlayState == OnPlayListener.PLAY_START) {
            mAudioManagers.pause(mRecordFilePath);
        }
    }

    /**
     * 开始录音
     */
    private void startRecord() {
        RxPermissions.getInstance(getContext())
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe(granted -> {
                    if (granted) {
                        mRecordFilePath = mAudioManagers.record(mRecordListener, mVoiceListener);
                    }
                });
    }

    /**
     * 录音
     */
    public void stopRecord() {
        if (mRecordFilePath != null) {
            mRecordTime = mAudioManagers.stopRecord();
        }
    }

    /**
     * 删除最近一次录音
     */
    private void deleteRecord() {
        if (mRecordFilePath != null) {
            File file = new File(mRecordFilePath);
            if (file.exists())
                file.delete();
        }
    }

    /**
     * 取消录音
     */
    private void cancel() {
        if (mPlayState == OnPlayListener.PLAY_START) {
            mAudioManagers.stop(mRecordFilePath);
        }
        dismissDialog();
        stopRecord();
        deleteRecord();
        clearState();
    }

    /**
     * 完成/发送录音
     */
    private void completeRecord() {
        if (mRecordCompleteListener != null && mRecordFilePath != null && mRecordTime >= MIN_RECORD_TIME) {
            mRecordCompleteListener.onRecordComplete(mRecordFilePath, mRecordTime, mLeftVoice.getVoiceArray());
        }
        //清除状态
        clearState();
    }

    /**
     * 显示对话框
     *
     * @param style
     * @param text
     */
    private void showTipsDialog(int style, String text) {
        if (mTipsDialog == null) {
            mTipsDialog = new TipsDialog(mContext, style);
        } else
            mTipsDialog.setStyle(style);
        if (style == TipsDialog.STYLE_NUM)
            mTipsDialog.setContent(text);
        if (!mTipsDialog.isShowing()) {
            mTipsDialog.show();
        }
    }

    /**
     * 延迟一段时间后关闭dialog
     */
    private void delayToDismissDialog(long delay) {
        mTimer = new Timer("dismiss dialog");
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                dismissDialog();
                stopTimer();
            }
        };
        mTimer.schedule(mTimerTask, delay, 1000);
    }

    /**
     * 关闭dialog
     */
    private void dismissDialog() {
        if (mTipsDialog != null && mTipsDialog.isShowing()) {
            mTipsDialog.dismiss();
        }
    }

    /**
     * 停止计时器
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 修改之前
         float x = event.getX();
         float y = event.getY();
         - tvTips.getBottom();
         **/
        float x = event.getRawX();
        float y = event.getRawY();

        int position = getPosition(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (position == POS_RECORD) {
                    if (mRecordState == STATE_NONE) {
                        startRecord();
                    } else if (mRecordState == OnRecordListener.RECORD_COMPLEMENTED) {
                        toSend = true;
                    }
                } else if (position == POS_PLAY) {
                    toPlay = true;
                } else if (position == POS_CANCEL) {
                    toCancel = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isAutoSend || !isAutoSend && mRecordState == OnRecordListener.RECORD_COMPLEMENTED) {
                    if (position == POS_PLAY) {
                        toPlay = true;
                    } else if (position == POS_CANCEL) {
                        toCancel = true;
                    } else if (position == POS_RECORD && mRecordState == OnRecordListener.RECORD_COMPLEMENTED) {
                        toSend = true;
                    } else {
                        toPlay = false;
                        toCancel = false;
                        toSend = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (toPlay) { //判断是否点击了play按钮
                    if (mPlayState == OnPlayListener.PLAY_START) {
                        pause();
                    } else {
                        play();
                    }
                    toPlay = false;
                }
                if (toCancel) { //判断是否点击了cancel按钮
                    cancel();
                    toCancel = false;
                }
                if (toSend) { //判断是否点击了send按钮
                    completeRecord();
                    toSend = false;
                }
                if (mRecordState == OnRecordListener.RECORD_START) {
                    //正在录音
                    if (isAutoSend) {//自动发送Mode
                        stopRecord();
                        if (mRecordTime >= MIN_RECORD_TIME) {
                            completeRecord();
                        } else {
                            deleteRecord();
                            clearState();
                        }
                    } else {  // 不自动发送Mode
                        stopRecord();
                        if (mRecordTime < MIN_RECORD_TIME) {
                            deleteRecord();
                            clearState();
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_CANCEL:
                cancel();
                toCancel = false;
                break;
        }
        notifyUiSetChanged();
        return true; //屏蔽父控件
    }

    /**
     * 根据当前播放state  {@link #mPlayState} 和record state  {@link #mRecordState}
     * 设置播放按钮和录音按钮的背景;
     */
    private void notifyUiSetChanged() {

        int playBgId = R.drawable.yb_btn_yy_st;
        if(isBiDa){
            playBgId = R.drawable.yb_btn_yy_bida_st;
        }

        int recordTextId = R.string.press_to_speak;
        RelativeLayout.LayoutParams lp = lpBig;

        //两套切图
        switch (mPlayState) {
            case OnPlayListener.PLAY_START://开始播放
                playBgId = R.drawable.yb_btn_yy_stz;
                if(isBiDa){
                    playBgId = R.drawable.yb_btn_yy_bida_stz;
                }
                tvTips.setTextColor(getResources().getColor(R.color.c_brand));
                break;
            case OnPlayListener.PLAY_PAUSED://暂停播放
            case STATE_NONE: //未开始播放
            case OnPlayListener.PLAY_COMPLEMENTED: //播放完成
                playBgId = R.drawable.yb_btn_yy_st;
                if(isBiDa){
                    playBgId = R.drawable.yb_btn_yy_bida_st;
                }
                break;
        }
        if (toPlay) {
            playBgId = R.drawable.yb_btn_yy_stz;
            if(isBiDa){
                playBgId = R.drawable.yb_btn_yy_bida_stz;
            }
        }
        ivPlay.setImageResource(playBgId);
        switch (mRecordState) {
            case STATE_NONE://未开始录音
                recordTextId = R.string.press_to_speak;
                lp = lpBig;
                break;
            case OnRecordListener.RECORD_START://开始录音
                if (isAutoSend) {
                    recordTextId = R.string.release_to_send;
                    lp = lpSmall;
                } else {
                    if(isBiDa){
                        recordTextId = R.string.recording_1;
                    }else{
                        recordTextId = R.string.recording;
                    }
                    lp = lpBig;
                }
                break;
            case OnRecordListener.RECORD_COMPLEMENTED://录音完成
                if (isAutoSend) {
                    recordTextId = R.string.send;
                    lp = lpBig;
                } else {
                    recordTextId = R.string.finish;
                    lp = lpBig;
                }
                break;
        }
        if (toSend) {
            if (isAutoSend) {
                recordTextId = R.string.send;
                lp = lpBig;
            } else {
                recordTextId = R.string.finish;
                lp = lpBig;
            }
        }
        recordStateTV.setText(getResources().getString(recordTextId));
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        if(!isBiDa){
            ivRecord.setLayoutParams(lp);
        }
        recordStateTV.setLayoutParams(lpText);
        if (!toCancel) {
            if (isAutoSend)
                ivCancel.setImageResource(R.drawable.xx_yy_btn_qx);
            else
                ivCancel.setImageResource(R.drawable.yb_btn_yy_sx);
        } else {
            if (isAutoSend)
                ivCancel.setImageResource(R.drawable.xx_yy_btn_qx_press);
            else
                ivCancel.setImageResource(R.drawable.yb_btn_yy_sx_press);
        }
    }

    /**
     * 根据当前触碰点坐标获取当前处于哪个控件上
     *
     * @return 当前所处位置  {@link #POS_BG} {@link #POS_PLAY} {@link #POS_CANCEL} {@link #POS_RECORD} 4种位置之一
     */
    private int getPosition(float x, float y) {
        int position = POS_BG;
        int[] cancel = new int[2];
        int[] record = new int[2];
        int[] play = new int[2];
        ivCancel.getLocationOnScreen(cancel);
        ivRecord.getLocationOnScreen(record);
        ivPlay.getLocationOnScreen(play);
        if (x >= record[0] && x <= record[0] + ivRecord.getWidth() && y >= record[1] && y <= record[1] + ivRecord.getHeight())
            position = POS_RECORD;
        else if (x >= play[0] && x <= play[0] + ivPlay.getWidth() && y >= play[1] && y <= play[1] + ivPlay.getHeight())
            position = POS_PLAY;
        else if (x >= cancel[0] && x <= cancel[0] + ivCancel.getWidth() && y >= cancel[1] && y <= cancel[1] + ivCancel.getHeight())
            position = POS_CANCEL;
        return position;
    }

    /**
     * 录音完成监听接口
     */
    public interface OnRecordCompleteListener {

        /**
         * 返回音频文件url 以及时长，
         *
         * @param filePath
         * @param recordTime
         */
        void onRecordComplete(String filePath, int recordTime, int[] voice);
    }
}


