package com.example.imageditlibrary.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.bean.Zz;
import com.example.imageditlibrary.fragment.StickerFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.IOException;
import java.util.List;


/**
 * 贴图分类列表Adapter
 *
 * @author panyi
 */
public class StickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx)
            .build();// 下载图片显示

    private StickerFragment mStickerFragment;
    private ImageClick mImageClick = new ImageClick();

    private List<Zz> images;
    public StickerAdapter(StickerFragment fragment,List<Zz> images) {
        super();
        this.images = images;
        this.mStickerFragment = fragment;
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.img);
        }
    }// end inner class

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_sticker_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageHolder imageHoler = (ImageHolder) holder;
        final String path = images.get(position).getImagepath();
        Log.i("aaa", "onBindViewHolder: "+path);
        imageHoler.image.setImageResource(R.drawable.app);
        Glide.with(mStickerFragment.getContext()).load(path).into(imageHoler.image);
        imageHoler.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStickerFragment.selectedStickerItem(path);
            }
        });
    }

    public void addStickerImages(String folderPath) {

        try {
            String[] files = mStickerFragment.getActivity().getAssets()
                    .list(folderPath);
            for (String name : files) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }

    /**
     * 选择贴图
     *
     * @author panyi
     */
    private final class ImageClick implements OnClickListener {
        @Override
        public void onClick(View v) {
        }
    }// end inner class

}// end class
