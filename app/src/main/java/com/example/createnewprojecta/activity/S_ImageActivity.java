package com.example.createnewprojecta.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.createnewprojecta.R;

public class S_ImageActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_imageactivity);
        imageView = findViewById(R.id.im);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.xfy);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.zui);
        Bitmap bitmap2 = mergeBitmap(bitmap,bitmap1);
        imageView.setImageBitmap(bitmap2);
    }
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap)
    {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 0, 0, null);
        return bitmap;}

}
