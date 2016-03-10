package com.wmct.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wmct.bean.MeasureData;
import com.wmct.bean.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little grey on 2016/1/15.
 */
public class MeasureDataDao {
    private DataBaseHelper dbHelper;

    public MeasureDataDao(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void insert(MeasureData measuredata) {  //添加measuredata测量数据
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("memberid", measuredata.getMemberId());
        values.put("diastolicpressure", measuredata.getDiastolicpressure());
        values.put("systolicpressure", measuredata.getSystolicpressure());
        values.put("heartrate", measuredata.getHeartrate());
        values.put("heartstate", measuredata.getHeartstate());
        values.put("measuretime", measuredata.getMeasuretime());
        database.insert("measuredata", null, values);
        database.close();
    }

    public void batchInsert(List<MeasureData> list) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                MeasureData measureData = list.get(i);
                ContentValues values = new ContentValues();
                values.put("memberid", measureData.getMemberId());
                values.put("diastolicpressure", measureData.getDiastolicpressure());
                values.put("systolicpressure", measureData.getSystolicpressure());
                values.put("heartrate", measureData.getHeartrate());
                values.put("heartstate", measureData.getHeartstate());
                values.put("measuretime", measureData.getMeasuretime());
                database.insert("measuredata", null, values);
            }
        }
        database.close();
    }

    public void delete(MeasureData measuredata) {   //删除不想要的measuredata,未上传的数据上传成功后调用
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(measuredata.getId())};
        database.delete("measuredata", "id = ?", whereArgs);
        database.close();
    }

    public List<MeasureData> queryAll(Member member) {  //查询某一member的所有measuredata
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] whereArgs = {String.valueOf(member.getMemberid())};
        Cursor cursor = database.query("measuredata", null, "memberid = ?", whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<MeasureData> measuredataList = new ArrayList<>();
            while (cursor.moveToNext()) {
                long id = cursor.getInt(cursor.getColumnIndex("id"));
                long member_id = cursor.getInt(cursor.getColumnIndex("memberid"));
                int diastolicpressure = cursor.getInt(cursor.getColumnIndex("diastolicpressure"));
                int systolicpressure = cursor.getInt(cursor.getColumnIndex("systolicpressure"));
                int heartrate = cursor.getInt(cursor.getColumnIndex("heartrate"));
                int heartstate = cursor.getInt(cursor.getColumnIndex("heartstate"));
                int measuretime = cursor.getInt(cursor.getColumnIndex("measuretime"));

                MeasureData qallmeasuredata = new MeasureData(id, member_id, diastolicpressure, systolicpressure,
                        heartrate, heartstate, measuretime);
                measuredataList.add(qallmeasuredata);
            }
            cursor.close();
            database.close();
            return measuredataList;
        }
        cursor.close();
        database.close();
        return null;
    }

    public List<MeasureData> query10(Member member) {  //查询某一member的最近10条measuredata用来画图
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] whereArgs = {String.valueOf(member.getMemberid())};
        Cursor cursor = database.query("measuredata", null, "memberid = ?", whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            List<MeasureData> list10 = new ArrayList<>();
            int i;
            for (i = 0; i < 10; i++) {
                long id = cursor.getInt(cursor.getColumnIndex("id"));
                long member_id = cursor.getInt(cursor.getColumnIndex("memberid"));
                int diastolicpressure = cursor.getInt(cursor.getColumnIndex("diastolicpressure"));
                int systolicpressure = cursor.getInt(cursor.getColumnIndex("systolicpressure"));
                int heartrate = cursor.getInt(cursor.getColumnIndex("heartrate"));
                int heartstate = cursor.getInt(cursor.getColumnIndex("heartstate"));
                int measuretime = cursor.getInt(cursor.getColumnIndex("measuretime"));
                MeasureData qmeasuredata = new MeasureData(id, member_id, diastolicpressure, systolicpressure,
                        heartrate, heartstate, measuretime);
                list10.add(qmeasuredata);
                if (cursor.moveToPrevious()) {

                } else {
                    cursor.close();
                    break;
                }
            }
            cursor.close();
            database.close();
            return list10;
        }
        cursor.close();
        database.close();
        return null;
    }

    public MeasureData querylast(Member member) {  //查询某一member上次的measuredata，用来显示上次测量结果
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] whereArgs = {String.valueOf(member.getMemberid())};
        Cursor cursor = database.query("measuredata", null, "memberid = ?", whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            long id = cursor.getInt(cursor.getColumnIndex("id"));
            long member_id = cursor.getInt(cursor.getColumnIndex("memberid"));
            int diastolicpressure = cursor.getInt(cursor.getColumnIndex("diastolicpressure"));
            int systolicpressure = cursor.getInt(cursor.getColumnIndex("systolicpressure"));
            int heartrate = cursor.getInt(cursor.getColumnIndex("heartrate"));
            int heartstate = cursor.getInt(cursor.getColumnIndex("heartstate"));
            int measuretime = cursor.getInt(cursor.getColumnIndex("measuretime"));

            MeasureData qmeasuredata = new MeasureData(id, member_id, diastolicpressure, systolicpressure,
                    heartrate, heartstate, measuretime);
            cursor.close();
            database.close();
            return qmeasuredata;
        }
        cursor.close();
        database.close();
        return null;
    }

    public void deleteAll() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("measuredata", null, null);
        database.close();
    }
}
