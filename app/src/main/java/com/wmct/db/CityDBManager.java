package com.wmct.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.wmct.health.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class CityDBManager {
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "city_cn.s3db";
    public static final String PACKAGE_NAME = "com.wmct.health";
    //"cc.lifelink.cn"
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;
    private SQLiteDatabase database;
    private Context context;
    private File file = null;

    public CityDBManager(Context context) {
        Log.e("cc", "CityDBManager");
        this.context = context;
    }

    public void openDatabase() {
        Log.e("cc", "openDatabase()");
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
        Log.i("path", DB_PATH + "/" + DB_NAME);
    }

    public SQLiteDatabase getDatabase() {
        Log.e("cc", "getDatabase()");
        return this.database;
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            Log.e("cc", "open and return");
            file = new File(dbfile);
            if (!file.exists()) {
                Log.e("cc", "file");
                InputStream is = context.getResources().openRawResource(R.raw.city);
                if (is != null) {
                    Log.e("cc", "is null");
                } else {
                }
                FileOutputStream fos = new FileOutputStream(dbfile);
                if (is != null) {
                    Log.e("cc", "fosnull");
                } else {
                }
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    Log.e("cc", "while");
                    fos.flush();
                }
                fos.close();
                is.close();
            }
            database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return database;
        } catch (FileNotFoundException e) {
            Log.e("cc", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("cc", "IO exception");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("cc", "exception " + e.toString());
        }
        return null;
    }

    public void closeDatabase() {
        Log.e("cc", "closeDatabase()");
        if (this.database != null)
            this.database.close();
    }
}