package com.elmoneyman.yummythreats.Utils;


import android.content.Context;
import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.R;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class  MessageNotifications implements MessageNotificationProvider {

    private final Context context;

    @Inject
    public MessageNotifications(@NonNull Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public String noConnectionMessage() {
        return context.getString( R.string.message_no_network);
    }

    @NonNull
    @Override
    public String errorMessage() {
        return context.getString(R.string.message_error_has_occurred);
    }

    @NonNull
    @Override
    public String emptyMessage() {
        return context.getString(R.string.message_empty_query);
    }
}
