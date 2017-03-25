package com.example.huwang.mobilesafe20.bean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/3/25.
 */

public class ContactUtils {
    public static List<ContactInfo> getAllContacts(Context context) {
        List<ContactInfo> list = new ArrayList<ContactInfo>();
        //获取解析者
        ContentResolver resolver = context.getContentResolver();
        //访问地址一
//        Uri raw_contacts = Uri.parse("content://com.android.contacts/raw_contacts");
//        Uri data = Uri.parse("content://com.android.contacts/data");
        //查询语句

        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Log.i("zhang", "cursor------所有联系人");
        while (cursor.moveToNext()) {
            Log.i("zhang", "循环内");
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            ContactInfo info = new ContactInfo();
            info.setId(id);
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            info.setName(name);
            Cursor item = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = " + id, null, null);
            while (item.moveToNext()) {
                String phoneNumber = item.getString(item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                info.setPhone(phoneNumber);
            }
            item.close();
            list.add(info);
        }

        cursor.close();
        return list;
    }
}
