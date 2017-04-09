package com.example.huwang.mobilesafe20.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.activity.AppLockListActivity;
import com.example.huwang.mobilesafe20.bean.AppInfo;
import com.example.huwang.mobilesafe20.db.dao.LockAppDao;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huwang on 2017/4/8.
 */

public class AppTypeLockAdapter extends ArrayAdapter<AppInfo> {
    List<AppInfo> list = new ArrayList<AppInfo>();

    private LockAppDao dao=null;
    public AppTypeLockAdapter(Context context, List<AppInfo> objects) {
        super(context, 0, 0, objects);
        list = objects;
        dao=new LockAppDao(context);
    }

    public static class ViewHolderAppLock {
        ImageView app_icon;
        TextView app_name;
        TextView app_state;

    }

    // 返回行视图，显示指定下标的数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderAppLock holder = null;
        // 视图
        if (convertView == null) {
            holder = new ViewHolderAppLock();
            convertView = View.inflate(getContext(), R.layout.view_item_app_lock, null);
            holder.app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.app_name = (TextView) convertView.findViewById(R.id.app_name);
            holder.app_state = (TextView) convertView.findViewById(R.id.app_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderAppLock) convertView.getTag();

        }
        // 数据
        final AppInfo info = getItem(position);// list.get(postion);

        // 设置值
        holder.app_icon.setImageDrawable(info.icon);
        holder.app_name.setText(info.name);
        holder.app_state.setText(info.isLock ? "解锁" : "加锁");
        holder.app_state.setSelected(info.isLock ? true : false);


        return convertView;
    }

    private Handler handler = new Handler();


    public  void startDeleteAnimation( final View  convertView, final AppInfo info) {
        TranslateAnimation anim;
        if (info.isLock) {
            anim = new TranslateAnimation//
                    (TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, -1.0f, // x坐标
                            TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);// y坐标
        } else {
            // ①　添加事件
            // ②　创建动画
            anim = new TranslateAnimation//
                    (TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 1.0f, // x坐标
                            TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);// y坐标

        }

        anim.setDuration(1000);// 时长
        convertView.setAnimation(anim);
        // ③　使用动画
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // 缩写方法
                handler.post(new Runnable() {// new Message
                    @Override
                    public void run() {// 不是子线程
                        if(!info.isLock)
                        {
                            //添加 加锁程序的包名
                            dao.add(info.packageName);
                            list.remove(info);
                            notifyDataSetChanged();// 列表的刷新
                            //更新集合个数据
                            AppLockListActivity activity=(AppLockListActivity) getContext();
                            TextView  app_status=(TextView) activity.findViewById(R.id.app_status);
                            app_status.setText("未加锁程序:"+list.size());

                        }else
                        {
                            //同步数
                            dao.delete(info.packageName);
                            list.remove(info);
                            notifyDataSetChanged();// 列表的刷新
                            //更新集合个数据
                            AppLockListActivity activity=(AppLockListActivity) getContext();
                            TextView app_status=(TextView) activity.findViewById(R.id.app_status);
                            app_status.setText("已加锁程序:"+list.size());;
                        }


                    }
                });

            }
        }.start();
    }
}
