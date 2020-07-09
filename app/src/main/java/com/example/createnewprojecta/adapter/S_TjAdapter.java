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
import com.example.createnewprojecta.bean.Tj;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class S_TjAdapter extends BaseAdapter {
    private List<Tj> mTj;
    private Context context;

    public int width;
    public interface SetData{
        void setdata(int position,String image);
    }
    public S_TjAdapter.SetData data;
    public void SetData(SetData data)
    {
        this.data=data;
    }
    public S_TjAdapter(Context context, List<Tj> mTj, int width) {
        this.context = context;
        this.mTj = mTj;
        this.width=width;
    }

    @Override
    public int getCount() {
        return mTj.size();
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
        final Tj tj = mTj.get(position);
        View view = View.inflate(context, R.layout.tj_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        Glide.with(context).load(tj.getImagepath()).into(viewHolder.tu);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width);
        viewHolder.tu.setLayoutParams(param);
        viewHolder.tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setdata(position, tj.getImagepath());
            }
        });
        return view;
    }


    static
    class ViewHolder {
        @BindView(R.id.tu)
        ImageView tu;
        @BindView(R.id.name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
