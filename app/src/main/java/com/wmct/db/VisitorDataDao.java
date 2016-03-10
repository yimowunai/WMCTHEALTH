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
public class VisitorDataDao {

    private DataBaseHelper dbHelper;

    public VisitorDataDao(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void insert(MeasureData visitordata) {  //添加visitordata游客测量数据
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("terminalid", visitordata.getTerminalid());
        values.put("diastolicpressure", visitordata.getDiastolicpressure());
        values.put("systolicpressure", visitordata.getSystolicpressure());
        values.put("heartrate", visitordata.getHeartrate());
        values.put("heartstate", visitordata.getHeartstate());
        values.put("measuretime", visitordata.getMeasuretime());
        database.insert("visitordata", null, values);
        database.close();
    }

    public void delete(MeasureData visitordata) {  //删除visitordata，在上传服务器后删除
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(visitordata.getId())};
        database.delete("visitordata", "id = ?", whereArgs);
        database.close();
    }

    public List<MeasureData> queryAll() {  //查询visitordata中的所有数据，返回list
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query("visitordata", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<MeasureData> visitordataList = new ArrayList<>();
            while (cursor.moveToNext()) {
                long id = cursor.getInt(cursor.getColumnIndex("id"));
                String terminalid = cursor.getString(cursor.getColumnIndex("terminalid"));
                int diastolicpressure = cursor.getInt(cursor.getColumnIndex("diastolicpressure"));
                int systolicpressure = cursor.getInt(cursor.getColumnIndex("systolicpressure"));
                int heartrate = cursor.getInt(cursor.getColumnIndex("heartrate"));
                int heartstate = cursor.getInt(cursor.getColumnIndex("heartstate"));
                int measuretime = cursor.getInt(cursor.getColumnIndex("measuretime"));

                MeasureData qallvisitordata = new MeasureData(id, terminalid, diastolicpressure, systolicpressure,
                        heartrate, heartstate, measuretime);
                visitordataList.add(qallvisitordata);
            }
            cursor.close();
            database.close();
            return visitordataList;
        }
        cursor.close();
        database.close();
        return null;
    }

    public void deleteAll() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("visitordata", null, null);
        database.close();
    }
}
