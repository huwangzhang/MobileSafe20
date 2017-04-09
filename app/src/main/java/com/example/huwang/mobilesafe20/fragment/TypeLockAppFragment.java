package com.example.huwang.mobilesafe20.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.adapter.AppTypeLockAdapter;
import com.example.huwang.mobilesafe20.bean.AppInfo;
import com.example.huwang.mobilesafe20.db.dao.LockAppDao;
import com.example.huwang.mobilesafe20.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huwang on 2017/4/8.
 */

public class TypeLockAppFragment extends Fragment {
    // 1.继承 Fragment v4
    // 2.重写onCrateView 返回Fragment显示的视图 null代表空白
    // 返回视图对象 ，给当前的Fragment使用
    // Actvity 使用setContentView
    // 3.
    // 4.添加集合
    private List<AppInfo> list = new ArrayList<AppInfo>();

    private LockAppDao dao=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final  View view = View.inflate(getActivity(), R.layout.fragment_type_lockapp, null);
        dao=new LockAppDao(getActivity());
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                getActivity().findViewById(R.id.view_waiting).setVisibility(View.VISIBLE);
            };
            // 子线程
            @Override
            protected Void doInBackground(Void... params) {
                list.clear();
                // TODO Auto-generated method stub
                List<AppInfo> temp = AppUtils.findAll(getActivity());
                for(AppInfo info:temp)
                {
                    //是否加锁   true
                    if(dao.isLock(info.packageName))
                    {
                        info.isLock=true;
                        list.add(info);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//				list.addAll(temp);
                return null;
            }

            // 任务:取到数据
            protected void onPostExecute(Void result) {
                ListView listview = (ListView) view.findViewById(R.id.listview_type_lock);
                // 控件与内容
                final AppTypeLockAdapter adapter = new AppTypeLockAdapter(getActivity(), list);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    // View view行视图
                    //position下标
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.startDeleteAnimation(view, list.get(position));
                    }
                });

                //更新程序个数
                TextView app_status=(TextView) getActivity().findViewById(R.id.app_status);
                app_status.setText("已锁程序:"+list.size());


                getActivity().findViewById(R.id.view_waiting).setVisibility(View.GONE);
            }
        }.execute();
        return view;
    }
}
