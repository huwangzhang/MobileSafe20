package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.adapter.BlackNumberAdapter;
import com.example.huwang.mobilesafe20.bean.BlackNumberInfo;
import com.example.huwang.mobilesafe20.db.dao.BlackNumberDao;
import com.example.huwang.mobilesafe20.service.SmsCallProtectService;
import com.example.huwang.mobilesafe20.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huwang on 2017/3/30.
 */

public class SmsCallActivity extends Activity {
    @ViewInject(R.id.listview)
    ListView listview;
    @ViewInject(R.id.no_data)
    TextView no_data;
    private List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();

    @OnClick(R.id.add)
    public void add(View view) {
        // 创建工具
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        // 设置内容与事件
        buidler.setTitle(null);
        View viewDialog = View.inflate(this, R.layout.view_dialog_addnumber, null);
        buidler.setView(viewDialog);
        // 弹出
        final AlertDialog dialog = buidler.create();
        dialog.show();

        final EditText number = (EditText) viewDialog.findViewById(R.id.number);
        Button ok = (Button) viewDialog.findViewById(R.id.ok);
        Button cancle = (Button) viewDialog.findViewById(R.id.cancle);
        final CheckBox checkbox_phone = (CheckBox) viewDialog.findViewById(R.id.checkbox_phone);
        final CheckBox checkbox_sms = (CheckBox) viewDialog.findViewById(R.id.checkbox_sms);
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String numberStirng = number.getText().toString().trim();
                boolean isphoneCheck = checkbox_phone.isChecked();
                boolean issmsCheck = checkbox_sms.isChecked();

                BlackNumberInfo info = new BlackNumberInfo();
                info.number = numberStirng;

                if (!isphoneCheck & !issmsCheck) {
                    ToastUtil.shortToast(getBaseContext(), "必须设置模式");
                    return;
                } else {
                    if (isphoneCheck & issmsCheck) {
                        info.mode = "0";
                    } else if (isphoneCheck & !issmsCheck) {
                        info.mode = "1";
                    } else if (!isphoneCheck & issmsCheck) {
                        info.mode = "2";
                    }
                    dao.add(info);//
                    list.add(0, info);// 数理增加
                    adapter.notifyDataSetChanged();// 刷新 同步显示列表 上
                    no_data.setVisibility(View.GONE);
                    dialog.dismiss();
                }

            }
        });

    }

    /**
     * activity成员方法
     *
     * @param info
     */
    public void delteAndRefreshUI(BlackNumberInfo info) {
        dao.delete(info.id);
        list.remove(info);
        // 刷新
        adapter.notifyDataSetChanged();// 刷新列表
        if (list.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
        }
    }

    BlackNumberDao dao;
    BlackNumberAdapter adapter;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (currPage == 1) {
                // List<BlackNumberInfo> templist = dao.findAll();
                // list.addAll(templist);
                // 表示内容的适配器
                adapter = new BlackNumberAdapter(SmsCallActivity.this, list);
                listview.setAdapter(adapter);
                if (list.size() == 0) {
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    no_data.setVisibility(View.GONE);
                }
                addScollListener();
            } else {//!=1 非首页
                adapter.notifyDataSetChanged();// 告诉列表重新显示数据
            }
        };
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_call);
        ViewUtils.inject(this);
        //测试服务
//		startService(new Intent(this,SmsCallProtectService.class));
        // 测试-->bug
        // // 假数据
        // currPage = 1;
        // for (int i = 0; i < 20; i++) {
        // BlackNumberInfo info = new BlackNumberInfo();
        // info.id = "00" + i;
        // info.number = "   " + currPage + "页--5554" + i;
        // info.mode = "0";
        // list.add(info);
        // }

        dao = new BlackNumberDao(this);
        //Alt_shift+M
        queryNextPage();
    }
    private void queryNextPage() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                    ++currPage;
                    List<BlackNumberInfo> page = dao.findPageByIndex(currPage, 20);
                    list.addAll(page);

                    Message msg=handler.obtainMessage();//new Message 优化的写法 先查找可重用的message
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            };
        }.start();
    }

    private void addScollListener() {
        // Button OnCliCkListener
        // ListView OnScrollListener
        listview.setOnScrollListener(new ListView.OnScrollListener() {
            //状态   1.IDLE空闲2.Touch Scroll拖动 3.滚动 onFliling
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://
                        int positionLast = listview.getLastVisiblePosition();
                        if (positionLast == (list.size() - 1)) {
                            ToastUtil.shortToast(getBaseContext(), "到底部");
                            Log.i("zhang", "SCROLL_STATE_IDLE---");
                            // 加载下一页
//						++currPage;
//						for (int i = 0; i < 20; i++) {
//							BlackNumberInfo info = new BlackNumberInfo();
//							info.id = "00" + i;
//							info.number = "   " + currPage + "页--5554" + i;
//							info.mode = "0";
//							list.add(info);
//						}
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
                            queryNextPage();
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

                        break;

                }

            }
            // totalItemCount 数量
            // firstVisibleItem 当前显示第一行对应的下标
            // 0
            // size()-1
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {



            }
        });
    }

    private int currPage = 0;
}
