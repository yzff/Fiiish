package com.manyanger.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;




import com.manyanger.common.AppUtil;
import com.manyanger.fiiish.AppInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * @ClassName: ImageCacheNew
 * @Description:
 * @author fred.ma
 * @date 2014年6月23日 下午2:40:40
 */
public final class ImageCacheNew {

	/** 硬缓存 start **/
	private static final String CACHE_DEST_NAME = "image";
	protected String destCacheDir;
	/*
	 * cache time (Minutes)
	 */
	private long cacheSaveInMinutes;

	private boolean isDiskCacheEnabled=true;
	/**
	 * cache in user phone
	 */
	public static final int DISK_CACHE_UPHONE = 0;

	/**
	 * cache in user sdcard
	 */
	public static final int DISK_CACHE_SDCARD = 1;

	protected int whichDiskSave = DISK_CACHE_SDCARD;

	private LruCache<String, Bitmap> mMemoryCache;
	private volatile static ImageCacheNew instance = null;
	private final int MAX_MEMORY = 8 * 1024;// 最大内存值

	public static ImageCacheNew getInstance() {
		if (instance == null) {
			synchronized (ImageCacheNew.class) {
				instance = new ImageCacheNew();
			}
		}
		return instance;
	}

	private ImageCacheNew() {
		mMemoryCache = new LruCache<String, Bitmap>((int) Math.min(Runtime
				.getRuntime().maxMemory() /(4 * 1024), MAX_MEMORY)) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				if (android.os.Build.VERSION.SDK_INT < 12) {
					return (value.getRowBytes() * value.getHeight())/1024;
				}
				return value.getByteCount() / 1024;
			}
			
			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				onEntryRemoved(evicted, key, oldValue, newValue);
			}
		};
		buildTempImageCache();

	}


	protected void onEntryRemoved(boolean evicted, String key,
			Bitmap oldValue, Bitmap newValue) {
		// 如果是android3.0以下，则手动recycle。3.0以上的，bitmap在堆栈中，由虚拟机自动GC
		if(!hasHonetComb()){
			if (evicted) {
				if (oldValue != null && !oldValue.isRecycled()) {
					oldValue.recycle();
				}
			} else {
				Log.d("ImageCache",
						"will not recyle pic，action from remove or duplicate key");
			}
		} else {
				oldValue = null;

		}
	}

	private boolean hasHonetComb(){
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			return false;
		}
		return true;
	}
	
	public synchronized Bitmap put(String key, Bitmap value, byte[] data) {
		if (isDiskCacheEnabled) {
			cacheToDisk(key, value, data, 0, data.length);
		}
		return mMemoryCache.put(key, value);
	}

	public Bitmap putInMemery(String key, Bitmap value) {
		return mMemoryCache.put(key, value);
	}

	public synchronized void clearAll() {
		mMemoryCache.evictAll();
	}
	
	public synchronized Bitmap get(String key) {
		if (key == null) {
			return null;
		}
		Bitmap value = mMemoryCache.get(key);
		if (value != null) {
			return value;
		}

		File file = getFileForKey(key);
		if (file.exists()) {
			try {
				value = readValueFromDisk(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			if (value == null) {
				return null;
			}
			mMemoryCache.put(key, value);
			return value;
		}
		return null;
	}

	public void buildTempImageCache() {
		if (AppUtil.getPreferredDataDir() != null) {
			isDiskCacheEnable(AppInfo.getContext(), DISK_CACHE_SDCARD,
					CACHE_DEST_NAME);
		} else {
			isDiskCacheEnable(AppInfo.getContext(), DISK_CACHE_UPHONE,
					CACHE_DEST_NAME);
		}
	}

	/**
	 * Enable caching to the phone's internal storage or SD card.
	 * 
	 * @param context
	 * @param storageDevice
	 * @param destDir
	 *            both of the start and the end won't '/'
	 * @return
	 */
	public boolean isDiskCacheEnable(Context context, int storageDevice,
			String destDir) {
		return isDiskCacheEnable(context, storageDevice, destDir, false);
	}

	/**
	 * Enable caching to the phone's internal storage or SD card.
	 * 
	 * @param context
	 * @param storageDevice
	 * @param destDir
	 *            both of the start and the end won't '/'
	 * @param isSanitizeDisk
	 * 
	 * @return
	 */
	public boolean isDiskCacheEnable(Context context, int storageDevice,
			String destDir, boolean isSanitizeDisk) {
		whichDiskSave = storageDevice;
		StringBuffer destBuf = null;
		if (storageDevice == DISK_CACHE_SDCARD
				&& AppUtil.getPreferredDataDir() != null) {
			// sdcard available
			destBuf = new StringBuffer(Environment
					.getExternalStorageDirectory().getAbsolutePath());
			destBuf.append(AppInfo.PRE_PATH);
			destBuf.append("/cache");
		} else {
			File internalCacheDir = context.getCacheDir();
			if (internalCacheDir == null) {
				return isDiskCacheEnabled = false;
			}
			destBuf = new StringBuffer(internalCacheDir.getAbsolutePath());
			destBuf.append("/cache");
		}
		destBuf.append(File.separator);
		if (destDir != null && destDir.trim().length() > 0) {
			destBuf.append(destDir);
			destBuf.append(File.separator);
		}

		this.destCacheDir = destBuf.toString();
		File outFile = new File(destCacheDir);
		if (!(isDiskCacheEnabled = outFile.exists())) {
			isDiskCacheEnabled = outFile.mkdirs();
		}

		if (!isDiskCacheEnabled) {
			Log.e("ImageCacheNew", "Failed creating disk cache directory "
					+ destCacheDir);
		} else {
			// sanitize disk cache
			if (isSanitizeDisk) {
				sanitizeDiskCache();
			}
		}

		return isDiskCacheEnabled;
	}

	/*
	 * sanitize disk cache that is overdue
	 */
	public void sanitizeDiskCache() {
		File[] cachedFiles = new File(destCacheDir).listFiles();
		if (cachedFiles == null) {
			return;
		}
		for (File f : cachedFiles) {
			long lastModified = f.lastModified();
			Date now = new Date();
			long ageInMinutes = (now.getTime() - lastModified) / (1000 * 60);

			if (ageInMinutes >= cacheSaveInMinutes) {
				f.delete();
			}
		}
	}

	private void cacheToDisk(String key, Bitmap value, byte[] data, int offset,
			int length) {
		File file = getFileForKey(key);
		if (file.exists()) {
			return;
		}
		try {
			if (file.createNewFile()) {
				if (data != null && data.length > 0) {
					writeByteToDisk(file, data, offset, length);
				} else {
					writeValueToDisk(file, value);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFileForKey(String key) {
		File outFile = new File(destCacheDir);
		if (!(isDiskCacheEnabled = outFile.exists())) {
			isDiskCacheEnabled = outFile.mkdirs();
		}
		return new File(outFile, getFileNameForKey(key));
	}

	protected String getFileNameForKey(String key) {
		// return Constants.getFileNameFromUrl(key);
		return key;
	}

	protected void writeValueToDisk(File file, Bitmap bitmap) {
		if (bitmap != null && file != null) {
			BufferedOutputStream ostream = null;
			try {
				FileOutputStream fos = new FileOutputStream(file);
				ostream = new BufferedOutputStream(fos);
				if (!bitmap.compress(CompressFormat.PNG, 100, ostream)) {
					bitmap.compress(CompressFormat.JPEG, 100, ostream);
				}
				ostream.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				AppUtil.closeOutputStream(ostream);
			}
		}
	}

	protected void writeByteToDisk(File file, byte[] data, int offset,
			int length) {
		if (data == null || data.length == 0) {
			return;
		}
		/*
		 * check cache Disk is have enough storage
		 */
		switch (whichDiskSave) {
		case DISK_CACHE_SDCARD:
			if (!AppUtil.checkSdcardHavEnghStorage(data.length)) {
				return;
			}
			break;
		case DISK_CACHE_UPHONE:
			if (!AppUtil.checkPhoneHavEnghStorage(data.length)) {
				return;
			}
			break;
		default:
			return;
		}
		BufferedOutputStream ostream = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ostream = new BufferedOutputStream(fos);
			ostream.write(data, offset, length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			AppUtil.closeOutputStream(ostream);
		}
	}

	protected Bitmap readValueFromDisk(File file) throws IOException {
		FileInputStream ins = null;
		Bitmap temp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		try {

//			opts.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
//			if(opts.outWidth > 720 && AppInfo.getScreenWidth() < 720){
//				opts.inSampleSize=2;
//			}
//			
//			opts.inJustDecodeBounds = false;
//			opts.inPreferredConfig = Bitmap.Config.RGB_565;
//			opts.inPurgeable = true;
//			opts.inInputShareable = true;
//			
			ins = new FileInputStream(file);
//			temp = BitmapFactory.decodeStream(ins, null, opts);
			temp = BitmapFactory.decodeStream(ins, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			temp = null;
		} finally {
			AppUtil.closeInputStream(ins);
		}
		return temp;
	}
	
	public Bitmap remove(String key)
	{
	    return	mMemoryCache.remove(key);
	}
	public Bitmap getFromInMemery(String key)
	{
	    return	mMemoryCache.get(key);
	}
	
}
