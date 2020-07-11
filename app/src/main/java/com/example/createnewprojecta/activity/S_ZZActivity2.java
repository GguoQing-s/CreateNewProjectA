package com.example.createnewprojecta.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.createnewprojecta.ImageScale;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.bean.Zz;
import com.example.createnewprojecta.net.VolleyLo;
import com.example.createnewprojecta.net.VolleyTo;
import com.example.createnewprojecta.util.ImgUtils;
import com.example.createnewprojecta.util2.ImageListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.audiofx.AudioEffect.ERROR;

public class S_ZZActivity2 extends AppCompatActivity {
    @BindView(R.id.canvas_layout)
    RelativeLayout canvasLayout;
    @BindView(R.id.image_layout)
    LinearLayout imageLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.change)
    ImageView change;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.change1)
    ImageView change1;
    private List<Integer> index;
    private String type, path;
    private List<Zz> mZz;
    private int A = 1, B = 2, screenWidth;
    private Canvas canvas;
    private List<Bitmap> mBitmap;
    private List<View> views;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            if (msg.what == A) {
                mergeBitmap(bitmap);
            } else if (msg.what == B) {
                mBitmap.add(bitmap);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_zzactivity2);
        ButterKnife.bind(this);
        views = new ArrayList<>();
        init();
    }

    private void setjieko(String name) {
        switch (name) {
            case "寿司":
                huoqu("get_shousi_photo");
                break;
            case "吉祥物":
                huoqu("get_jixiangwu_photo");
                break;
            case "包子":
                huoqu("get_baozi_photo");
                break;
            case "斗牛家":
                huoqu("get_douniujia_photo");
                break;
            case "粒子":
                huoqu("get_lizi_photo");
                break;
            case "女警察":
                huoqu("get_nvjingcha_photo");
                break;
            case "消防员":
                huoqu("get_xiaofangyuan_photo");
                break;
            case "护士":
                huoqu("get_hs_photo");
                break;
            case "西瓜":
                huoqu("get_xigua_photo");
                break;
            case "渔夫":
                huoqu("get_yufu_photo");
                break;
            case "放心农场":
                huoqu("get_fangxinnongchang_photo");
                break;
            case "小猪":
                huoqu("get_xiaozhu_photo");
                break;
        }
    }

    private void huoqu(String url) {
        VolleyTo volleyTo = new VolleyTo();
        volleyTo.setUrl(url)
                .setJsonObject("UserName", "user1")
                .setVolleyLo(new VolleyLo() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            mZz.clear();
                            Gson gson = new Gson();
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (!(jsonArray.getJSONObject(i).getInt("type") == 1)) {
                                    Zz zz = gson.fromJson(jsonArray.getString(i), Zz.class);
                                    mZz.add(zz);
                                }

                            }
                            addData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }).start();
    }

    private void init() {
        index = new ArrayList<>();
        mBitmap = new ArrayList<>();
        mZz = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        path = getIntent().getStringExtra("path");
        screenWidth = getIntent().getIntExtra("width", 0);
        change.setImageResource(R.drawable.baocun);
        title.setText("制作");
        initTitle();
        setjieko(type);
        View view = View.inflate(S_ZZActivity2.this, R.layout.im_item, null);
        ImageView imageView = view.findViewById(R.id.canvas_image);
        imageView.setOnTouchListener(new ImageListener(imageView));
        ImageView delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasLayout.removeViewAt(0);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < canvasLayout.getChildCount(); i++) {
                    canvasLayout.getChildAt(i)
                            .findViewById(R.id.canvas_image).setBackgroundResource(R.drawable.bk1);
                }
                canvasLayout.getChildAt(0)
                        .findViewById(R.id.canvas_image).setBackgroundResource(R.drawable.bk);
            }
        });
        Glide.with(S_ZZActivity2.this).load(path).into(imageView);
        view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, screenWidth / 2));
        canvasLayout.addView(view);
    }

    private void initTitle() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.height = screenWidth / 8;
        layoutParams.width = screenWidth;
        layout.setLayoutParams(layoutParams);
        title.setTextSize(22);
        change.setLayoutParams(new LinearLayout.LayoutParams(layout.getLayoutParams().height - 25,
                layout.getLayoutParams().height - 25));
        change1.setLayoutParams(new LinearLayout.LayoutParams(layout.getLayoutParams().height - 25,
                layout.getLayoutParams().height - 25));
    }

    private void setData(int y) {
        final View view = View.inflate(S_ZZActivity2.this, R.layout.im_item, null);

        final ImageView imageView = view.findViewById(R.id.canvas_image);
        TextView textView = view.findViewById(R.id.number);
        ImageView delete = view.findViewById(R.id.delete);

        for (int i = 0; i < canvasLayout.getChildCount(); i++) {
            canvasLayout.getChildAt(i)
                    .findViewById(R.id.canvas_image).setBackgroundResource(R.drawable.bk1);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = view.findViewById(R.id.number);
                for (int i = index.size(); i > 0; i--) {
                    if (index.get(i - 1).toString().equals(tv.getText().toString())) {
                        index.remove(i - 1);
                    }
                }

                for (int i = 0; i < canvasLayout.getChildCount(); i++) {
                    View view1 = canvasLayout.getChildAt(i);

                    TextView tv2 = view1.findViewById(R.id.number);
                    if (tv2.getText().toString().equals(tv.getText().toString())) {
                        canvasLayout.removeViewAt(i);
                        if (canvasLayout.getChildCount() < 1) {
                            canvasLayout.removeAllViews();
                        } else {
                            canvasLayout.getChildAt(canvasLayout.getChildCount() - 1).findViewById(R.id.canvas_image).setBackgroundResource(R.drawable.bk);
                        }
                    }
                }

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = view.findViewById(R.id.number);
                for (int i = 0; i < canvasLayout.getChildCount(); i++) {
                    View view1 = canvasLayout.getChildAt(i);
                    TextView tv2 = view1.findViewById(R.id.number);
                    if (tv2.getText().toString().equals(tv.getText().toString())) {
                        canvasLayout.getChildAt(i)
                                .findViewById(R.id.canvas_image).setBackgroundResource(R.drawable.bk);
//                        imageView.setOnTouchListener(new ImageScale(imageView));
                    } else {
                        canvasLayout.getChildAt(i)
                                .findViewById(R.id.canvas_image).setBackgroundResource(R.drawable.bk1);
                    }
                }
            }
        });
        Glide.with(S_ZZActivity2.this).load(mZz.get(y).getImagepath()).into(imageView);
        textView.setText(y + "");
        view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, screenWidth / 2));
        canvasLayout.addView(view);
    }


    private void addData() {
        for (int i = 0; i < mZz.size(); i++) {
            View view = View.inflate(S_ZZActivity2.this, R.layout.zz_item2, null);
            ImageView imageView = view.findViewById(R.id.image);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index.add(finalI);
                    setData(finalI);
                }
            });
            Glide.with(S_ZZActivity2.this).load(mZz.get(i).getImagepath()).into(imageView);
            view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, screenWidth / 2));
            imageLayout.addView(view);
        }
    }


    private Bitmap mergeBitmap(Bitmap firstBitmap) {
        final Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),
                firstBitmap.getConfig());
        canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < index.size(); i++) {
                        URL url = new URL(mZz.get(index.get(i)).getImagepath());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        int code = conn.getResponseCode();
                        if (code == 200) {
                            InputStream is = conn.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            Message msg = new Message();
                            msg.what = B;
                            msg.obj = bitmap;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        }
                    }

                    for (int y = 0; y < mBitmap.size(); y++) {
                        canvas.drawBitmap(mBitmap.get(y), 0, 0, null);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImgUtils.saveCanvas(S_ZZActivity2.this, bitmap);//
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = ERROR;
                    handler.sendMessage(msg);
                }
            }
        }.start();
        return bitmap;
    }

    @OnClick({R.id.change1, R.id.change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change1:
                AlertDialog.Builder builder = new AlertDialog.Builder(S_ZZActivity2.this);
                builder.setTitle("提示");
                builder.setCancelable(false);
                builder.setMessage("是否返回首页！");
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
                break;
            case R.id.change:
                new Thread() {
                    public void run() {
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                Message msg = new Message();
                                msg.what = A;
                                msg.obj = bitmap;
                                handler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = ERROR;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        }
                    }
                }.start();
//                Log.i("111", "onViewClicked: "+canvasLayout.getChildCount());
//                ImageView imageView = canvasLayout.getChildAt(0).findViewById(R.id.canvas_image);
//                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//                canvas = new Canvas(bitmap);
//                if (views.size()>1) {
//                    for (int i = 1; i < views.size(); i++) {
//                        canvas.drawBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap(), new Matrix(), null);
//                    }
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImgUtils.saveCanvas(S_ZZActivity2.this, bitmap);//
//                    }
//                });
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(S_ZZActivity2.this);
        builder.setTitle("提示");
        builder.setCancelable(false);
        builder.setMessage("是否返回首页！");
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
        return super.onKeyDown(keyCode, event);
    }
}
