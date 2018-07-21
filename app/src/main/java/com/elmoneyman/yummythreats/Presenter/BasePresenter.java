package com.elmoneyman.yummythreats.Presenter;


import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.View.BaseView;

public interface BasePresenter<View extends BaseView> {
    void attachView(@NonNull View view);
}