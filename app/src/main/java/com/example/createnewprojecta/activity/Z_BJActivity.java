package com.example.createnewprojecta.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.createnewprojecta.R;
import com.example.imageditlibrary.BaseActivity;
import com.example.imageditlibrary.fragment.StickerFragment;
import com.example.imageditlibrary.utils.BitmapUtils;
import com.example.imageditlibrary.utils.FileUtil;
import com.example.imageditlibrary.view.StickerView;
import com.example.imageditlibrary.view.imagezoom.ImageViewTouch;
import com.example.imageditlibrary.view.imagezoom.ImageViewTouchBase;

/**
 * @LogIn Name zhangyingyu
 * @Create by 张瀛煜 on 2020-07-10 at 17:55 ：）
 */
public class Z_BJActivity extends BaseActivity {

    public static final String FILE_PATH = "file_path";
    public static final String EXTRA_OUTPUT = "extra_output";
    public static final String SAVE_FILE_PATH = "save_file_path";

    public static final String IMAGE_IS_EDIT = "image_is_edit";

    public static final int MODE_NONE = 0;
    public static final int MODE_STICKERS = 1;
    public StickerFragment mStickerFragment;// 贴图Fragment
    public ImageViewTouch mainImage;

    protected int mOpTimes = 0;
    protected boolean isBeenSaved = false;

    public String filePath;// 需要编辑图片路径
    public String saveFilePath;// 生成的新图片路径
    private Z_BJActivity mContext;
    public int mode = MODE_NONE;// 当前操作模式
    private Bitmap mainBitmap;// 底层显示Bitmap
    private int imageWidth, imageHeight;// 展示图片控件 宽 高
    private LinearLayout layout;
    private ImageView change1;
    private TextView title;
    private ImageView change;
    public StickerView mStickerView;// 贴图层View
    private LoadImageTask mLoadImageTask;
    public String type;
    private int screenWidth;
    private SaveImageTask mSaveImageTask;

    /**
     * @param context
     * @param editImagePath
     * @param outputPath
     * @param requestCode
     */
    public static void start(Activity context, final String editImagePath, final String outputPath, final int requestCode, final String type) {
        if (TextUtils.isEmpty(editImagePath)) {
            Toast.makeText(context, R.string.no_choose, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent it = new Intent(context, Z_BJActivity.class);
        it.putExtra(Z_BJActivity.FILE_PATH, editImagePath);
        it.putExtra(Z_BJActivity.EXTRA_OUTPUT, outputPath);
        it.putExtra("type", type);
        context.startActivityForResult(it, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        filePath = getIntent().getStringExtra(FILE_PATH);
        saveFilePath = getIntent().getStringExtra(EXTRA_OUTPUT);// 保存图片路径
        initView();
        dianjia();
        initData();
        initTitle();
    }
    private void initTitle() {
        /*LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.height = screenWidth / 8;
        layoutParams.width = screenWidth;
        layout.setLayoutParams(layoutParams);
        title.setTextSize(22);*/
    }
    private void initData() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        screenWidth = outSize.x;


    }

    private void dianjia() {
        change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Z_BJActivity.this);
                builder.setTitle("提示");
                builder.setCancelable(false);
                builder.setMessage("是否返回首页！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }


    /**
     * 异步载入编辑图片
     *
     * @param filepath
     */
    public void loadImage(String filepath) {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask();
        mLoadImageTask.execute(filepath);
    }

    /**
     * 导入文件图片任务
     */
    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Log.i("aaa", "doInBackground: "+params);
            return BitmapUtils.getSampledBitmap(params[0], imageWidth,
                    imageHeight);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            changeMainBitmap(result, false);
        }
    }// end inner class

    private void initView() {
        mContext = this;
        type = getIntent().getStringExtra("type");
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels / 2;
        imageHeight = metrics.heightPixels / 2;
        layout = findViewById(R.id.layout);
        change1 = findViewById(R.id.change1);
        title = findViewById(R.id.title);
        title.setText("制作");
        change = findViewById(R.id.change);
        mainImage = findViewById(R.id.main_image);
        change.setImageResource(R.drawable.baocun);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStickerFragment.applyStickers();//保存贴图
//                doSaveImage();
            }
        });
        mStickerView = findViewById(R.id.sticker_panel);


        mStickerFragment = StickerFragment.newInstance();
        //bottomGallery.setAdapter(mBottomGalleryAdapter);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.farmeLayout, mStickerFragment).commit();



        changeMainBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.whiteimage), false);
    }

    protected void doSaveImage() {
        if (mSaveImageTask != null) {
            mSaveImageTask.cancel(true);
        }
        mSaveImageTask = new SaveImageTask();
        mSaveImageTask.execute(mainBitmap);
    }

    /**
     * 保存图像
     * 完成后退出
     */
    private final class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
        private Dialog dialog;

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            return BitmapUtils.saveBitmap(params[0], saveFilePath, Z_BJActivity.this);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onCancelled(Boolean result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Z_BJActivity.getLoadingDialog(mContext, R.string.saving_image, false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result) {
                Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                resetOpTimes();
                // onSaveTaskDone();
            } else {
                Toast.makeText(mContext, R.string.save_error, Toast.LENGTH_SHORT).show();
            }
        }
    }//end inner class


    public void resetOpTimes() {
        isBeenSaved = true;
    }

    @Override
    public void onBackPressed() {
        switch (mode) {
            case MODE_STICKERS:
                mStickerFragment.backToMain();
                return;
        }// end switch

        if (canAutoExit()) {
            onSaveTaskDone();
        } else {//图片还未被保存    弹出提示框确认
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.exit_without_save)
                    .setCancelable(false).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mContext.finish();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    protected void onSaveTaskDone() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FILE_PATH, filePath);
        returnIntent.putExtra(EXTRA_OUTPUT, saveFilePath);
        returnIntent.putExtra(IMAGE_IS_EDIT, mOpTimes > 0);

        FileUtil.ablumUpdate(this, saveFilePath);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public boolean canAutoExit() {
        return isBeenSaved || mOpTimes == 0;
    }

    private final class BottomGalleryAdapter extends FragmentPagerAdapter {
        public BottomGalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int index) {
            // System.out.println("createFragment-->"+index);
            switch (index) {
                case StickerFragment.INDEX:// 贴图
                    return mStickerFragment;
            }//end switch
            return mStickerFragment;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    /**
     * @param newBit
     * @param needPushUndoStack
     */
    public void changeMainBitmap(Bitmap newBit, boolean needPushUndoStack) {
        if (newBit == null)
            return;

        if (mainBitmap == null || mainBitmap != newBit) {
            mainBitmap = newBit;
            mainImage.setImageBitmap(mainBitmap);
            mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        }
        if (needPushUndoStack){
            doSaveImage();
        }
    }

    public Bitmap getMainBit() {
        return mainBitmap;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Z_BJActivity.this);
        builder.setTitle("提示");
        builder.setCancelable(false);
        builder.setMessage("是否返回首页！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        return super.onKeyDown(keyCode, event);
    }
}
