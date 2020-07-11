package com.example.createnewprojecta.util2;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/21.
 */
public class MyAutoSizeRelativeLayout extends android.support.v7.widget.AppCompatImageView {
    private PointF centerPoint;
    private float firstdistance;
    private Matrix matrix;

    public MyAutoSizeRelativeLayout(Context context) {
        super(context);
        initParams();
    }

    private void initParams() {
        matrix = new Matrix();
        setImageMatrix(matrix);
    }

    public MyAutoSizeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public MyAutoSizeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int count = event.getPointerCount();
        if(count == 1){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    System.out.println("actiondown 1----");
                    break;
                case MotionEvent.ACTION_MOVE:
                    System.out.println("actionmove 1----");
                    break;
                case MotionEvent.ACTION_UP:
                    System.out.println("actionup 1----");
                    break;
                default:
                    break;
            }
        }else if(count == 2){
            switch (event.getAction()&MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_POINTER_DOWN:
                    firstdistance = getDistance(event);
                    centerPoint = getCenterPoint(event);
                    System.out.println("actiondown 2===");
                    break;
                case MotionEvent.ACTION_MOVE:
                    float distance = getDistance(event);
                    float scale = distance / firstdistance;
                    matrix.postScale(scale,scale,centerPoint.x,centerPoint.y);
                    setImageMatrix(matrix);
                    System.out.println(scale + "====");
                    firstdistance = distance;
                    System.out.println("actionmove 2===");
                    break;
                case MotionEvent.ACTION_UP:

                    break;
                default:
                    break;
            }
        }


        return true;
    }

    /**
     * 获取两个点的中心点坐标
     * @param event
     */
    private PointF getCenterPoint(MotionEvent event) {
        PointF point = new PointF();
        point.x = (event.getX()+event.getX(1))/2;
        point.y = (event.getY()+event.getY(1))/2;
        return point;
    }

    /**
     * 两根手指时手指间的距离
     * @param event
     */
    private float getDistance(MotionEvent event) {
        float x1 = event.getX();
        float y1 = event.getY();

        float x2 = event.getX(1);
        float y2 = event.getY(1);
        float distance = (float) Math.sqrt((x1 - x2)*(x1 - x2)+(y1 - y2)*(y1- y2));

        return distance;
    }
}
