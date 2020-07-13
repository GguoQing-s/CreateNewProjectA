package com.example.imageditlibrary.fragment;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.android.volley.VolleyError;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.activity.Z_BJActivity;
import com.example.createnewprojecta.activity.Z_CameraCreate;
import com.example.createnewprojecta.bean.Zz;
import com.example.createnewprojecta.net.VolleyLo;
import com.example.createnewprojecta.net.VolleyTo;
import com.example.imageditlibrary.BaseActivity;
import com.example.imageditlibrary.ModuleConfig;
import com.example.imageditlibrary.adapter.StickerAdapter;
import com.example.imageditlibrary.adapter.StickerAdapter2;
import com.example.imageditlibrary.adapter.StickerTypeAdapter;
import com.example.imageditlibrary.model.StickerBean;
import com.example.imageditlibrary.task.StickerTask;
import com.example.imageditlibrary.view.StickerItem;
import com.example.imageditlibrary.view.StickerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.media.audiofx.AudioEffect.ERROR;

/**
 * 贴图分类fragment
 *
 * @author panyi
 */
public class StickerFragment2 extends BaseCameraFragment {
    public static final int INDEX = ModuleConfig.INDEX_STICKER;

    public static final String TAG = StickerFragment2.class.getName();
    public static final String STICKER_FOLDER = "stickers";

    private View mainView;
    private ViewFlipper flipper;
    private View backToType;// 返回类型选择
    private RecyclerView typeList;// 贴图分类列表
    private RecyclerView stickerList;// 贴图素材列表
    private StickerView mStickerView;// 贴图显示控件
    private StickerAdapter2 mStickerAdapter;// 贴图列表适配器
    private List<Zz> mZz;

    private LoadStickersTask mLoadStickersTask;
    private List<StickerBean> stickerBeanList = new ArrayList<StickerBean>();

    private SaveStickersTask mSaveTask;

    public static StickerFragment2 newInstance() {
        StickerFragment2 fragment = new StickerFragment2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainView = inflater.inflate(R.layout.fragment_camera_image_sticker_type,
                null);
        //loadStickersData();

        return mainView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mStickerView = activity.mStickerView;
        flipper = (ViewFlipper) mainView.findViewById(R.id.flipper);
        flipper.setInAnimation(activity, R.anim.in_bottom_to_top);
        flipper.setOutAnimation(activity, R.anim.out_bottom_to_top);

        //
        typeList =  mainView
                .findViewById(R.id.stickers_type_list);
        typeList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        typeList.setLayoutManager(mLayoutManager);
        typeList.setAdapter(new StickerTypeAdapter(this));
        backToType = mainView.findViewById(R.id.back_to_type);// back按钮

        stickerList =  mainView.findViewById(R.id.stickers_list);
        stickerList.setHasFixedSize(true);
        LinearLayoutManager stickerListLayoutManager = new LinearLayoutManager(
                activity);
        stickerListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        stickerList.setLayoutManager(stickerListLayoutManager);
        mStickerAdapter = new StickerAdapter2(this);
        stickerList.setAdapter(mStickerAdapter);

        backToType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {// 返回上一级列表
                flipper.showPrevious();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(3);
            }
        }).start();
    }

    @Override
    public void onShow() {
        activity.mode = Z_BJActivity.MODE_STICKERS;
        activity.mStickerFragment.getmStickerView().setVisibility(
                View.VISIBLE);
    }

    //导入贴图数据
    private void loadStickersData() {
        if (mLoadStickersTask != null) {
            mLoadStickersTask.cancel(true);
        }
        mLoadStickersTask = new LoadStickersTask();
        mLoadStickersTask.execute(1);
    }


    /**
     * 导入贴图数据
     */
    private final class LoadStickersTask extends AsyncTask<Integer, Void, Void> {
        private Dialog loadDialog;

        public LoadStickersTask() {
            super();
            loadDialog = BaseActivity.getLoadingDialog(getActivity(), R.string.saving_image, false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadDialog.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            stickerBeanList.clear();
            AssetManager assetManager = getActivity().getAssets();
            try {
                String[] lists = assetManager.list(STICKER_FOLDER);
                for (String parentPath : lists) {

                }//end for each
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadDialog.dismiss();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadDialog.dismiss();
        }
    }//end inner class

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadStickersTask != null) {
            mLoadStickersTask.cancel(true);
        }
    }

    /**
     * 跳转至贴图详情列表
     *
     * @param path
     */
    public void swipToStickerDetails(String path) {
        mStickerAdapter.addStickerImages(path);
        flipper.showNext();
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                mStickerView.addBitImage(bitmap);
                Log.i(TAG, "handleMessage: ");
            }
            else if (msg.what==3){

                selectedStickerItem1(activity.filePath);
            }
            return false;
        }
    });

    /**
     * 选择贴图加入到页面中
     *
     * @param path
     */
    public void selectedStickerItem(final String path) {
        new Thread() {
            public void run() {
                try {
                    Log.i(TAG, "run: " + path);
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = ERROR;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = ERROR;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    public void selectedStickerItem1(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        mStickerView.addBitImage(bitmap);
    }

    public StickerView getmStickerView() {
        return mStickerView;
    }

    public void setmStickerView(StickerView mStickerView) {
        this.mStickerView = mStickerView;
    }

    /**
     * 返回主菜单页面
     *
     * @author panyi
     */
    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end inner class

    @Override
    public void backToMain() {
       // activity.mode = Z_BJActivity.MODE_NONE;
    }

    /**
     * 保存贴图任务
     *
     * @author panyi
     */
    private final class SaveStickersTask extends StickerTask {
        public SaveStickersTask(Z_CameraCreate activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            LinkedHashMap<Integer, StickerItem> addItems = mStickerView.getBank();
            for (Integer id : addItems.keySet()) {
                StickerItem item = addItems.get(id);
                item.matrix.postConcat(m);// 乘以底部图片变化矩阵
                canvas.drawBitmap(item.bitmap, item.matrix, null);
            }// end for
        }

        @Override
        public void onPostResult(Bitmap result) {
            mStickerView.clear();
            activity.changeMainBitmap(result, true);
            backToMain();
        }
    }// end inner class

    /**
     * 保存贴图层 合成一张图片
     */
    public void applyStickers() {
        // System.out.println("保存 合成图片");
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
        mSaveTask = new SaveStickersTask((Z_CameraCreate) getActivity());
        mSaveTask.execute(activity.getMainBit());
    }
}// end class
