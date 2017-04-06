package com.example.huwang.mobilesafe20.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.bean.AppInfo;
import com.example.huwang.mobilesafe20.utils.FormatUtils;

/**
 * Created by huwang on 2017/4/4.
 */

public class AppManagerAdapter extends ArrayAdapter<AppInfo> {
	private List<AppInfo> userapps = new ArrayList<AppInfo>();
	private List<AppInfo> sysapps = new ArrayList<AppInfo>();

	public AppManagerAdapter(Context context, List<AppInfo> userapps, List<AppInfo> sysapps) {
		super(context, 0);
		this.userapps = userapps;
		this.sysapps = sysapps;
	}

	// 返回列表的行数
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2 + userapps.size() + sysapps.size();
	}

	// 返回行视图的种类
	@Override
	public int getViewTypeCount() {
		return 2; // 视图类型 0 标题 1应用程序信息
	}

	// 返回指定下标的行视图的类型
	@Override
	public int getItemViewType(int position) {// -->getView

		// 标题
		if (0 == position) {
			return 0;// 类型为0的视图 标题
		} else if ((1 + userapps.size()) == position) {
			return 0;// 类型为0的视图 标题
		} else {
			return 1;// 1应用程序信息
		}
	}

	class ViewHolder {
		TextView title;
		TextView app_name;
		TextView app_location;
		TextView app_size;
		ImageView app_icon;
	}

	// 返回列表的行视图 显示指定下标的数据

	// 如果 是复杂ListView优化的代码量是普通列表的N倍
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		if (type == 0) {// 标题
			return setTitleView(position, convertView);
		} else {
			return setAppInfo(position, convertView);

		}
	}

	private View setAppInfo(int position, View convertView) {
		// 获取行视图 经过优化
		// 1.优化inflate
		// 2.减少findViewById
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.view_item_app_info, null);
			holder = new ViewHolder();
			holder.app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
			holder.app_name = (TextView) convertView.findViewById(R.id.app_name);
			holder.app_location = (TextView) convertView.findViewById(R.id.app_location);
			holder.app_size = (TextView) convertView.findViewById(R.id.app_size);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AppInfo info = null;
		// 数据 设置值
		if (position > 0 && position < (1 + userapps.size())) {
			// 用户应用程序信息
			info = userapps.get(position - 1);
		} else if (position >= 2 + userapps.size()) {
			// 系统应用程序信息
			info = sysapps.get(position - 2 - userapps.size());
		}

		// 赋值
		holder.app_name.setText(info.name);
		if (info.isInPhone) {
			holder.app_location.setText("手机内部");
		} else {
			holder.app_location.setText("sd卡");
		}
		// holder.app_location.setText(info.location);
		holder.app_size.setText(FormatUtils.formatSize(getContext(), info.size));
		holder.app_icon.setImageDrawable(info.icon);
		// byte-->K M G
		// 返回视图
		return convertView;
	}

	/**
	 * 设置标题视图
	 *
	 * @param position
	 * @param convertView
	 * @return
	 */
	private View setTitleView(int position, View convertView) {
		// 用户应用个数
		// 系统应用个数
		ViewHolder holder = null;
		if (convertView == null) {
			// 1.重用视图 inflate
			// 2.减少findViewById
			holder = new ViewHolder();
			convertView = View.inflate(getContext(), R.layout.view_item_app_title, null);
			holder.title = (TextView) convertView.findViewById(R.id.apptitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置值
		if (position == 0) {
			holder.title.setText("用户应用:" + userapps.size());
		} else {
			holder.title.setText("系统应用:" + sysapps.size());
		}
		// 返回设置值 的视图
		return convertView;
	}

}
