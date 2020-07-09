package com.example.createnewprojecta.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.bean.Dt;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class S_ImageAdapter extends BaseAdapter {
    private List<Dt> mDt;
    private Context context;
    public int width;
    public interface SetData{
        void setdata(int position,String image);
    }
    public SetData data;
    public void SetData(SetData data)
    {
        this.data=data;
    }
    public S_ImageAdapter(Context context, List<Dt> mDt,int width) {
        this.context = context;
        this.mDt = mDt;
        this.width=width;
    }

    @Override
    public int getCount() {
        return mDt.size();
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
        final Dt dt  =mDt.get(position);
        View view = View.inflate(context, R.layout.zz_item1, null);
        ViewHolder viewHolder  =new ViewHolder(view);
        Glide.with(context).load(dt.getImagepath()).into(viewHolder.image);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width);
        viewHolder.image.setLayoutParams(param);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setdata(position,dt.getImagepath());
            }
        });
        return view;
    }

    static
    class ViewHolder {
        @BindView(R.id.image)
        ImageView image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
