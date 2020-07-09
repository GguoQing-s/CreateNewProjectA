package com.example.createnewprojecta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.createnewprojecta.util.ColorUtil;
import com.example.createnewprojecta.util.GifUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity2 extends AppCompatActivity {
    public static final int WRITE_STORAGE = 1;
    public static final int READ_STORAGE = 2;
    @BindView(R.id.change)
    ImageView change;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.lujing)
    TextView lujing;
    @BindView(R.id.fanhui)
    TextView fanhui;
    @BindView(R.id.tishi)
    TextView tishi;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    private AlertDialog alertDialog;
    private List<String> paths;
    private int screenWidth;
    private File file;
    private String path;
    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private ProgressDialog dialog;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                alertDialog.dismiss();
                Toast.makeText(MainActivity2.this, "制作成功已保存到相册", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.fromFile(file);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                lujing.setText(path);
                tishi.setText("GIF保存路径");
                title1.setVisibility(View.GONE);
                title.setText("保存成功");
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
            } else {
                dialog.dismiss();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        title.setText("制作");
        screenWidth = getIntent().getIntExtra("width", 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.height = screenWidth / 8;
        layoutParams.width = screenWidth;
        layout.setLayoutParams(layoutParams);
        title.setTextSize(22);
        title1.setTextSize(15);
        change.setLayoutParams(new LinearLayout.LayoutParams(layout.getLayoutParams().height - 25,
                layout.getLayoutParams().height - 25));

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) lujing.getLayoutParams();
        layoutParams2.width = screenWidth - 50;
        lujing.setLayoutParams(layoutParams2);


        ColorUtil.initColor(this);
        alertDialog = new AlertDialog.Builder(this).setView(new ProgressBar(this))
                .setMessage("正在生成gif图片").create();
        ImageSelector.show(this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);

        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("加载中。。。");

        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMG) {
            showContent(data);
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showContent(Intent data) {
        paths = ImageSelector.getImagePaths(data);
        if (paths.isEmpty()) {
            Toast.makeText(MainActivity2.this, "没有选择图片！", Toast.LENGTH_LONG).show();
            finish();
        }
        setVolley();
    }

    List<Bitmap> bitmaps = new ArrayList<>();

    private void setVolley() {
        for (int i = 0; i < paths.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            bitmaps.add(bitmap);
        }
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GifUtil gifUtil = new GifUtil();
                path = gifUtil.createGif(bitmaps, System.currentTimeMillis() + "", 500, MainActivity2.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        handler.sendEmptyMessageDelayed(1, 2000);
                        file = new File(path);
                        //Glide.with(MainActivity2.this).clear(image);

                        Glide.with(MainActivity2.this).load(file).skipMemoryCache(true).into(image);
                    }
                });
            }
        }).start();
    }

    @OnClick({R.id.change, R.id.title1, R.id.fanhui})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change:
                String l = lujing.getText().toString();
                if (l.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                    builder.setTitle("提示");
                    builder.setCancelable(false);
                    builder.setMessage("退出将不会保存作品");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    finish();
                }
                break;
            case R.id.title1:
                alertDialog.show();
                handler.sendEmptyMessageDelayed(0, 2000);

                break;
            case R.id.fanhui:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String l = lujing.getText().toString();
        if (l.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
            builder.setTitle("提示");
            builder.setCancelable(false);
            builder.setMessage("退出将不会保存作品");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
