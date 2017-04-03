package com.example.huwang.mobilesafe20.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Xml;

import com.example.huwang.mobilesafe20.bean.SmsInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmsUtil {


	public static void sendTextSms(String sendNumber, String body) {
		// SmsManager.getDefault().sendTextMessage(目标号码, 当前手机号, 短信内容, null,
		// null);

		SmsManager.getDefault().sendTextMessage(sendNumber, null, body, null, null);
	}
	/**
	 * 解析短信
	 *
	 * @param intent
	 * @return
	 */
	public static List<SmsMessage> parseSms(Intent intent) {
		List<SmsMessage> list = new ArrayList<SmsMessage>();
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for (Object pdu : pdus) {
			// SmsMessage:封装短信参数的对象. 号码 内容
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
			list.add(sms);
		}
		return list;
	}

	/**
	 * 获取所有的短信
	 *
	 * @param context
	 * @return
	 */
	public static List<SmsInfo> findAllFromProvider(Context context) {
		List<SmsInfo> list = new ArrayList<SmsInfo>();
		// ①　配置权限
		// ②　获取解析者
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms");
		// ③　返回游标
		// select address,date,body,type from sms ;
		// resolver.query(访问地址, 返回字段, where条件, where参数, 排序);
		Cursor cursor = resolver.query(uri, new String[] { "address", "date", "body", "type" }, null, null, null);
		while (cursor.moveToNext()) {
			SmsInfo info = new SmsInfo();
			// ④　读出数据
			info.address = cursor.getString(cursor.getColumnIndex("address"));
			info.date = cursor.getString(cursor.getColumnIndex("date"));
			info.body = cursor.getString(cursor.getColumnIndex("body"));
			info.type = cursor.getString(cursor.getColumnIndex("type"));
			list.add(info);
		}

		cursor.close();
		return list;
	}

	/**
	 * 备份
	 *
	 * @param context
	 * @param
	 * @throws Exception
	 *             OnProgressListener接口的实现类的实例
	 */
	public static void backup(Context context, OnProgressListener listener) throws Exception {
		int progress = 0;
		List<SmsInfo> infos = findAllFromProvider(context);

		// 开始
		if (listener != null) {
			listener.onStart(infos.size());
		}
		// Notification
		// ①　检测文件是否存
		String savePath = "/data/data/" + context.getPackageName() + "/sms.xml";
		File file = new File(savePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// ②　获取序列化器
		// ③　逐个生成标签
		XmlSerializer writer = Xml.newSerializer();
		FileOutputStream out = new FileOutputStream(file);
		// writer.setOutput(输出流, 编码);
		writer.setOutput(out, "utf-8");
		writer.startDocument("UTF-8", true);

		writer.startTag(null, "sms-list");
		writer.attribute(null, "count", infos.size() + "");
		// <sms-list count=10>
		// <sms >
		// <address></address>
		// <body></body>
		// ...
		// <sms>
		// </sms-list>
		for (SmsInfo info : infos) {
			// info--->xml
			writer.startTag(null, "sms");
			writer.startTag(null, "address");
			writer.text(info.address);
			writer.endTag(null, "address");

			writer.startTag(null, "date");
			writer.text(info.date);
			writer.endTag(null, "date");
			writer.startTag(null, "type");
			writer.text(info.type);
			writer.endTag(null, "type");
			writer.startTag(null, "body");
			writer.text(EncrptUtils.encrypt(info.body));
			writer.endTag(null, "body");
			writer.endTag(null, "sms");
			Thread.sleep(500);
			++progress;
			if (listener != null) {
				listener.onProgressChage(progress, infos.size());
			}
		}
		writer.endTag(null, "sms-list");
		// 关闭
		writer.endDocument();
	}

	// 生成/创建 监听器
	// 1.哪一部代码可能发生变化
	// 2.Alt+shift+M
	// 3.方法要抽象abstract
	// 4.比抽象类更抽象的是接口 OnClickListener
	public static interface OnProgressListener {
		public abstract void onProgressChage(int progress, int max);

		// {
		// publishProgress(progress);
		// }

		public abstract void onStart(int max);
		// {
		// progressbar.setMax(infos.size());
		// }
	}

	/**
	 * 获取短信集合从备份文件
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static List<SmsInfo> findAllFromXml(Context context) throws Exception {
		String savePath = "/data/data/" + context.getPackageName() + "/sms.xml";
		// ①　通过XmlPullParser解析数据
		XmlPullParser parser = Xml.newPullParser();

		List<SmsInfo> list = null;
		// parser.setInput(输入流, 编码)
		FileInputStream input = new FileInputStream(savePath);
		parser.setInput(input, "utf-8");
		int evetType = XmlPullParser.START_DOCUMENT;// 第一行
		SmsInfo info = null;
		while (evetType != XmlPullParser.END_DOCUMENT) {
			switch (evetType) {
				case XmlPullParser.START_TAG:
					if ("sms-list".equals(parser.getName())) {
						list = new ArrayList<SmsInfo>();
					} else if ("sms".equals(parser.getName())) {
						info = new SmsInfo();
					} else if ("address".equals(parser.getName())) {
						info.address = parser.nextText();// 获取标签后的标签体：开始与结束之间的内容
					} else if ("type".equals(parser.getName())) {
						info.type = parser.nextText();// 获取标签后的标签体：开始与结束之间的内容
					} else if ("date".equals(parser.getName())) {
						info.date = parser.nextText();// 获取标签后的标签体：开始与结束之间的内容
					} else if ("body".equals(parser.getName())) {
						info.body = parser.nextText();// 获取标签后的标签体：开始与结束之间的内容
					}
					break;
				case XmlPullParser.END_TAG:
					if ("sms".equals(parser.getName())) {
						list.add(info);
					}
					break;
			}
			evetType = parser.next();// 下一标签
		}
		// ②　获得集合
		return list;
		// ③　集合同步到provider
	}

	/**
	 * 短信还原
	 *
	 * @param context
	 * @throws Exception
	 */
	public static void restore(Context context, OnProgressListener listener) throws Exception {
		List<SmsInfo> infos = findAllFromXml(context);
		//产生了最大条数
		if (listener != null) {
			listener.onStart(infos.size());
		}
		// 获取内容解析者
		ContentResolver resolver = context.getContentResolver();
		// 访问地址
		int progress=0;
		Uri uri = Uri.parse("content://sms");
		for (SmsInfo info : infos) {
			// insert into sms (
			// address,date,body,type)values('110','22222222','当前是测试',6);
			// ContentValues:代表一条记录 是一个Map集合
			ContentValues values = new ContentValues();
			values.put("address", info.address);
			values.put("date", info.date);
			values.put("body", info.body);
			values.put("type", info.type);
			resolver.insert(uri, values);// 执行插入sql
			++progress;
			Thread.sleep(500);
			//产生进度变化的事件
			if (listener != null) {
				listener.onProgressChage(progress, infos.size());
			}
		}

	}

}
