package com.example.createnewprojecta.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.createnewprojecta.R;
import com.example.createnewprojecta.activity.S_YJFKActivity;
import com.example.createnewprojecta.adapter.S_WdAdapter;
import com.example.createnewprojecta.util2.CacheUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class S_Fragment_wd extends Fragment {
    @BindView(R.id.listView)
    ListView listView;
    Unbinder unbinder;
    private String[] name;
    private S_WdAdapter wdAdapter;
    private int width,screenWidth;
    public S_Fragment_wd(int width,int screenWidth)
    {
        this.width=width;
        this.screenWidth=screenWidth;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_fragment_wd, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        name = new String[6];
        name[0]="给我们评分";
        name[1]="意见反馈";
        name[2]="清除缓存";
        name[3]="隐私政策";
        name[4]="用户协议";
        name[5]="系统隐私权限";
        setadapter();
    }

    private void setadapter() {
        wdAdapter = new S_WdAdapter(getContext(),name,width);
        listView.setAdapter(wdAdapter);
        wdAdapter.SetData(new S_WdAdapter.SetData() {
            @Override
            public void setdata(String name) {
                switch (name)
                {
                    case "意见反馈":
                        startActivity(new Intent(getContext(), S_YJFKActivity.class).putExtra("screenWidth",screenWidth));
                        break;
                    case "清除缓存":
                        try {
                            CacheUtil cacheUtil = new CacheUtil();
                            cacheUtil.getTotalCacheSize(getContext());
                            cacheUtil.clearAllCache(getContext());
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case "系统隐私权限":
                        startSetting(getContext());
                        break;
                }
            }
        });
    }
    /**
     * 启动app设置应用程序信息界面
     */
    public static void startSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
