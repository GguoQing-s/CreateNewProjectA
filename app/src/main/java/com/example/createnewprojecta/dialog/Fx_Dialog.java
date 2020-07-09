package com.example.createnewprojecta.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.activity.S_ZZActivity2;
import com.example.createnewprojecta.util.ImgUtils;
import com.example.createnewprojecta.util.NativeShareTool;
import com.example.createnewprojecta.util2.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.media.audiofx.AudioEffect.ERROR;

@SuppressLint("ValidFragment")
public class Fx_Dialog extends DialogFragment {
    @BindView(R.id.cha)
    ImageView cha;
    @BindView(R.id.tu)
    ImageView tu;
    @BindView(R.id.pyq)
    ImageView pyq;
    @BindView(R.id.qq)
    ImageView qq;
    @BindView(R.id.kj)
    ImageView kj;
    @BindView(R.id.wb)
    ImageView wb;
    @BindView(R.id.bc)
    ImageView bc;
    Unbinder unbinder;
    @BindView(R.id.wx)
    ImageView wx;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.v)
    View v;
    private NativeShareTool nativeShareTool;
    private int screenWidth;
    private String path;
    private int A=1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            File file = (File) msg.obj;
            if (msg.what == A) {
                try {
                    Uri uri = Uri.fromFile(file);
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    Toast.makeText(getContext(), "图片保存成功", Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    Toast.makeText(getContext(), "图片保存失败", Toast.LENGTH_LONG).show();
                }finally {
                    getDialog().dismiss();
                }
            }else if (msg.what==2){
                nativeShareTool.shareWechatFriend(file,true);
            }else if (msg.what==3){
                nativeShareTool.shareWechatMoment(file);
            }else if(msg.what==4){
                nativeShareTool.shareImageToQQ(file);
            }else if (msg.what==5){
                nativeShareTool.shareImageToQQZone(file.getPath());
            }else if(msg.what==6){
                nativeShareTool.shareToSinaFriends(getContext(),true,file.getPath());
            }
        }
    };
    public Fx_Dialog(int screenWidth,String path) {
        this.screenWidth = screenWidth;
        this.path=path;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        View view = inflater.inflate(R.layout.fx_dialog, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inview();
        addAnimator();
    }

    private void initaddAnimator(ImageView imageView, int duration,int wz) {
        ObjectAnimator transXAnim = ObjectAnimator.ofFloat(imageView, "translationX", wz, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(transXAnim);
        set.setDuration(duration);
        set.start();
    }

    private void addAnimator() {
        try {
            initaddAnimator(cha, 600,-300);
            initaddAnimator(tu, 600,-300);
            initaddAnimator(wx, 600,-300);
            initaddAnimator(wx, 600,-300);
            Animation translateAnimation = new TranslateAnimation(-300, 0, 0, 0);//设置平移的起点和终点
            translateAnimation.setDuration(600);
            textView.setAnimation(translateAnimation);
            v.setAnimation(translateAnimation);
            initaddAnimator(pyq, 600,-300);
            initaddAnimator(qq, 600,300);
            initaddAnimator(kj, 600,-300);
            initaddAnimator(wb, 600,-300);
            initaddAnimator(bc, 600,300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inview() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) wx.getLayoutParams();
        layoutParams.width = screenWidth / 5 - 20;
        layoutParams.height = screenWidth / 5 - 20;
        wx.setLayoutParams(layoutParams);
        pyq.setLayoutParams(layoutParams);
        qq.setLayoutParams(layoutParams);
        kj.setLayoutParams(layoutParams);
        wb.setLayoutParams(layoutParams);
        bc.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) tu.getLayoutParams();
        layoutParams2.width = screenWidth / 2;
        layoutParams2.height = screenWidth / 2;
        tu.setLayoutParams(layoutParams2);
        Glide.with(getContext()).load(path).into(tu);

        nativeShareTool = NativeShareTool.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(screenWidth - 100, screenWidth + 300);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.cha, R.id.wx, R.id.pyq, R.id.qq, R.id.kj, R.id.wb, R.id.bc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cha:
                getDialog().dismiss();
                break;
            case R.id.wx:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(getContext().getExternalCacheDir(), "out111put.gif");
                        FileOutputStream out = null;
                        if (file.exists()) {
                            file.delete();
                        }
                        try {
                            file.createNewFile();
                            out = new FileOutputStream(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                byte[] buffer = new byte[1024];
                                int len = -1;
                                while( (len = is.read(buffer)) != -1 ){
                                    out.write(buffer, 0, len);
                                }
                                is.close();
                                out.close();// 保存数据
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = file;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.pyq:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(getContext().getExternalCacheDir(), "out111put.gif");
                        FileOutputStream out = null;
                        if (file.exists()) {
                            file.delete();
                        }
                        try {
                            file.createNewFile();
                            out = new FileOutputStream(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                byte[] buffer = new byte[1024];
                                int len = -1;
                                while( (len = is.read(buffer)) != -1 ){
                                    out.write(buffer, 0, len);
                                }
                                is.close();
                                out.close();// 保存数据
                                Message msg = new Message();
                                msg.what = 3;
                                msg.obj = file;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.qq:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(getContext().getExternalCacheDir(), "out111put.gif");
                        FileOutputStream out = null;
                        if (file.exists()) {
                            file.delete();
                        }
                        try {
                            file.createNewFile();
                            out = new FileOutputStream(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                byte[] buffer = new byte[1024];
                                int len = -1;
                                while( (len = is.read(buffer)) != -1 ){
                                    out.write(buffer, 0, len);
                                }
                                is.close();
                                out.close();// 保存数据
                                Message msg = new Message();
                                msg.what = 4;
                                msg.obj = file;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.kj:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(getContext().getExternalCacheDir(), "out111put.gif");
                        FileOutputStream out = null;
                        if (file.exists()) {
                            file.delete();
                        }
                        try {
                            file.createNewFile();
                            out = new FileOutputStream(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                byte[] buffer = new byte[1024];
                                int len = -1;
                                while( (len = is.read(buffer)) != -1 ){
                                    out.write(buffer, 0, len);
                                }
                                is.close();
                                out.close();// 保存数据
                                Message msg = new Message();
                                msg.what = 5;
                                msg.obj = file;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.wb:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(getContext().getExternalCacheDir(), "out111put.gif");
                        FileOutputStream out = null;
                        if (file.exists()) {
                            file.delete();
                        }
                        try {
                            file.createNewFile();
                            out = new FileOutputStream(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                byte[] buffer = new byte[1024];
                                int len = -1;
                                while( (len = is.read(buffer)) != -1 ){
                                    out.write(buffer, 0, len);
                                }
                                is.close();
                                out.close();// 保存数据
                                Message msg = new Message();
                                msg.what = 6;
                                msg.obj = file;
                                handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.bc:
                new Thread() {
                    public void run() {
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy", System.currentTimeMillis() + ".gif");
                        FileOutputStream out = null;
                        if (file.exists()) {
                            file.delete();
                        }
                        try {
                            file.createNewFile();
                            out = new FileOutputStream(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code = conn.getResponseCode();
                            if (code == 200) {
                                InputStream is = conn.getInputStream();
                                byte[] buffer = new byte[1024];
                                int len = -1;
                                while( (len = is.read(buffer)) != -1 ){
                                    out.write(buffer, 0, len);
                                }
                                is.close();
                                out.close();// 保存数据
                                Message msg = new Message();
                                msg.what = A;
                                msg.obj = file;
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
                break;
        }
    }

    private Bitmap getPicBit() {
        AssetManager assets = this.getResources().getAssets();
        InputStream open = null;
        File mFile = null;
        Bitmap mBitmap = null;
        try {
            open = assets.open("share_pic.jpg");

            BitmapFactory.Options bo  = new BitmapFactory.Options();
            bo.inScaled = false;
            bo.inPreferredConfig = Bitmap.Config.RGB_565;
            bo.inJustDecodeBounds = false;
            bo.inDither = true;
            mBitmap = BitmapFactory.decodeStream(open, null, bo);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (open != null){
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mBitmap;
    }
}
