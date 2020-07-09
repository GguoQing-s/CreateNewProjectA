package com.example.createnewprojecta.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.model.ISelectImageItem;
import com.example.createnewprojecta.model.Img;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yixuanxuan on 16/10/12.
 */

public class SelectorAdapter2 extends RecyclerView.Adapter<RecyclerViewHolder2> {

    private List<ISelectImageItem> mData;
    private OnItemClickListener mListener;

    public SelectorAdapter2(@NonNull OnItemClickListener listener) {
        mData = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public RecyclerViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.scrat.app.selectorlibrary.R.layout.item_img, parent, false);
        return new RecyclerViewHolder2(v);
    }

    private static final int[] COLORS = new int[]{
            com.scrat.app.selectorlibrary.R.color.image_selector_red,
            com.scrat.app.selectorlibrary.R.color.image_selector_orange,
            com.scrat.app.selectorlibrary.R.color.image_selector_yellow
    };

    private int getColor(int position) {
        int pos = position % COLORS.length;
        return COLORS[pos];
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder2 holder, int position) {
        final int pos = holder.getAdapterPosition();
        ISelectImageItem item = getItem(pos);
        if (item == null)
            return;

        final ImageView imageView = holder.getView(R.id.iv_img);


        Glide.with(imageView.getContext()).load(item.getImgPath()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);

        if (item.isChecked()) {
            zoomOut(holder.getRootView());
        } else {
            zoomIn(holder.getRootView());
        }
    }

    private void zoomIn(View v) {
        ViewCompat.animate(v).setDuration(300L).scaleX(1.0f).scaleY(1.0f).start();
    }

    private void zoomOut(View v) {
        ViewCompat.animate(v).setDuration(300L).scaleX(0.9f).scaleY(0.9f).start();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private ISelectImageItem getItem(int position) {
        if (position >= mData.size())
            return null;

        return mData.get(position);
    }

    public void replaceDatas(List<Img> data) {
        mData.clear();

        if (data != null && !data.isEmpty()) {
            mData.addAll(data);
        }

        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        int onItemClick(ISelectImageItem item, int pos);
    }


}
