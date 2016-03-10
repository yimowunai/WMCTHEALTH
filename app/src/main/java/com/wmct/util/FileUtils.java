package com.wmct.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class FileUtils {
	public static List<File> listPathFiles(String path) {
		List<File> list = new ArrayList<File>();
		File file = new File(path);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				list.add(f);
			}
		}
		return list;
	}
	public static String getAppCache(Context context,String dir){
		String savePath = context.getCacheDir().getAbsoluteFile()+File.separator+dir;
		File saveDir = new File(savePath);
		if(!saveDir.exists()){
			saveDir.mkdirs();
		}
		return savePath;
	}
}
