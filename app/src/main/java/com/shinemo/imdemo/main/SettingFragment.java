package com.shinemo.imdemo.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shinemo.imdemo.BaseFragment;
import com.shinemo.imdemo.R;

/**
 * Created by yangqinlin on 17/2/27.
 */

public class SettingFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.tab_setting);
        return view;
    }
}
