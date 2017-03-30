package com.example.huwang.mobilesafe20.utils;

import android.telephony.SmsManager;

public class SmsUtil {


	public static void sendTextSms(String sendNumber, String body) {
		// SmsManager.getDefault().sendTextMessage(目标号码, 当前手机号, 短信内容, null,
		// null);

		SmsManager.getDefault().sendTextMessage(sendNumber, null, body, null, null);
	}
}
