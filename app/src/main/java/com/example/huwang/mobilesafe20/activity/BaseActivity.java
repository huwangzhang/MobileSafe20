package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.utils.ToastUtil;

import static com.example.huwang.mobilesafe20.R.drawable.next;

/**
 * Created by huwang on 2017/3/22.
 */

public class BaseActivity extends Activity {
    private Class<?> preActivity = null;
    private Class<?> nextActivity = null;
    private GestureDetector gestureDetector = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GestureDetector.OnGestureListener;
        //GestureDetector.SimpleOnGestureListener;
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
//            @Override
//            public boolean onDown(MotionEvent e) {
//                ToastUtil.shortToast(getBaseContext(),  "用户点击了!");
//                return super.onDown(e);
//            }

            /**
             * @param e1 按下坐标对象
             * @param e2
             * @param velocityX  速度
             * @param velocityY
             * @return false 系统处理 true自己处理
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                if (Math.abs(velocityX) < 200) {
//
//                }
                if (e2.getRawX() - e1.getRawX() > 200) {
                    prePage(null);
                    return true;
                }
                if (e1.getRawX() - e2.getRawX() > 200) {
                    nextPage(null);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        gestureDetector = new GestureDetector(listener);
    }

    public void setPreActivity(Class<?> preActivity) {
        this.preActivity = preActivity;
    }

    public void setNextActivity(Class<?> nextActivity) {
        this.nextActivity = nextActivity;
    }

    /**
     * 进入下一页
     *
     * @param view
     */
    public void nextPage(View view) {
        if (nextActivity != null) {
            startActivity(new Intent(getBaseContext(), nextActivity));
            finish();
            overridePendingTransition(R.anim.push_left_out, R.anim.push_right_in);
        }
    }

    /**
     * 返回上一页
     *
     * @param view
     */
    public void prePage(View view) {
        if (preActivity != null) {
            startActivity(new Intent(getBaseContext(), preActivity));
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        }
    }

    /**
     * 该方法响应用户对界面的操作
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
