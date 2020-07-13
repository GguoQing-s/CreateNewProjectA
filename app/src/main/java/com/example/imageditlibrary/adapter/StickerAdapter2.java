package com.example.imageditlibrary.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.bean.Zz;
import com.example.createnewprojecta.net.VolleyLo;
import com.example.createnewprojecta.net.VolleyTo;
import com.example.imageditlibrary.fragment.StickerFragment;
import com.example.imageditlibrary.fragment.StickerFragment2;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 贴图分类列表Adapter
 *
 * @author panyi
 */
public class StickerAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx)
            .build();// 下载图片显示

    private StickerFragment2 mStickerFragment;
    private ImageClick mImageClick = new ImageClick();

    private List<Zz> images = new ArrayList<>();
    public StickerAdapter2(StickerFragment2 fragment) {
        super();
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
      //  imageHoler.image.setImageResource(R.drawable.app);
        Glide.with(mStickerFragment.getContext()).load(path).into(imageHoler.image);
        imageHoler.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStickerFragment.selectedStickerItem(path);
            }
        });
    }

    public void addStickerImages(final String folderPath) {
        if (images == null) {
            images = new ArrayList<>();
        } else {
            images.clear();
        }
        if (folderPath.equals("")){
            VolleyTo volleyTo = new VolleyTo();
            volleyTo.setUrl("get_wenzi_photo")
                    .setJsonObject("UserName", "user1")
                    .setVolleyLo(new VolleyLo() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Gson gson = new Gson();
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Zz zz = gson.fromJson(jsonArray.getString(i), Zz.class);
                                    images.add(zz);
                                }
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }).start();
        }else {
            VolleyTo volleyTo = new VolleyTo();
            volleyTo.setUrl("get_alltype_photo")
                    .setJsonObject("UserName", "user1")
                    .setVolleyLo(new VolleyLo() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Gson gson = new Gson();
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Zz zz = gson.fromJson(jsonArray.getString(i), Zz.class);
                                    if ((zz.getType() + "").equals(folderPath)) {
                                        images.add(zz);
                                    }
                                }
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }).start();
        }

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
