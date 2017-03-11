package com.tutu.tcontact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tutu on 2017/3/11.
 */

public class ContactsUtils {
    public static List<ContactInfo> getAllContacts() {
        // 获取联系人数据
        ContentResolver cr = App.app.getContentResolver();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
        };
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }

        List<ContactInfo> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String number = cursor.getString(1);

            ContactInfo info = new ContactInfo();
            info.setName(name);
            info.setNumber(number);

            result.add(info);
        }

        cursor.close();
        return result;
    }
}
