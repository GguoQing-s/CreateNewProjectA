package com.example.createnewprojecta.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;


import com.example.createnewprojecta.gifmaker.AnimatedGifEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Create by 张瀛煜 on 2020-06-08 ：）
 */
public class GifUtil {
    /**
     * 生成gif图
     *
     * @param delay 图片之间间隔的时间
     */
    public String createGif(List<Bitmap> pics, String file_name, int delay, final Context context) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
        localAnimatedGifEncoder.start(baos);//start
        localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
        localAnimatedGifEncoder.setDelay(delay);
        localAnimatedGifEncoder.setTransparent(Color.BLACK);

        //【注意1】开始生成gif的时候，是以第一张图片的尺寸生成gif图的大小，后面几张图片会基于第一张图片的尺寸进行裁切
        //所以要生成尺寸完全匹配的gif图的话，应先调整传入图片的尺寸，让其尺寸相同
        //【注意2】如果传入的单张图片太大的话会造成OOM，可在不损失图片清晰度先对图片进行质量压缩
        if (pics.isEmpty()) {
        } else {
            for (int i = 0; i < pics.size(); i++) {
                // Bitmap localBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(pics.get(i)), 512, 512);
                localAnimatedGifEncoder.addFrame(pics.get(i));

            }
        }
        localAnimatedGifEncoder.finish();//finish


        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFMakerDemo");
        if (!file.exists()) file.mkdir();
        final String path = Environment.getExternalStorageDirectory().getPath() + "/GIFMakerDemo/" + file_name + ".gif";
        try {
            FileOutputStream fos = new FileOutputStream(path);
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  path;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(context).clear(gif_image);
//                Glide.with(context).load(new File(path)).into(gif_image);
//                Toast.makeText(context, "Gif已生成。保存路径：\n" + path, Toast.LENGTH_LONG).show();
//            }
//        });
    }

}
