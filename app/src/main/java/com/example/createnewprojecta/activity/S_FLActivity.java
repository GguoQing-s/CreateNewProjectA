package com.example.createnewprojecta.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.adapter.S_FlAdapter;
import com.example.createnewprojecta.bean.Fl;
import com.example.createnewprojecta.dialog.Fx_Dialog;
import com.example.createnewprojecta.net.VolleyLo;
import com.example.createnewprojecta.net.VolleyTo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class S_FLActivity extends AppCompatActivity {
    @BindView(R.id.change)
    ImageView change;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout)
    LinearLayout layout;
    private List<Fl> mFl;
    private S_FlAdapter flAdapter;
    private int width, screenWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_flactivity);
        ButterKnife.bind(this);
        inview();
        initTitle();
        huoqu();
    }

    private void initTitle() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.height = screenWidth / 8;
        layoutParams.width = screenWidth;
        layout.setLayoutParams(layoutParams);
        title.setTextSize(22);
    }

    private void inview() {
        mFl = new ArrayList<>();
        width = getIntent().getIntExtra("width", 0);
        screenWidth = getIntent().getIntExtra("screenWidth", 0);
    }

    private void huoqu() {
        VolleyTo volleyTo = new VolleyTo();
        volleyTo.setUrl("get_shousi_photo")
                .setJsonObject("UserName", "user1")
                .setVolleyLo(new VolleyLo() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            Gson gson = new Gson();
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (!(jsonArray.getJSONObject(i).getInt("type") == 1)) {
                                    Fl fl = gson.fromJson(jsonArray.getString(i), Fl.class);
                                    mFl.add(fl);
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
        flAdapter = new S_FlAdapter(this, mFl, width);
        gridView.setAdapter(flAdapter);
        flAdapter.SetData(new S_FlAdapter.SetData() {
            @Override
            public void setdata(int position, String image) {
                Fx_Dialog fx_dialog = new Fx_Dialog(screenWidth, image);
                fx_dialog.show(getSupportFragmentManager(), "");
                fx_dialog.setCancelable(false);

            }
        });
    }

    @OnClick(R.id.change)
    public void onViewClicked() {
        finish();
    }
}
