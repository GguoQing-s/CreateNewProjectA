package com.example.createnewprojecta.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.bean.Fl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class S_FlAdapter extends BaseAdapter {
    private List<Fl> mFl;
    private Context context;

    public int width;

    public interface SetData {
        void setdata(int position, String image);
    }

    public SetData data;

    public void SetData(SetData data) {
        this.data = data;
    }

    public S_FlAdapter(Context context, List<Fl> mFl, int width) {
        this.context = context;
        this.mFl = mFl;
        this.width = width;
    }

    @Override
    public int getCount() {
        return mFl.size();
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
        final Fl fl = mFl.get(position);
        View view = View.inflate(context, R.layout.fl_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        Glide.with(context).load(fl.getImagepath()).into(viewHolder.tu);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width);
        viewHolder.tu.setLayoutParams(param);
        viewHolder.tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setdata(position, fl.getImagepath());
            }
        });
        return view;
    }

    static
    class ViewHolder {
        @BindView(R.id.tu)
        ImageView tu;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
