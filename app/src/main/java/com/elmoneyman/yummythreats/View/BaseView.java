package com.elmoneyman.yummythreats.View;

import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Presenter.BasePresenter;

public interface BaseView<Presenter extends BasePresenter<? extends BaseView>> {
    void attachPresenter(@NonNull Presenter presenter);
}