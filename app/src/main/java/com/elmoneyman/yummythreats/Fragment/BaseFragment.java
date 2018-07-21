package com.elmoneyman.yummythreats.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.elmoneyman.yummythreats.R;

import butterknife.BindBool;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    protected Unbinder doNotBind;

    @BindBool(R.bool.is_tablet)
    protected boolean isTablet;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initializeDependencies();
    }

    void bind(View root){
        if(root!=null){
            doNotBind = ButterKnife.bind(this,root);
        }
    }

    void bind(Activity activity){
        if(activity!=null){
            doNotBind = ButterKnife.bind(this,activity);
        }
    }

    abstract void initializeDependencies();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(doNotBind !=null) doNotBind.unbind();
    }
}
