package com.example.createnewprojecta.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.createnewprojecta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class S_WdAdapter extends BaseAdapter {
    private String[] name;
    private Context context;

    public int width;

    public interface SetData {
        void setdata(String name);
    }

    public SetData data;

    public void SetData(SetData data) {
        this.data = data;
    }

    public S_WdAdapter(Context context, String[] name, int width) {
        this.context = context;
        this.name = name;
        this.width = width;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.wd_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.textView.setText(name[position]);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width/5, width/5);
        viewHolder.tu.setLayoutParams(param);
        viewHolder.beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setdata(name[position]);
            }
        });
       switch (position)
       {
           case 0:
               viewHolder.tu.setImageResource(R.drawable.pf);
               break;
           case 1:
               viewHolder.tu.setImageResource(R.drawable.yjfk);
               break;
           case 2:
               viewHolder.tu.setImageResource(R.drawable.sc);
               break;
           case 3:
               viewHolder.tu.setImageResource(R.drawable.yszc);
               break;
           case 4:
               viewHolder.tu.setImageResource(R.drawable.yhxy);
               break;
           case 5:
               viewHolder.tu.setImageResource(R.drawable.qx);
               break;
       }
        return view;
    }


    static
    class ViewHolder {
        @BindView(R.id.tu)
        ImageView tu;
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.beijing)
        LinearLayout beijing;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
