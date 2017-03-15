package com.shinemo.imdemo.chat.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.chat.adapter.EmojAdapter;
import com.shinemo.imdemo.chat.adapter.EmojView.BaseEmojFragment;
import com.shinemo.imdemo.chat.adapter.EmojView.EmojSmileFragment;
import com.shinemo.openim.Constants;
import com.shinemo.openim.helper.UserSP;
import com.shinemo.openim.service.msg.attachment.AudioAttachment;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.openim.utils.ToastUtil;
import com.shinemo.utils.SmileUtils;
import com.shinemo.utils.audio.AudioManagers;
import com.shinemo.widget.audio.AudioView;
import com.shinemo.widget.fonticon.FontIcon;
import com.shinemo.widget.viewpage.ImageDotView;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

/**
 * Created by panjiejun on 2017/3/6.
 */

public class InputBarView extends LinearLayout {

    @BindView(R.id.chat_detail_voice)
    FontIcon chatDetailVoice;
    @BindView(R.id.chat_detail_text)
    EmojiconEditText mChatDetailText;
    @BindView(R.id.bottom_line)
    View bottomLine;
    @BindView(R.id.chat_smile_btn)
    FontIcon chatSmileBtn;
    @BindView(R.id.chat_detail_add)
    FontIcon chatDetailAdd;
    @BindView(R.id.chat_detail_send)
    Button chatDetailSend;
    @BindView(R.id.bottomContainer)
    LinearLayout bottomContainer;
    @BindView(R.id.audio)
    AudioView audioView;
    @BindView(R.id.chat_detail_gridview)
    View mAddLayout;
    @BindView(R.id.panel_root)
    KPSwitchPanelLinearLayout mPanelRoot;
    @BindView(R.id.chat_smile_layout)
    View mFaceLayout;
    @BindView(R.id.chat_smile_dot)
    ImageDotView mFaceDotView;
    @BindView(R.id.chat_smile_viewpage)
    ViewPager mFaceViewPage;
    @BindView(R.id.smileBar)
    SmileBar mSmileBar;

    private int emojPages = -1;
    private EmojAdapter.EmojType currentType = EmojAdapter.EmojType.SMILE;

    private Context mContext;

    private static final long PIC_SHOW_TIME_LIMIT = 600000;
    private MoreAction moreAction;
    private int mChatType;
    private String mCid;
    private RecyclerView mRvContent;
    private EmojAdapter emojAdapter;
    private FragmentManager fragmentManager;
    private BaseEmojFragment[] emojFragments = null;


    public InputBarView(Context context) {
        this(context, null);
    }

    public InputBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View meun = LayoutInflater.from(mContext).inflate(R.layout.chat_input_bar, this);
        ButterKnife.bind(this, meun);
        audioView.setAutoSend(true);
        audioView.setRecordCompleteListener(onRecordCompleteListener);
    }

    public void init(RecyclerView mRvContent, int chatType, String toId, FragmentManager fragmentManager, MoreAction moreAction){
        this.moreAction = moreAction;
        this.mChatType = chatType;
        this.mCid = toId;
        this.mRvContent = mRvContent;
        this.fragmentManager = fragmentManager;
        int resIds[] = {R.drawable.smile_bar_1};
        mSmileBar.setResIds(resIds);
        emojFragments = new BaseEmojFragment[1];
        mSmileBar.setSmileSelectListener(position -> {
            switch (position) {
                case 0:
                    itemSelected(EmojAdapter.EmojType.SMILE, position);
                    break;
            }
        });
    }

    private void itemSelected(EmojAdapter.EmojType type, int position) {
        emojAdapter.setEmojType(type);
        int page = 0;
        if (position < emojFragments.length) {
            for (int i = 0; i < position; i++) {
                page += emojFragments[i].getCount();
            }
        }
        //表情的页数
        mFaceViewPage.setCurrentItem(page);
        emojAdapter.notifyDataSetChanged();
    }

    public KPSwitchPanelLinearLayout getPanelRoot() {
        return mPanelRoot;
    }

    public void setmPanelRoot(KPSwitchPanelLinearLayout mPanelRoot) {
        this.mPanelRoot = mPanelRoot;
    }

    private AudioView.OnRecordCompleteListener onRecordCompleteListener = new AudioView.OnRecordCompleteListener() {
        @Override
        public void onRecordComplete(String filePath, int recordTime, int[] voide) {
            AudioManagers.getInstance().stopPlay();
            sendAudio(recordTime, filePath, voide, false);
        }
    };

    private void sendAudio(int duration, String path, int[] voice, boolean isBida) {
        if (duration < 1) {
            ToastUtil.show(mContext, R.string.record_too_short);
            return;
        }
        AudioAttachment attachment = new AudioAttachment();
        attachment.setVoice(voice);
        attachment.setPath(path);
        attachment.setDuration(duration);
        moreAction.sendAudio(attachment);
    }

    @OnClick(R.id.chat_detail_send)
    public void sendText() {
        String content = mChatDetailText.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            moreAction.sendText(content);
        }
    }

    @OnClick(R.id.take_photo)
    public void takePhoto() {
       if (moreAction != null) {
           moreAction.takePhoto();
       }
    }

    @OnClick(R.id.chat_detail_voice)
    public void voiceBtn() {
        //如果键盘弹出
        if (mPanelRoot.getHeight() == 0 || !mPanelRoot.isVisible()) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            KeyboardUtil.hideKeyboard(mChatDetailText);
            showAudio();
        } else if (mPanelRoot.isKeyboardShowing()) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            showAudio();
        } else if (audioView.getVisibility() == View.GONE) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            showAudio();
        } else {
            KPSwitchConflictUtil.showKeyboard(mPanelRoot, mChatDetailText);
            hideVoice();
        }
    }

    @OnClick(R.id.chat_detail_add)
    public void addBtn() {
        if (mPanelRoot.getHeight() == 0 || !mPanelRoot.isVisible()) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            KeyboardUtil.hideKeyboard(mChatDetailText);
            showAdd();
        } else if (mPanelRoot.isKeyboardShowing()) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            showAdd();
        } else if (mAddLayout.getVisibility() == View.GONE) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            showAdd();
        } else {
            KPSwitchConflictUtil.showKeyboard(mPanelRoot, mChatDetailText);
            hideAdd();
        }

    }

    @OnClick(R.id.chat_smile_btn)
    public void smileBtn() {
        if (mPanelRoot.getHeight() == 0 || !mPanelRoot.isVisible()) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            KeyboardUtil.hideKeyboard(mChatDetailText);
            showFace();
        } else if (mPanelRoot.isKeyboardShowing()) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            showFace();
        } else if (mFaceLayout.getVisibility() == View.GONE) {
            KPSwitchConflictUtil.showPanel(mPanelRoot);
            showFace();
        } else {
            KPSwitchConflictUtil.showKeyboard(mPanelRoot, mChatDetailText);
            hideSmile();
        }

    }

    private void showAudio() {
        audioView.setVisibility(View.VISIBLE);
        hidewithmain(audioView.getId());
    }

    /**
     * 根据id来判断当前应该隐藏那个页面
     *
     * @param id
     */
    private void hidewithmain(int id) {
        if (id == mFaceLayout.getId()) {
            hideVoice();
            hideAdd();
        } else if (id == audioView.getId()) {
            hideSmile();
            hideAdd();
        } else if (id == mAddLayout.getId()) {
            hideSmile();
            hideVoice();
        }
        setListEnd();
    }

    private void hideVoice() {
        audioView.setVisibility(View.GONE);
    }


    private void hidePanel() {
        KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
    }

    private void hideAdd() {
        mAddLayout.setVisibility(View.GONE);
    }

    private void hideSmile() {
        mFaceLayout.setVisibility(View.GONE);
    }

    private void showFace() {
        mFaceLayout.setVisibility(View.VISIBLE);
        //当高度发生变化或者适配器为空时
        if (oldSoftKeyHeight != KeyboardUtil.getValidPanelHeight(mContext) || emojAdapter == null) {
            oldSoftKeyHeight = KeyboardUtil.getValidPanelHeight(mContext);
            initEmojAdapter();

        }
        hidewithmain(mFaceLayout.getId());
    }


    private void initEmojAdapter() {
        emojAdapter = new EmojAdapter(fragmentManager, EmojAdapter.EmojType.SMILE, emojFragments, mSmileClickListener, KeyboardUtil.getValidPanelHeight(mContext));
        mFaceDotView.setImageCount(emojFragments[0].getCount());
        mFaceViewPage.setAdapter(emojAdapter);
        mFaceViewPage.addOnPageChangeListener(new PagerListener());
    }

    private EmojSmileFragment.OnSmileClick mSmileClickListener = new EmojSmileFragment.OnSmileClick() {
        @Override
        public void onSmileClick(String text) {
            SmileUtils.appendTextToInputText(text, mChatDetailText);
        }

        @Override
        public void onDeleteClick() {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_DEL);
            mChatDetailText.dispatchKeyEvent(event);
        }
    };

    public class PagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int pages = emojAdapter.getEmojPages(position);
            if (emojPages != pages) {
                emojPages = pages;
                mFaceDotView.setImageCount(emojPages);
            }
            emojAdapter.setCurrentPosition(position);
            mFaceDotView.setSelection(position - emojAdapter.getAllPrevTypeEmojPages(position));
            int daoselect = mSmileBar.getmCurPosition();
            if (currentType != emojAdapter.getEmojType() || currentType != EmojAdapter.EmojType.values()[daoselect]) {
                currentType = emojAdapter.getEmojType();
                switch (currentType) {
                    case SMILE:
                        mSmileBar.setCurSelectedItem(0);
                        break;
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setListEnd() {
        Handlers.MAIN.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (moreAction != null){
                    moreAction.setListEnd();
                }
            }
        }, 300);
    }

    private int oldSoftKeyHeight = 0;
    private void showAdd() {
        mAddLayout.setVisibility(View.VISIBLE);
        if (oldSoftKeyHeight != KeyboardUtil.getValidPanelHeight(mContext)) {
            oldSoftKeyHeight = KeyboardUtil.getValidPanelHeight(mContext);
//            chatAddAdapter = new ChatAddAdapter(getSupportFragmentManager(), mAddListener, mAddList, oldSoftKeyHeight);
//            mAddViewPage.setAdapter(chatAddAdapter);
//            mAddViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                    mAddDotView.setSelection(position);
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            });
        }
//        if (chatAddAdapter.getCount() > 1) {
//            mAddDotView.setVisibility(View.VISIBLE);
//            mAddDotView.setImageCount(chatAddAdapter.getCount());
//        } else {
//            mAddDotView.setVisibility(View.GONE);
//        }
        hidewithmain(mAddLayout.getId());

        RxPermissions.getInstance(mContext)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        showPicShortCut();
                    }
                });
    }

    private void showPicShortCut() {
        String lastPic;
        long currentTime = System.currentTimeMillis(); //当前时间
        long lastTakenTime;  //最新拍照时间
        // 上次获取到的照片
        String lastPicSaved = UserSP.getInstance().getString(Constants.EXTRA_PIC_LAST_SAVE);
        ContentResolver mResolver = mContext.getContentResolver();
        Cursor cursor = mResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN}, null, null, "datetaken desc");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    lastTakenTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                    lastPic = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (currentTime - lastTakenTime <= PIC_SHOW_TIME_LIMIT && !lastPicSaved.equals(lastPic)) {
//                        ll_pic_shortcut.setVisibility(View.VISIBLE);
//                        im_pic_shortcut.setImageURI(Uri.parse("file://" + lastPic));
//                        timerShowPicShortCut();
//                        SharePrefsManager.getInstance().putString(Constants.EXTRA_PIC_LAST_SAVE, lastPic);
//                        im_pic_shortcut.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                SinglePictureActivity.startActivity(ChatDetailActivity.this, lastPic, CameraUtils.REQUEST_PHOTO_SHORT_CUT);
//                            }
//                        });
                    }
                }
            } finally {
                cursor.close();
            }

        }
    }

    public interface MoreAction{
        void sendAudio(AudioAttachment attachment);

        void sendText(String content);

        void takePhoto();

        void setListEnd();
    }

}
