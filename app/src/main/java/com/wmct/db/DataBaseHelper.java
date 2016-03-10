package com.wmct.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by little grey on 2016/1/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "DataBaseHelper";
    public static final String WMCT_HEALTH_DATABASE_NAME = "wmct_health.db";

    public static final String WMCT_HEALTH_FAMILY_TABLE_NAME = "family";
    public static final String WMCT_HEALTH_MEMBER_TABLE_NAME = "member";
    public static final String WMCT_HEALTH_NOT_UPLOAD_MEASUREDATA_TABLE_NAME = "not_uploaded";
    public static final String WMCT_HEALTH_MEASUREDATA_TABLE_NAME = "measuredata";
    public static final String WMCT_HEALTH_VISITORDATA_TABLE_NAME = "visitordata";

    public static final String CREATE_FAMILY_TABLE = "create table "
            + WMCT_HEALTH_FAMILY_TABLE_NAME
            + " (id integer primary key autoincrement,"
            + "name text,"
            + "pwd text,"
            + "familyPhone text,"
            + "address text,"
            + "terminalid text)";
    public static final String CREATE_MEMBER_TABLE = "create table "
            + WMCT_HEALTH_MEMBER_TABLE_NAME
            + " (id integer primary key autoincrement,"
            + "memberid integer,"
            + "familyPhone text,"
            + "membername  text,"
            + "age integer,"
            + "gender integer,"
            + "image text)";
    public static final String CREATE_MEASUREDATA_TABLE = "create table "
            + WMCT_HEALTH_MEASUREDATA_TABLE_NAME
            + " (id integer primary key autoincrement,"
            + "memberid integer,"
            + "diastolicpressure integer,"
            + "systolicpressure integer,"
            + "heartrate integer,"
            + "heartstate integer,"
            + "measuretime text)";
    public static final String CREATE_NOT_UPLOADED_TABLE = "create table "
            + WMCT_HEALTH_NOT_UPLOAD_MEASUREDATA_TABLE_NAME
            + " (id integer primary key autoincrement,"
            + "memberid integer,"
            + "diastolicpressure integer,"
            + "systolicpressure integer,"
            + "heartrate integer,"
            + "heartstate integer,"
            + "measuretime text)";
    public static final String CREATE_VISITORDATA_TABLE = "create table "
            + WMCT_HEALTH_VISITORDATA_TABLE_NAME
            + " (id integer primary key autoincrement,"
            + "terminalid text,"
            + "diastolicpressure integer,"
            + "systolicpressure integer,"
            + "heartrate integer,"
            + "heartstate integer,"
            + "measuretime text)";


    public DataBaseHelper(Context context) {
        super(context, WMCT_HEALTH_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAMILY_TABLE);
        db.execSQL(CREATE_MEMBER_TABLE);
        db.execSQL(CREATE_NOT_UPLOADED_TABLE);
        db.execSQL(CREATE_MEASUREDATA_TABLE);
        db.execSQL(CREATE_VISITORDATA_TABLE);
        Log.d(TAG, "DataBaseHelper create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + WMCT_HEALTH_FAMILY_TABLE_NAME);
        db.execSQL("drop table if exists " + WMCT_HEALTH_MEMBER_TABLE_NAME);
        db.execSQL("drop table if exists " + WMCT_HEALTH_MEASUREDATA_TABLE_NAME);
        db.execSQL("drop table if exists " + WMCT_HEALTH_VISITORDATA_TABLE_NAME);
        db.execSQL("drop table if exists " + WMCT_HEALTH_NOT_UPLOAD_MEASUREDATA_TABLE_NAME);
        onCreate(db);
    }
}
