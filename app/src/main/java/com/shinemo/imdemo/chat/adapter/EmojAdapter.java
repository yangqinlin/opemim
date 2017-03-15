package com.shinemo.imdemo.chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.shinemo.imdemo.chat.adapter.EmojView.BaseEmojFragment;
import com.shinemo.imdemo.chat.adapter.EmojView.EmojSmileFragment;

/**
 * Created by zale on 16/1/20.
 */
public class EmojAdapter extends FragmentStatePagerAdapter {


    private int itemCount, height;
    private EmojType emojType;

    public enum EmojType {
        SMILE, CUSTOMSMILE, BIGSMILE, BIGSMILE2, BIGSMILE3, BIGSMILE4, BIGSMILE5
    }

    private EmojSmileFragment.OnSmileClick smileClick;

    public BaseEmojFragment[] emojFragments;

    public EmojAdapter(FragmentManager fm, EmojType emojType, BaseEmojFragment[] emojFragments, EmojSmileFragment.OnSmileClick smileClick,   int height) {
        super(fm);
        this.emojType = emojType;
        this.height = height;
        this.smileClick = smileClick;
        this.emojFragments = emojFragments;
        update();
    }

    public void update() {
        itemCount = 0;
        for (int i = 0; i < emojFragments.length; i++) {
            switch (i) {
                case 0:
                    if (emojFragments[0] == null) {
                        emojFragments[0] = EmojSmileFragment.newInstance(0, smileClick, height);
                    }
                    itemCount += emojFragments[0].getCount();
                    break;
            }
        }
    }

    public void setCurrentPosition(int position) {
        updateTypeByNum(position);
    }

    private void updateTypeByNum(int position) {
        for (int i = 0; i < EmojType.values().length; i++) {
            if (position >= getAllEmojFragmentCountByNum(i) && position < getAllEmojFragmentCountByNum(i + 1)) {
                emojType = EmojType.values()[i];
                break;
            }
        }
    }

    public int getEmojPages(int position) {
        for (int i = 0; i < emojFragments.length; i++) {
            if (position >= emojFragments[i].getCount()) {
                position = position - emojFragments[i].getCount();
            } else {
                return emojFragments[i].getCount();
            }
        }
        return 0;
    }

    public int getAllPrevTypeEmojPages(int position) {
        int maxCount = itemCount;
        for (int i = emojFragments.length - 1; i >= 0; i--) {
            if (position < maxCount) {
                maxCount = maxCount - emojFragments[i].getCount();
            } else {
                return maxCount;
            }
        }
        return 0;
    }

    @Override
    public Fragment getItem(int position) {
        for (int i = 0; i < EmojType.values().length; i++) {
            int temp = getAllEmojFragmentCountByNum(i + 1);
            if (position < temp) {
                emojType = EmojType.values()[i];
                switch (emojType) {
                    case SMILE:
                        return EmojSmileFragment.newInstance(position, smileClick, height);
                }
                break;
            }
        }
        return EmojSmileFragment.newInstance(0, smileClick, height);
    }

    @Override
    public int getItemPosition(Object object) {
        if(emojType== EmojType.CUSTOMSMILE){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public int getAllEmojFragmentCountByNum(int num) {
        int result = 0;
        for (int i = 0; i < num; i++) {
            result += emojFragments[i].getCount();
        }
        return result;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    public void setEmojType(EmojType emojType) {
        this.emojType = emojType;
    }

    public EmojType getEmojType() {
        return emojType;
    }

}
