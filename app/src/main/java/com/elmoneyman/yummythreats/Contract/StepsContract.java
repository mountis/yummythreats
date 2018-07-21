package com.elmoneyman.yummythreats.Contract;


import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Presenter.BasePresenter;
import com.elmoneyman.yummythreats.View.BaseView;

public interface StepsContract {
    interface Presenter extends BasePresenter<View> {
        void attachView(@NonNull View view);
        void stop();
        void showNext();
        void showPrev();
        void showCurrent();
        void requestStep(int step);
    }

    interface View extends BaseView<Presenter> {
        void attachPresenter(@NonNull Presenter presenter);
        void showMessage(@NonNull String message);
        void playVideo(String videoUrl);
        void pauseVideo();
        void hideNextButton();
        void hidePrevButton();
        void showNextButton();
        void showPrevButton();
        void showPageNumber(int currentPage, int total);
        void hidePlayer();
        void showDescription(String shortDescription, String description);
    }
}
