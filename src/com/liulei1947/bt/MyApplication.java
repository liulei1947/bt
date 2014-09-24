package com.liulei1947.bt;

import java.io.File;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import cn.bmob.v3.BmobUser;

import com.liulei1947.bt.entity.QiangYu;
import com.liulei1947.bt.entity.SeePic;
import com.liulei1947.bt.entity.User;
import com.liulei1947.bt.utils.ActivityManagerUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApplication extends Application{

	public static String TAG;
	
	private static MyApplication myApplication = null;
	
	private QiangYu currentQiangYu = null;
	private SeePic currentSeePic = null;
	
	public static MyApplication getInstance(){
		
		return myApplication;
	}
	public User getCurrentUser() {
		User user = BmobUser.getCurrentUser(myApplication, User.class);
		if(user!=null){
			return user;
		}
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		TAG = this.getClass().getSimpleName();
		//由于Application类本身已经单例，所以直接按以下处理即可。
		myApplication = this;
		
		initImageLoader();
	}

	
	
	public QiangYu getCurrentQiangYu() {
		return currentQiangYu;
	}
	
	public void setCurrentQiangYu(QiangYu currentQiangYu) {
		this.currentQiangYu = currentQiangYu;
	}
	public SeePic getCurrentSeePic() {
		return currentSeePic;
	}
	public void setCurrentSeePic(SeePic currentSeePic) {
		this.currentSeePic = currentSeePic;
	}
	public void addActivity(Activity ac){
		ActivityManagerUtils.getInstance().addActivity(ac);
	}
	
	public void exit(){
		ActivityManagerUtils.getInstance().removeAllActivity();
	}
	
	public Activity getTopActivity(){
		return ActivityManagerUtils.getInstance().getTopActivity();
	}
	
	/**
	 * 初始化imageLoader
	 */
	public void initImageLoader(){
		File cacheDir = StorageUtils.getOwnCacheDirectory(this,
				"BTFile/Cache");
		File cacheDir2 = StorageUtils.getOwnCacheDirectory(this,
				"BTFile/uTorrentFile");
		//File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
										.threadPriority(Thread.NORM_PRIORITY - 2)
									
										.memoryCache(new LruMemoryCache(5*1024*1024))
										.memoryCacheSize(10*1024*1024)
										.discCache(new UnlimitedDiscCache(cacheDir))
										.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
										.build();
		ImageLoader.getInstance().init(config);
	}
	
	public DisplayImageOptions getOptions(int drawableId){
		return new DisplayImageOptions.Builder()
		.showImageOnLoading(drawableId)
		.showImageForEmptyUri(R.drawable.bg_pic_error)
		.showImageOnFail(R.drawable.bg_pic_error)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new FadeInBitmapDisplayer(100))
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		


	}
}
