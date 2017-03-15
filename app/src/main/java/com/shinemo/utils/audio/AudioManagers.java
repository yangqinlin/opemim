package com.shinemo.utils.audio;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.text.TextUtils;

import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.ApplicationContext;
import com.shinemo.openim.http.filemanage.FileManage;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.openim.utils.Md5Util;
import com.shinemo.openim.utils.ToastUtil;
import com.shinemo.widget.audio.VoiceView;
import com.shinemo.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import rx.Subscriber;

public class AudioManagers {

    private static AudioManagers sInstance;

    private AudioManagers() {
        mContext = ApplicationContext.getInstance();
    }

    public static AudioManagers getInstance() {
        if (sInstance == null) {
            sInstance = new AudioManagers();
        }
        return sInstance;
    }

    private static volatile boolean VOICE_RECORD_NOT_TUNNING = false;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private Lock mPlayerLock = new ReentrantLock();// 锁对象
    private Context mContext;
    // 当前播放的声音
    public String mCurUrl;
    // mCurUrl对应的监听函数
    public OnPlayListener mCurPlayListener;
    private List<String> mDownloadingPool = new ArrayList<String>();
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int EVERY_TIME = 50;
    // 录音监听函数
    private OnRecordListener mCurRecordListener;
    private OnRecordVoiceListener mRecordVoiceListener;

    private Lock mRecordLock = new ReentrantLock();// 锁对象
    private File mCurRecordFile;
    private long mRecordStartTime;

    //如果是网络文件，未加载完毕给出提示
//    private ProgressBar progressBar;


//    public void play(final String url,OnPlayListener listener,ProgressBar progressBar){
//        this.progressBar = progressBar;
//        this.play(url,listener);
//    }

    public void play(final String url, OnPlayListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        stopPlay();
        stopRecord();
        mCurUrl = url;
        mCurPlayListener = listener;
        // 本地文件直接播放
        if (isLocalFile(mCurUrl)) {
            doPlayLocalFile(mCurUrl);
        } else {
            // Request audioRequest = null;
            // //判断是否进入了下载队列

            File dir = FileUtils.getRecordPath(mContext);
            File file = new File(dir, Md5Util.getStringMD5(url));
            if (file.exists()) {
                doPlayLocalFile(file.getAbsolutePath());
                return;
            }
            synchronized (mDownloadingPool) {
                if (mDownloadingPool.contains(url)) {
                    return; // 已经在下载队列则直接返回
                } else {
                    mDownloadingPool.add(url); // 加入下载队列
                }
            }

            FileManage.getInstance().downloadFile(mCurUrl, FileUtils.getRecordPath(mContext).getAbsolutePath()).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mDownloadingPool.remove(url);
                    if (mCurPlayListener != null) {
                        mCurPlayListener.onPlayErrorListener(url,
                                OnPlayListener.DOWNLOAD_RECORD_ERROR);
                        mCurPlayListener = null;
                    }
                    mCurUrl = null;
                }

                @Override
                public void onNext(String data) {
                    mDownloadingPool.remove(url);
                    doPlayLocalFile(data);
                }
            });
//            FileManage.getInstance().downloadFile(mCurUrl, FileUtils.getRecordPath(mContext).getAbsolutePath(), new DefaultCallback<String>(mContext) {
//                @Override
//                public void onDataSuccess(String data) {
//
//                }
//
//                @Override
//                public void onFail(int code, String reason) {
//                    super.onFail(code, reason);
//                    mDownloadingPool.remove(url);
//                    if (mCurPlayListener != null) {
//                        mCurPlayListener.onPlayErrorListener(url,
//                                OnPlayListener.DOWNLOAD_RECORD_ERROR);
//                        mCurPlayListener = null;
//                    }
//                    mCurUrl = null;
//                }
//            });
        }
    }

    public void pause(String url) {
        // 只能是暂停当前播放的声音
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(mCurUrl)
                || (url.compareTo(mCurUrl) != 0)) {
            return;
        }

        mPlayerLock.lock();
        if (mPlayer == null) // we were not in playback
        {
            mPlayerLock.unlock();
            return;
        }

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            stopProgress();
            if (mCurPlayListener != null) {
                mCurPlayListener.onPlayStateListener(mCurUrl,
                        OnPlayListener.PLAY_PAUSED);
            }
        }
        mPlayerLock.unlock();
    }

    public void resume(String url) {
        // 只能恢复当前播放的声音
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(mCurUrl)
                || (url.compareTo(mCurUrl) != 0)) {
            return;
        }

        mPlayerLock.lock();
        if (mPlayer == null) // we were not in playback
        {
            mPlayerLock.unlock();
            return;
        }

        if (!mPlayer.isPlaying()) {
            mPlayer.start();
            timerShowProgress();
            if (mCurPlayListener != null) {
                mCurPlayListener.onPlayStateListener(mCurUrl,
                        OnPlayListener.PLAY_START);
            }
        }
        mPlayerLock.unlock();
    }

    public void stop(String url) {
        // 只能是停止当前播放的声音
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(mCurUrl)
                || (url.compareTo(mCurUrl) != 0)) {
            return;
        }

        stopPlay();
    }

    public String record(OnRecordListener recordListener, OnRecordVoiceListener voiceListener){
        mRecordVoiceListener = voiceListener;
        return record(recordListener);
    }

    public String record(OnRecordListener listener) {
        String path = null;
        // 录音之前停止播放
        stopPlay();

        mRecordLock.lock();
        // 如果在录音中则直接返回当前录音文件的路径，否则开启一个录音
        // Handle IOException
        try {
            if (mRecorder == null) {
                mCurRecordListener = listener;
                mRecorder = new MediaRecorder();
                if (!VOICE_RECORD_NOT_TUNNING) {
                    mRecorder
                            .setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
                    mRecorder.setAudioEncodingBitRate(5525);// 最小码率
                } else {
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                }
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mCurRecordFile = createRecordFile();
                mRecorder.setOutputFile(mCurRecordFile.getAbsolutePath());
                mRecordStartTime = System.currentTimeMillis();
                mRecorder.prepare();
                timerShowProgress();
                mRecorder.start();
                if (mCurRecordListener != null) {
                    mCurRecordListener
                            .onRecordStateListener(OnRecordListener.RECORD_START);
                }
            }
        } catch (Exception exception) {
            ToastUtil.show(ApplicationContext.getInstance(), R.string.record_fail);
            if(mRecorder != null){
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            }
            stopProgress();
            if (mCurRecordFile != null) {
                mCurRecordFile.delete();
                mCurRecordFile = null;
            }

            if (mCurRecordListener != null) {
                mCurRecordListener
                        .onRecordErrorListener(OnRecordListener.IN_RECORD_ERROR);
                mCurRecordListener = null;
            }
            mRecordStartTime = 0;
            
            return null;
        } finally {
            mRecordLock.unlock();
        }

        if (mCurRecordFile != null) {
            path = mCurRecordFile.getAbsolutePath();
        }

        return path;
    }

    public int stopRecord() {
        mRecordLock.lock();
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                if (mCurRecordListener != null) {
                    mCurRecordListener
                            .onRecordStateListener(OnRecordListener.RECORD_COMPLEMENTED);
                    mCurRecordListener = null;
                    mCurRecordFile = null;
                }
            }
        } catch (RuntimeException e) {
            if (mCurRecordFile != null) {
                mCurRecordFile.delete();
                mCurRecordFile = null;
            }

            if (mCurRecordListener != null) {
                mCurRecordListener
                        .onRecordErrorListener(OnRecordListener.IN_RECORD_ERROR);
                mCurRecordListener = null;
            }
        } finally {
            if (mRecorder != null) {
                mRecorder.release();
                stopProgress();
                mRecorder = null;
            }
            mRecordLock.unlock();
        }
        if (mRecordStartTime != 0) {
            return (int) ((System.currentTimeMillis() - mRecordStartTime) / 1000);
        } else {
            return 0;
        }
    }

    public int getRecordLength() {
        if (mRecordStartTime != 0) {
            return (int) ((System.currentTimeMillis() - mRecordStartTime) / 1000);
        }
        return 0;
    }

    /**
     * 判断url指定的是否为本地文件
     *
     * @param url TODO
     */
    private boolean isLocalFile(String url) {
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme) || (scheme.compareTo("file") == 0)
                || (scheme.compareTo("content") == 0)) {
            return true;
        }

        return false;
    }

    private void doPlayLocalFile(String path) {
        mPlayerLock.lock();
        mPlayer = new MediaPlayer();
        mPlayer.reset();

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(path);
            //增加fd的处理是为了避免没有外部存储语音无法播放的问题
            if (fis != null && fis.getFD() != null) {
                mPlayer.setDataSource(fis.getFD());
            } else {
                mPlayer.setDataSource(path);
            }
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mCurPlayListener != null)
                        mCurPlayListener.onPlayStateListener(mCurUrl,
                                OnPlayListener.PLAY_START);
                }

            });

            mPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopProgress();
                    stopPlay();
                    if (mCurPlayListener != null) {
                        mCurPlayListener.onPlayStateListener(mCurUrl,
                                OnPlayListener.PLAY_COMPLEMENTED);
                        mCurPlayListener = null;
                    }
                    mCurUrl = null;
                }
            });

            mPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    stopProgress();
                    stopPlay();
                    if (mCurPlayListener != null) {
                        mCurPlayListener.onPlayErrorListener(mCurUrl,
                                OnPlayListener.IN_CALL_RECORD_ERROR);
                        mCurPlayListener = null;
                    }
                    mCurUrl = null;

                    return false;
                }

            });

            mPlayer.prepare();
            timerShowProgress();
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            if (mCurPlayListener != null) {
                mCurPlayListener.onPlayErrorListener(mCurUrl,
                        OnPlayListener.INTERNAL_ERROR);
                mCurPlayListener = null;
            }
            mCurUrl = null;
            mPlayer = null;
            e.printStackTrace();
            return;
        } catch (IllegalStateException e) {
            if (mCurPlayListener != null) {
                mCurPlayListener.onPlayErrorListener(mCurUrl,
                        OnPlayListener.INTERNAL_ERROR);
                mCurPlayListener = null;
            }
            mCurUrl = null;
            mPlayer = null;
            e.printStackTrace();
            return;
        } catch (SecurityException e) {
            if (mCurPlayListener != null) {
                mCurPlayListener.onPlayErrorListener(mCurUrl,
                        OnPlayListener.INTERNAL_ERROR);
                mCurPlayListener = null;
            }
            mCurUrl = null;
            mPlayer = null;
            e.printStackTrace();
            return;
        } catch (IOException e) {
            if (mCurPlayListener != null) {
                mCurPlayListener.onPlayErrorListener(mCurUrl,
                        OnPlayListener.IO_ACCESS_ERROR);
                mCurPlayListener = null;
            }
            mCurUrl = null;
            mPlayer = null;
            e.printStackTrace();
            return;
        } finally {
            mPlayerLock.unlock();
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPlay() {
        mPlayerLock.lock();
        if (mPlayer == null) // we were not in playback
        {
            mPlayerLock.unlock();
            return;
        }

        mPlayer.stop();
        mPlayer.release();
        stopProgress();
        if (mCurPlayListener != null) {
            mCurPlayListener.onPlayStateListener(mCurUrl,
                    OnPlayListener.PLAY_STOPED);
        }
        mCurUrl = null;
        mPlayer = null;
        mPlayerLock.unlock();
    }

    private void timerShowProgress() {
        mTimer = new Timer("VoiceRecorder");
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                showPlayProgress();
                showRecordProgress();
                showRecordVoice();
            }
        };
        mTimer.schedule(mTimerTask, 0, EVERY_TIME);
    }

    private void showRecordVoice() {
        if(mRecordVoiceListener != null){
            if(mRecorder != null && mCurRecordFile != null && mRecordStartTime != 0){
                Handlers.MAIN.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecordVoiceListener != null && mRecorder != null) {
                            try {
                                int voice = VoiceView.MAX_LEVEL *
                                        mRecorder.getMaxAmplitude() / OnRecordVoiceListener.MAX_AMPLITUDE + 1;
                                if(voice > VoiceView.MAX_LEVEL )
                                    voice =  VoiceView.MAX_LEVEL;
                                mRecordVoiceListener.onRecordVoice(voice);
                            } catch (Exception e) {
                                mRecordVoiceListener.onRecordVoice(VoiceView.MIN_LEVEL);
                            }
                        }
                    }
                });
            }
        }
    }

    private void showRecordProgress() {
        if (mCurRecordListener != null) {
            if (mRecorder != null && mCurRecordFile != null && mRecordStartTime != 0) {
                Handlers.MAIN.post(new Runnable() { //推送给主线程
                    @Override
                    public void run() {
                        if (mCurRecordListener != null)
                            mCurRecordListener.onRecordProgressListener(getRecordLength());
                    }
                });
            }
        }

    }

    private void showPlayProgress() {
        try {
            if (mCurPlayListener != null) {
                if (mPlayer != null && mPlayer.isPlaying() && mPlayer.getDuration() > 0
                        && mPlayer.getCurrentPosition() > 0) {
                    Handlers.MAIN.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mPlayer != null) {
                                mCurPlayListener.onProgressListener(mCurUrl,
                                        mPlayer.getCurrentPosition() * 100 / mPlayer.getDuration());
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
        }
    }

    public void stopProgress() {
        stopTimer();
    }

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

    private File createRecordFile() {
        File dir = FileUtils.getRecordPath(mContext);
        File file = new File(dir, "" + System.currentTimeMillis());
        return file;
    }

    /**
     * 获得本地录音文件的长度
     */
    public int getRecordDuration(String recordUrl) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(recordUrl); //在获取前，设置文件路径（应该只能是本地路径）
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release(); //释放
        int dur = 0;
        if (!TextUtils.isEmpty(duration)) {
            dur = Integer.parseInt(duration);
        }
        return dur;
    }
}