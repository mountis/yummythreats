package com.elmoneyman.yummythreats.Utils;


import android.support.annotation.NonNull;

public interface MessageNotificationProvider {
    @NonNull String noConnectionMessage();
    @NonNull String emptyMessage();
    @NonNull String errorMessage();
}
