package com.shinemo.imdemo;

/**
 * Created by yangqinlin on 17/2/27.
 */

public abstract class BasePresenter<T extends BaseView> {

    public T mView;

    public BasePresenter(T view) {
        mView = view;
    }

    public void detachView() {
        mView = null;
    }
}
