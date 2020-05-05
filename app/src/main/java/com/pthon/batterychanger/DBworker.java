package com.pthon.batterychanger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBworker {

    public static String get(Context context) {
        String li = "";
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();



        Cursor cursor = database.query(DBHelper.TABLE_LINK, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex));
                li = cursor.getString(nameIndex);
            } while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();

        return li;
    }

    public static String put(Context context, String service, String min, String max) {
        String url = service + "/" + min + "/" + max;
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        database.execSQL("DELETE FROM `link`");

        contentValues.put(DBHelper.KEY_NAME, url);
        database.insert(DBHelper.TABLE_LINK, null, contentValues);
        return null;
    }
}
