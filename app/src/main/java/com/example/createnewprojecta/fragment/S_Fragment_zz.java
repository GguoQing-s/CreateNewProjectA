package com.example.createnewprojecta.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.activity.S_ZZActivity2;
import com.example.createnewprojecta.activity.Z_BJActivity;
import com.example.createnewprojecta.adapter.S_ImageAdapter;
import com.example.createnewprojecta.bean.Dt;
import com.example.createnewprojecta.net.VolleyLo;
import com.example.createnewprojecta.net.VolleyTo;
import com.example.createnewprojecta.util.FileUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.media.audiofx.AudioEffect.ERROR;

@SuppressLint("ValidFragment")
public class S_Fragment_zz extends Fragment {
    @BindView(R.id.gridView)
    GridView gridView;
    Unbinder unbinder;
    @BindView(R.id.title_layout)
    LinearLayout titleLayout;
    private S_ImageAdapter imageAdapter;
    public int width, screenWidth;
    private List<Dt> mDt;
    private String[] mname = {"寿司", "吉祥物", "包子", "斗牛家", "粒子"
            , "西瓜", "渔夫", "放心农场", "小猪"};
    private String type = "寿司";

    public static final int ACTION_REQUEST_EDITIMAGE = 9;

    public S_Fragment_zz(int width, int screenWidth) {
        this.width = width;
        this.screenWidth = screenWidth;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                File file = (File) msg.obj;
                File outputFile = FileUtils.genEditFile();
                Z_BJActivity.start(getActivity(), file.getPath(), Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy", ACTION_REQUEST_EDITIMAGE, type);

            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_fragment_zz, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        addTitle();
        huoqu("get_shousi_photo");
    }

    private void addTitle() {
        titleLayout.removeAllViews();
        for (int i = 0; i < mname.length; i++) {
            final View view = View.inflate(getContext(), R.layout.title_item, null);
            TextView textView = view.findViewById(R.id.name);
            if (mname[0].equals(mname[i])) {
                textView.setBackgroundResource(R.drawable.tbk1);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = view.findViewById(R.id.name);
                    type = tv.getText().toString();
                    setjieko(type);
                    for (int i = 0; i < titleLayout.getChildCount(); i++) {
                        View view1 = titleLayout.getChildAt(i);
                        TextView tv2 = view1.findViewById(R.id.name);
                        if (tv2.getText().toString().equals(type)) {
                            tv2.setBackgroundResource(R.drawable.tbk1);
                        } else {
                            tv2.setBackgroundResource(R.drawable.tbk2);
                        }
                    }
                }
            });

            textView.setText(mname[i]);
            textView.setTextSize(20);
            view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT));
            titleLayout.addView(view);
        }
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
                            mDt.clear();
                            Gson gson = new Gson();
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (jsonArray.getJSONObject(i).getInt("type") == 1) {
                                    Dt dt = gson.fromJson(jsonArray.getString(i), Dt.class);
                                    mDt.add(dt);
                                }

                            }
                            setadapter();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }).start();
    }

    private void setadapter() {
        imageAdapter = new S_ImageAdapter(getContext(), mDt, width);
        gridView.setAdapter(imageAdapter);
        imageAdapter.SetData(new S_ImageAdapter.SetData() {
            @Override
            public void setdata(int position, String image) {
                final String path = image;
//                startActivity(new Intent(getContext(), S_ZZActivity2.class).putExtra("type", type).putExtra("path", image).putExtra("width", screenWidth));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(getContext().getExternalCacheDir(), "out111put.png");
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
                                while ((len = is.read(buffer)) != -1) {
                                    out.write(buffer, 0, len);
                                }
                                is.close();
                                out.close();// 保存数据

                                Message msg = new Message();
                                msg.what = 1;
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

            }
        });
    }

    private void init() {
        mDt = new ArrayList<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
