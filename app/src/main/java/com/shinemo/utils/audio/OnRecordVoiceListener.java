package com.shinemo.utils.audio;

/**
 * 录音音量监听接口
 * Created by Yetwish on 2015/9/19.
 */
public interface OnRecordVoiceListener {

    public static final int MAX_AMPLITUDE = 15000;

    void onRecordVoice(int voiceLevel);
}
