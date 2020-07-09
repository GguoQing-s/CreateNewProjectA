package com.example.createnewprojecta.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.createnewprojecta.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class S_YJFKActivity extends AppCompatActivity {
    @BindView(R.id.change)
    ImageView change;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.tishi)
    TextView tishi;
    @BindView(R.id.jianyi)
    EditText jianyi;
    @BindView(R.id.tishi2)
    TextView tishi2;
    @BindView(R.id.lxfs)
    EditText lxfs;
    @BindView(R.id.tijiao)
    Button tijiao;
    @BindView(R.id.email)
    ImageView email;
    @BindView(R.id.tel)
    ImageView tel;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.v2)
    View v2;
    @BindView(R.id.line)
    LinearLayout line;
    private int screenWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_yjfkactivity);
        ButterKnife.bind(this);
        inview();
        initTitle();
    }

    private void initTitle() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.height = screenWidth / 8;
        layoutParams.width = screenWidth;
        layout.setLayoutParams(layoutParams);
        title.setTextSize(22);


        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) change.getLayoutParams();
        layoutParams1.height = screenWidth / 11;
        layoutParams1.width = screenWidth / 11;
        change.setLayoutParams(layoutParams1);

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) tijiao.getLayoutParams();
        layoutParams2.width = screenWidth - 250;
        tijiao.setLayoutParams(layoutParams2);

        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) tel.getLayoutParams();
        layoutParams3.height = screenWidth / 6;
        layoutParams3.width = screenWidth / 6;
        tel.setLayoutParams(layoutParams3);
        email.setLayoutParams(layoutParams3);

        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) v1.getLayoutParams();
        layoutParams4.width = screenWidth / 3;
        v1.setLayoutParams(layoutParams4);
        v2.setLayoutParams(layoutParams4);
    }

    private void inview() {
        title.setText("意见反馈");
        title.setTextSize(22);
        screenWidth = getIntent().getIntExtra("screenWidth", 0);
        change.setImageResource(R.drawable.cha1);
    }


    @OnClick({R.id.email, R.id.tel, R.id.change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.email:
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:way.ping.li@gmail.com"));
                data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
                data.putExtra(Intent.EXTRA_TEXT, "这是内容");
                startActivity(data);
                break;
            case R.id.tel:
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:123456"));
                startActivity(dialIntent);
                break;
            case R.id.change:
                finish();
                break;
        }
    }
}
