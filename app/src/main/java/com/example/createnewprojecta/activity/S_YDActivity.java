package com.example.createnewprojecta.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.createnewprojecta.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class S_YDActivity extends AppCompatActivity implements View.OnTouchListener {

    // 两个用来叠加的图片
    ImageView mIv;
    ImageView mIv2;
    // 显示绘制后的图像
    ImageView mImageView;
    Canvas canvas;
    int lastX, lastY;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.change)
    ImageView change;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.conta)
    ImageView conta;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img)
    ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_ydactivity);
        ButterKnife.bind(this);


        mImageView = (ImageView) findViewById(R.id.conta);
        mImageView.setImageResource(R.drawable.zz1);
        mImageView.setDrawingCacheEnabled(true);// 启用缓存
        mIv2 = (ImageView) findViewById(R.id.img2);
        mIv2.setDrawingCacheEnabled(true);
        mIv2.setOnTouchListener(this);
        mIv = (ImageView) findViewById(R.id.img);
        mIv.setDrawingCacheEnabled(true);
        mIv.setOnTouchListener(this);
    }


    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;

                int left = v.getLeft() + dx;
                int top = v.getTop() + dy;
                int right = v.getRight() + dx;
                int bottom = v.getBottom() + dy;
                v.layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    @OnClick(R.id.change)
    public void onViewClicked() {
        // 保存叠加的图片
        Bitmap bitmap = mImageView.getDrawingCache();
        if (canvas == null) {
            canvas = new Canvas(bitmap);
        }
        // 根据两个机器人的位置绘制
        canvas.drawBitmap(mIv.getDrawingCache(), mIv.getLeft(),mIv.getTop(), null);
        canvas.drawBitmap(mIv2.getDrawingCache(), mIv2.getLeft(),mIv2.getTop(), null);

        // 显示在界面上
        mImageView.setImageBitmap(bitmap);

        // 保存至本地
        File f = new File(getCacheDir().getAbsolutePath() + "/"
                + SystemClock.currentThreadTimeMillis() + "img.jpg");

        try {
            FileOutputStream fos = new FileOutputStream(f);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();


            Uri uri = Uri.fromFile(f);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(this,"图片保存成功",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 将两个机器人设置不可见
        mIv.setVisibility(View.GONE);
        mIv2.setVisibility(View.GONE);
    }
}
