package com.wmct.health;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p>
 * 作者 ：三月半
 * <p>
 * 时间 ：2016/1/11 16:28
 * <p>
 * --------------------------------------
 */
public class AppConfig {
    private static final String TAG = "AppConfig";
    public static final String CONF_APP_UNIQUEID = "APP_UNIQUEID";
    private final static String APP_CONFIG = "config";
    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }


    public String get(String key) {
        Properties properties = getProperties();
        return (properties != null) ? properties.getProperty(key) : null;
    }

    public void set(String key, String value) {
        Properties properties = getProperties();
        properties.setProperty(key, value);
        setProperties(properties);
    }

    public void set(Properties property) {
        Properties properties = getProperties();
        properties.putAll(property);
        setProperties(properties);
    }

    public void remove(String... key) {
        Properties properties = getProperties();
        for (String str : key) {
            properties.remove(str);
        }
        setProperties(properties);
    }

    private void setProperties(Properties properties) {
        FileOutputStream outputStream = null;
        File dirConfig = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
        try {
            outputStream = new FileOutputStream(dirConfig.getPath() + File.separator + APP_CONFIG);
            properties.store(outputStream, null);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Properties getProperties() {
        FileInputStream inputStream = null;
        Properties properties = new Properties();
        File dirConfig = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
        File config = new File(dirConfig.getPath() + File.separator + APP_CONFIG);
        if(!config.exists()){
            try {
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            inputStream = new FileInputStream(dirConfig.getPath() + File.separator + APP_CONFIG);
            properties.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
}
