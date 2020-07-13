package com.example.imageditlibrary.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.createnewprojecta.activity.Z_CameraCreate;

/**
 * Created by panyi on 2017/3/28.
 */

public abstract class BaseCameraFragment extends Fragment {
    protected Z_CameraCreate activity;

    protected Z_CameraCreate ensureEditActivity(){
        if(activity==null){
            activity = (Z_CameraCreate)getActivity();
        }
        return activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ensureEditActivity();
    }

    public abstract void onShow();

    public abstract void backToMain();
}//end class
