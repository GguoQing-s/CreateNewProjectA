package com.example.createnewprojecta.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.createnewprojecta.MainActivity;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.fragment.S_Fragment_dt;
import com.example.createnewprojecta.fragment.S_Fragment_tj;
import com.example.createnewprojecta.fragment.S_Fragment_wd;
import com.example.createnewprojecta.fragment.S_Fragment_zz;
import com.example.createnewprojecta.fragment.Z_FragmentMain;
import com.example.createnewprojecta.fragment.Z_FragmentPhoto;
import com.example.createnewprojecta.util.ColorUtil;
import com.example.createnewprojecta.util.ShareToolUtil;
import com.example.createnewprojecta.util3.PermissionsCall;
import com.example.createnewprojecta.util3.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class S_ZZActivity1 extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.change)
    ImageView change;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.canvas_layout)
    FrameLayout canvasLayout;
    @BindView(R.id.im1)
    ImageView im1;
    @BindView(R.id.im2)
    ImageView im2;
    @BindView(R.id.im3)
    ImageView im3;
    @BindView(R.id.im4)
    ImageView im4;
    @BindView(R.id.change1)
    ImageView change1;
    private FragmentTransaction ft;
    public int width, screenWidth;//屏幕宽度
    private final static int requestCode = 111;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_zzactivity1);
        ButterKnife.bind(this);
        applyPermissions();
        ColorUtil.initColor(this);
        initData();
        initIamge();
        initTitle();
        addFragment();
    }


    public void applyPermissions() {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        list.add(Manifest.permission.CAMERA);
        list.add(Manifest.permission.CALL_PHONE);
        list.add(Manifest.permission.READ_PHONE_STATE);
        list.add(Manifest.permission.RECORD_AUDIO);
        PermissionsUtils.with(S_ZZActivity1.this)
                .permissions(list)
                .request(requestCode, new PermissionsCall() {
                    @Override
                    public void errorRequest(String errorMsg) {

                    }

                    @Override
                    public void granted() {

                    }

                    @Override
                    public void denideList(List<String> list) {

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void addFragment() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.canvas_layout, new Z_FragmentPhoto());
        ft.commit();
    }


    private void initTitle() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.height = screenWidth / 8;
        layoutParams.width = screenWidth;
        layout.setLayoutParams(layoutParams);
        title.setTextSize(22);
        change.setLayoutParams(new LinearLayout.LayoutParams(layout.getLayoutParams().height - 25,
                layout.getLayoutParams().height - 25));
        change1.setVisibility(View.GONE);
    }

    private void initIamge() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) im1.getLayoutParams();
        layoutParams.width = screenWidth / 5;
        layoutParams.height = screenWidth / 7;
        im1.setLayoutParams(layoutParams);
        im2.setLayoutParams(layoutParams);
        im3.setLayoutParams(layoutParams);
        im4.setLayoutParams(layoutParams);

    }

    private void initData() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        screenWidth = outSize.x;
        //动态设置GridView图片的宽高,间距是10，每行两列，计算每个图片的宽度，高度与宽度一致
        width = (screenWidth - (3 * Dp2Px(this, 10))) / 3;


    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @OnClick({R.id.im1, R.id.im2, R.id.im3, R.id.im4})
    public void onViewClicked(View view) {
        ft = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.im1:
                im1.setImageResource(R.drawable.tj2);
                im2.setImageResource(R.drawable.zz1);
                im3.setImageResource(R.drawable.dt1);
                im4.setImageResource(R.drawable.wd1);
               // ft.replace(R.id.canvas_layout, new S_Fragment_tj(width,screenWidth));
                ft.replace(R.id.canvas_layout,new Z_FragmentPhoto());
                title.setText("推荐");
                break;
            case R.id.im2:
                im1.setImageResource(R.drawable.tj1);
                im2.setImageResource(R.drawable.zz2);
                im3.setImageResource(R.drawable.dt1);
                im4.setImageResource(R.drawable.wd1);
                ft.replace(R.id.canvas_layout, new S_Fragment_zz(width, screenWidth));
                title.setText("制作");
                break;
            case R.id.im3:
                im1.setImageResource(R.drawable.tj1);
                im2.setImageResource(R.drawable.zz1);
                im3.setImageResource(R.drawable.dt2);
                im4.setImageResource(R.drawable.wd1);
                ft.replace(R.id.canvas_layout, new S_Fragment_dt(screenWidth));
                title.setText("GIF表情包制作");
                break;
            case R.id.im4:
                im1.setImageResource(R.drawable.tj1);
                im2.setImageResource(R.drawable.zz1);
                im3.setImageResource(R.drawable.dt1);
                im4.setImageResource(R.drawable.wd2);
                ft.replace(R.id.canvas_layout, new S_Fragment_wd(width,screenWidth));
                title.setText("我的");
                break;
        }
        ft.commit();
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast  toast = new Toast(S_ZZActivity1.this);
                View view  = LayoutInflater.from(S_ZZActivity1.this).inflate(R.layout.toast_dialog,null);
                toast.setView(view);
                toast.setGravity(Gravity.TOP,0,20);
                toast.show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
