package com.wmct.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wmct.bean.MeasureData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little grey on 2016/1/15.
 */
public class NotUploadMeasureDataDao {
    private DataBaseHelper dbHelper;

    public NotUploadMeasureDataDao(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void insert(MeasureData not_uploaded) {  //添加not_uploaded未上传的测量数据
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("memberid", not_uploaded.getMemberId());
        values.put("diastolicpressure", not_uploaded.getDiastolicpressure());
        values.put("systolicpressure", not_uploaded.getSystolicpressure());
        values.put("heartrate", not_uploaded.getHeartrate());
        values.put("heartstate", not_uploaded.getHeartstate());
        values.put("measuretime", not_uploaded.getMeasuretime());
        database.insert("not_uploaded", null, values);
        database.close();
    }

    public void delete(MeasureData not_uploaded) {  //删除not_uploaded，在上传服务器后删除
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(not_uploaded.getId())};
        database.delete("not_uploaded", "id = ?", whereArgs);
        database.close();
    }

    public List<MeasureData> queryAll() {  //查询not_uploaded所有未上传的数据，返回list
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query("not_uploaded", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<MeasureData> not_uploadedList = new ArrayList<>();
            while (cursor.moveToNext()) {
                long id = cursor.getInt(cursor.getColumnIndex("id"));
                long memberid = cursor.getInt(cursor.getColumnIndex("memberid"));
                int diastolicpressure = cursor.getInt(cursor.getColumnIndex("diastolicpressure"));
                int systolicpressure = cursor.getInt(cursor.getColumnIndex("systolicpressure"));
                int heartrate = cursor.getInt(cursor.getColumnIndex("heartrate"));
                int heartstate = cursor.getInt(cursor.getColumnIndex("heartstate"));
                int measuretime = cursor.getInt(cursor.getColumnIndex("measuretime"));

                MeasureData qallnot_uploaded = new MeasureData(id, memberid, diastolicpressure, systolicpressure,
                        heartrate, heartstate, measuretime);
                not_uploadedList.add(qallnot_uploaded);
            }
            cursor.close();
            database.close();
            return not_uploadedList;
        }
        cursor.close();
        database.close();
        return null;
    }
    public void deleteAll(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("not_uploaded", null, null);
        database.close();
    }
}
