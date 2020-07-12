package com.example.createnewprojecta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.createnewprojecta.R;
import com.example.createnewprojecta.activity.Z_TackPhotoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @LogIn Name zhangyingyu
 * @Create by 张瀛煜 on 2020-07-12 at 18:39 ：）
 */
public class Z_FragmentPhoto extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.choose_image)
    ImageView chooseImage;
    @BindView(R.id.choose_text)
    TextView chooseText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tack_photo_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.choose_image, R.id.choose_text})
    public void onViewClicked(View view) {
        startActivity(new Intent(getActivity(), Z_TackPhotoActivity.class));
    }
}
