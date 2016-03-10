package com.wmct.net.Image;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.wmct.health.R;
import com.wmct.util.ImageResizer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ---------------------------------------
 * 版权 ：山大信息学院
 * <p>
 * 作者 ：三月半
 * <p>
 * 时间 ：2016/1/7 10:29
 * <p>
 * --------------------------------------
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private static final int MESSAGE_POST_RESULT = 1;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = CPU_COUNT * 2 + 1;
    private static final int TAG_KEY_URI = R.id.tag_frist;
    private static final int DISK_CACHE_SIZE = 1024*1024*50;
    private static final int IO_BUFFER_SIZE = 8*1024;
    private static final int DISK_CACHE_INDEX = 0;

    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndDecrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), mThreadFactory);

    private Context mContext;
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskCache;
    private ImageResizer mImageResizer = new ImageResizer();
    private boolean mIsDiskLruCacheCreated = false;


    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            imageView.setImageBitmap(result.bitmap);
            String url = (String) imageView.getTag(TAG_KEY_URI);
            if (url.equals(result.url)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.d(TAG, "set image bitmap,but url has changes.");
            }
        }
    };
    private int usableSpace;

    private ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void bindBitmap(String uri, ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    public void bindBitmap(final String url, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI, url);
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, url, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    private Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromMemCache,uri:" + uri);
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromDiskCache,uri:" + uri);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
            Log.d(TAG, "loadBitmapFromHttp,uri:" + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Log.w(TAG, "encounter error,DiskLruCache is not create.");
            bitmap = downLoadBitmapFromUul(uri);
        }
        return bitmap;
    }

    private Bitmap downLoadBitmapFromUul(String uri) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream inputStream = null;
        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(conn.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromHttp(String uri, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network in UI Thread.");
        }
        if (mDiskCache == null) {
            return null;
        }

        String key = hashKeyFromUrl(uri);
        DiskLruCache.Editor editor = mDiskCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downLoadUrlToStream(uri, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskCache.flush();
        }

        return loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
    }

    private boolean downLoadUrlToStream(String uri, OutputStream outputStream) {
        HttpURLConnection conn = null;
        BufferedInputStream inputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(conn.getInputStream());
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            int lenght = 0;
            while ((lenght = inputStream.read()) != -1) {
                bufferedOutputStream.write(lenght);
            }
            inputStream.close();
            bufferedOutputStream.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private Bitmap loadBitmapFromDiskCache(String uri, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI Thread,it's not recommended");
        }
        if (mDiskCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFromUrl(uri);
        DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = mImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromMemCache(String uri) {
        String key = hashKeyFromUrl(uri);
        Bitmap bitmap = mMemoryCache.get(key);
        return bitmap;
    }

    private String hashKeyFromUrl(String uri) {
        String cacheKey = "";
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(uri.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(0xFF & bytes[i]);
            if (hexString.length() < 2) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        return sb.toString();
    }


    private File getDiskCacheDir(Context mContext, String bitmap) {
        boolean externalStorageAvalilable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath = "";
        if (externalStorageAvalilable) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath, bitmap);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        StatFs statFs = new StatFs(path.getPath());

        return (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();
    }

    private class LoaderResult {
        private ImageView imageView;
        private String url;
        private Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.url = uri;
            this.bitmap = bitmap;
        }
    }
}
