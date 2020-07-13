package com.example.createnewprojecta.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.createnewprojecta.R;
import com.example.imageditlibrary.BaseActivity;
import com.example.imageditlibrary.fragment.StickerFragment2;
import com.example.imageditlibrary.utils.BitmapUtils;
import com.example.imageditlibrary.view.StickerView;
import com.example.imageditlibrary.view.imagezoom.ImageViewTouch;
import com.example.imageditlibrary.view.imagezoom.ImageViewTouchBase;

/**
 * @LogIn Name zhangyingyu
 * @Create by 张瀛煜 on 2020-07-13 at 14:51 ：）
 */
public class Z_CameraCreate extends BaseActivity {
    public static final String FILE_PATH = "file_path";
    public static final String EXTRA_OUTPUT = "extra_output";
    public static final String SAVE_FILE_PATH = "save_file_path";


    public String filePath;// 需要编辑图片路径
    public String saveFilePath;// 生成的新图片路径
    public String type;
    public Z_CameraCreate mContext;
    private int imageWidth, imageHeight;// 展示图片控件 宽 高
    private ImageView change1;
    private TextView title;
    private ImageView change;
    public StickerView mStickerView;// 贴图层View
    private Bitmap mainBitmap;// 底层显示Bitmap
    public ImageViewTouch mainImage;
    public StickerFragment2 mStickerFragment;// 贴图Fragment
    private SaveImageTask mSaveImageTask;
    public int mode = MODE_NONE;// 当前操作模式
    public static final int MODE_NONE = 0;
    public static final int MODE_STICKERS = 1;

    protected int mOpTimes = 0;
    protected boolean isBeenSaved = false;

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
        Intent it = new Intent(context, Z_CameraCreate.class);
        it.putExtra(Z_CameraCreate.FILE_PATH, editImagePath);
        it.putExtra(Z_CameraCreate.EXTRA_OUTPUT, outputPath);
        it.putExtra("type", type);
        context.startActivityForResult(it, requestCode);
    }

    public Bitmap getMainBit() {
        return mainBitmap;
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getIntent().getStringExtra(FILE_PATH);
        saveFilePath = getIntent().getStringExtra(EXTRA_OUTPUT);// 保存图片路径
        setContentView(R.layout.activity_canmera_edit);
        initView();
        Log.i("111", "onCreate: Z_CameraCreate");
        change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Z_CameraCreate.this);
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

    private void initView() {
        mContext = this;
        type = getIntent().getStringExtra("type");
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels / 2;
        imageHeight = metrics.heightPixels / 2;
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
        mStickerFragment = StickerFragment2.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.farmeLayout, mStickerFragment).commit();
        changeMainBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.whiteimage), false);
    }

    /**
     * 保存图像
     * 完成后退出
     */
    private final class  SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
        private Dialog dialog;

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            return BitmapUtils.saveBitmap(params[0], saveFilePath, Z_CameraCreate.this);
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
        if (needPushUndoStack) {
            doSaveImage();
        }
    }


    protected void doSaveImage() {
        if (mSaveImageTask != null) {
            mSaveImageTask.cancel(true);
        }
        mSaveImageTask = new SaveImageTask();
        mSaveImageTask.execute(mainBitmap);
    }

}
