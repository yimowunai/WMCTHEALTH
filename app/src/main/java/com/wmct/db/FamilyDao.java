package com.wmct.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wmct.bean.Family;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little grey on 2016/1/15.
 */
public class FamilyDao {
    private DataBaseHelper dbHelper;

    public FamilyDao(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void insert(Family family) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", family.getName());
        values.put("pwd", family.getPwd());
        values.put("familyPhone", family.getPhone());
        values.put("address", family.getAddress());
        values.put("terminalid", family.getTerminalid());
        database.insert("family", null, values);
        database.close();
    }

    public void delete(Family family) {  //删除多余的family
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(family.getPhone())};
        database.delete("family", "familyPhone = ?", whereArgs);
        database.close();
    }

    public void update(Family family) {  //修改family的信息
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", family.getName());
        values.put("pwd", family.getPwd());
        values.put("address", family.getAddress());
        values.put("terminalid", family.getTerminalid());
        String[] whereArgs = {family.getPhone()};
        database.update("family", values, "familyPhone = ?", whereArgs);
        database.close();
    }

    public Family query(Family family) {  //查询单个family的信息
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] whereArgs = {family.getPhone()};
        Cursor cursor = database.query("family", null, "familyPhone = ?", whereArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            String familyPhone = cursor.getString(cursor.getColumnIndex("familyPhone"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String terminalid = cursor.getString(cursor.getColumnIndex("terminalid"));
            database.close();
            Family qfamily = new Family(id, name, pwd, familyPhone, address, terminalid);
            return qfamily;
        }
        cursor.close();
        database.close();
        return null;
    }

    public List<Family> queryAll() {  //查询所有family的信息，返回List
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query("family", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<Family> familyList = new ArrayList<>();
            while (cursor.moveToNext()) {
                long id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                String familyPhone = cursor.getString(cursor.getColumnIndex("familyPhone"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String terminalid = cursor.getString(cursor.getColumnIndex("terminalid"));

                Family qallfamily = new Family(id, name, pwd, familyPhone, address, terminalid);
                familyList.add(qallfamily);
            }
            cursor.close();
            database.close();
            return familyList;
        }
        cursor.close();
        database.close();
        return null;
    }
}
