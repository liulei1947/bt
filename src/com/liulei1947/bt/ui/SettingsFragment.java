package com.liulei1947.bt.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import astuetz.viewpager.extensions.sample.help.HelpFragmentActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.liulei1947.bt.MyApplication;
import com.liulei1947.bt.R;
import com.liulei1947.bt.base.BaseHomeFragment;
import com.liulei1947.bt.entity.User;
import com.liulei1947.bt.ui.PersonalFragment.IProgressControllor;
import com.liulei1947.bt.utils.ActivityUtil;
import com.liulei1947.bt.utils.CacheUtils;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SettingsFragment extends BaseHomeFragment implements OnClickListener,OnCheckedChangeListener{

	TextView logout;
	RelativeLayout update ;
	RelativeLayout help ;
	LinearLayout cleanCache;
	CheckBox pushSwitch;
	CheckBox sexSwitch;
	TextView CacheFile;
	RelativeLayout iconLayout;
	ImageView userIcon;
	
	RelativeLayout nickLayout;
	TextView nickName;
	
	RelativeLayout signLayout;
	TextView signature;
	
	IProgressControllor mIProgressControllor;
	
	static final int UPDATE_SEX = 11;
	static final int UPDATE_ICON = 12;
	static final int GO_LOGIN = 13;
	static final int UPDATE_SIGN = 14;
	static final int EDIT_SIGN = 15;
	
	public static SettingsFragment newInstance(){
		SettingsFragment fragment = new SettingsFragment();
		return fragment;
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_settings;
	}

	@Override
	protected void findViews(View view) {
		// TODO Auto-generated method stub
		logout = (TextView)view.findViewById(R.id.user_logout);
		update = (RelativeLayout)view.findViewById(R.id.settings_update);
		help = (RelativeLayout)view.findViewById(R.id.settings_help);
		cleanCache = (LinearLayout)view.findViewById(R.id.settings_cache);
		CacheFile = (TextView)view.findViewById(R.id.settings_cache_file);
		pushSwitch = (CheckBox)view.findViewById(R.id.settings_push_switch);
		sexSwitch = (CheckBox)view.findViewById(R.id.sex_choice_switch);
		
		iconLayout = (RelativeLayout)view.findViewById(R.id.user_icon);
		userIcon = (ImageView)view.findViewById(R.id.user_icon_image);
		
		nickLayout = (RelativeLayout)view.findViewById(R.id.user_nick);
		nickName = (TextView)view.findViewById(R.id.user_nick_text);
		
		signLayout = (RelativeLayout)view.findViewById(R.id.user_sign);
		signature = (TextView)view.findViewById(R.id.user_sign_text);
		//*************************
		File file = new File(Environment.getExternalStorageDirectory().getPath()+"/BTFile");
		FileSizeUtil filecount=new FileSizeUtil();
		double filesize = filecount.getFileOrFilesSize(file,3);
		CacheFile.setText("文件目录："+Environment.getExternalStorageDirectory().getPath()+"/BTFile"+"   大小"+filesize+"MB");
		
	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		initPersonalInfo();
	}

	private void initPersonalInfo(){
		User user = BmobUser.getCurrentUser(mContext,User.class);
		if(user != null){
			nickName.setText(user.getUsername());
			signature.setText(user.getSignature());
			if(user.getSex().equals(Constant.SEX_FEMALE)){
				sexSwitch.setChecked(true);
				sputil.setValue("sex_settings", 0);
			}else{
				sexSwitch.setChecked(false);
				sputil.setValue("sex_settings", 1);
			}
			BmobFile avatarFile = user.getAvatar();
			if(null != avatarFile){
				ImageLoader.getInstance()
				.displayImage(avatarFile.getFileUrl(), userIcon, 
						MyApplication.getInstance().getOptions(R.drawable.user_icon_default_main),
						new SimpleImageLoadingListener(){

							@Override
							public void onLoadingComplete(String imageUri, View view,
									Bitmap loadedImage) {
								// TODO Auto-generated method stub
								super.onLoadingComplete(imageUri, view, loadedImage);
							}
					
				});
			}
			logout.setText("退出登录");
		}else{
			logout.setText("登录");
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			mIProgressControllor = (IProgressControllor)activity;
		}catch(ClassCastException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断用户是否登录
	 * @return
	 */
	private boolean isLogined(){
		BmobUser user = BmobUser.getCurrentUser(mContext, User.class);
		if(user != null){
			return true;
		}
		return false;
	}
	
	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		logout.setOnClickListener(this);
		update.setOnClickListener(this);
		help.setOnClickListener(this);
		cleanCache.setOnClickListener(this);
		pushSwitch.setOnCheckedChangeListener(this);
		sexSwitch.setOnCheckedChangeListener(this);
		
		iconLayout.setOnClickListener(this);
		nickLayout.setOnClickListener(this);
		signLayout.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_logout:
			if(isLogined()){
				BmobUser.logOut(mContext);
				ActivityUtil.show(getActivity(), "登出成功。");
			}else{
				redictToLogin(GO_LOGIN);
			}
			
			
			break;
		
		case R.id.settings_update:
			Toast.makeText(mContext, "正在检查。。。", Toast.LENGTH_SHORT).show();
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					// TODO Auto-generated method stub
					switch (updateStatus) {
			        case UpdateStatus.Yes: // has update
			            UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
			            break;
			        case UpdateStatus.No: // has no update
			            Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.NoneWifi: // none wifi
			            Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.Timeout: // time out
			            Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
			            break;
			        }
				}
			});
			UmengUpdateAgent.forceUpdate(mContext);
			break;
		case R.id.settings_help:
			redictToActivity(mContext, HelpFragmentActivity.class, null);
			break;
		case R.id.settings_cache:
			ImageLoader.getInstance().clearDiscCache();
			ActivityUtil.show(getActivity(), "清除图片缓存和种子文件完毕");
			File file = new File(Environment.getExternalStorageDirectory().getPath()+"/BTFile");
			FileSizeUtil filecount=new FileSizeUtil();
			double filesize = filecount.getFileOrFilesSize(file,3);
			filecount.deleteFile(file);
			CacheFile.setText("文件目录："+Environment.getExternalStorageDirectory().getPath()+"/BTFile"+"   大小"+filesize+"MB");
			break;
			
		case R.id.user_icon:
			if(isLogined()){
				showAlbumDialog();
			}else{
				redictToLogin(UPDATE_ICON);
			}
			break;
		case R.id.user_nick:
			//无需设置
			break;
		case R.id.user_sign:
			if(isLogined()){
				Intent intent = new Intent();
				intent.setClass(mContext, EditSignActivity.class);
				startActivityForResult(intent, EDIT_SIGN);
			}else{
				redictToLogin(UPDATE_SIGN);
			}
			break;
		default:
			break;
		}
	}
	
	String dateTime;
	AlertDialog albumDialog;
	public void showAlbumDialog(){
		albumDialog = new AlertDialog.Builder(mContext).create();
		albumDialog.setCanceledOnTouchOutside(true);
		View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_usericon, null);
		albumDialog.show();
		albumDialog.setContentView(v);
		albumDialog.getWindow().setGravity(Gravity.CENTER);
		
		
		TextView albumPic = (TextView)v.findViewById(R.id.album_pic);
		TextView cameraPic = (TextView)v.findViewById(R.id.camera_pic);
		albumPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				albumDialog.dismiss();
				Date date1 = new Date(System.currentTimeMillis());
				dateTime = date1.getTime() + "";
				getAvataFromAlbum();
			}
		});
		cameraPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				albumDialog.dismiss();
				Date date = new Date(System.currentTimeMillis());
				dateTime = date.getTime() + "";
				getAvataFromCamera();
			}
		});
	}
	

	
	private void getAvataFromCamera(){
		File f = new File(CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri uri = Uri.fromFile(f);
		Log.e("uri", uri + "");
		
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, 1);
	}
	
	private void getAvataFromAlbum(){
		Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		intent2.setType("image/*");
		startActivityForResult(intent2, 2);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.settings_push_switch:
			if(isChecked){
				//接受推送，储存值
				sputil.setValue("isPushOn", true);
				PushAgent mPushAgent = PushAgent.getInstance(mContext);
				mPushAgent.enable();
			}else{
				//关闭推送，储存值
				sputil.setValue("isPushOn", false);
				PushAgent mPushAgent = PushAgent.getInstance(mContext);
				mPushAgent.disable();
			}
			break;
		case R.id.sex_choice_switch:
			if(isChecked){
				sputil.setValue("sex_settings", 0);
				updateSex(0);
			}else{
				sputil.setValue("sex_settings", 1);
				updateSex(1);
			}
			break;
		default:
			break;
		}
			
	}
	
	private void updateSex(int sex){
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user!=null){
			if(sex == 0){
				user.setSex(Constant.SEX_FEMALE);
			}else{
				user.setSex(Constant.SEX_MALE);
			}
			if(mIProgressControllor!=null){
				mIProgressControllor.showActionBarProgress();
			}
			user.update(mContext, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					if(mIProgressControllor!=null){
						mIProgressControllor.hideActionBarProgress();
					}
					ActivityUtil.show(getActivity(),"更新信息成功。");
					LogUtils.i(TAG,"更新信息成功。");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					if(mIProgressControllor!=null){
						mIProgressControllor.hideActionBarProgress();
					}
					ActivityUtil.show(getActivity(), "更新信息失败。请检查网络~");
					LogUtils.i(TAG,"更新失败1-->"+arg1);
				}
			});
		}else{
			redictToLogin(UPDATE_SEX);
		}
		
	}

	private void redictToLogin(int requestCode){
		Intent intent = new Intent();
		intent.setClass(getActivity(), RegisterAndLoginActivity.class);
		startActivityForResult(intent, requestCode);
		ActivityUtil.show(mContext, "请先登录。");
	}
	
	String iconUrl;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			case UPDATE_SEX:
				initPersonalInfo();
				break;
			case UPDATE_ICON:
				initPersonalInfo();
				iconLayout.performClick();
				break;
			case UPDATE_SIGN:
				initPersonalInfo();
				signLayout.performClick();
				break;
			case EDIT_SIGN:
				initPersonalInfo();
				break;
			case 1:
				String files =CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime;
				File file = new File(files);
				if(file.exists()&&file.length() > 0){
					Uri uri = Uri.fromFile(file);
					startPhotoZoom(uri);
				}else{
					
				}
				break;
			case 2:
				if (data == null) {
					return;
				}
				startPhotoZoom(data.getData());
				break;
			case 3:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bitmap = extras.getParcelable("data");
						// 锟斤拷锟斤拷图片
						iconUrl = saveToSdCard(bitmap);
						userIcon.setImageBitmap(bitmap);
						updateIcon(iconUrl);
					}
				}
				break;
			case GO_LOGIN:
				initPersonalInfo();
				logout.setText("退出登录");
				break;
			default:
				break;
			}
		}
	}
	
	private void updateIcon(String avataPath){
		if(avataPath!=null){
			final BmobFile file = new BmobFile(new File(avataPath));
			if(mIProgressControllor!=null){
				mIProgressControllor.showActionBarProgress();
			}
			file.uploadblock(mContext, new UploadFileListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					if(mIProgressControllor!=null){
						mIProgressControllor.hideActionBarProgress();
					}
					LogUtils.i(TAG, "上传文件成功。"+file.getFileUrl());
					User currentUser = BmobUser.getCurrentUser(mContext, User.class);
					currentUser.setAvatar(file);
					if(mIProgressControllor!=null){
						mIProgressControllor.showActionBarProgress();
					}
					currentUser.update(mContext, new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							if(mIProgressControllor!=null){
								mIProgressControllor.hideActionBarProgress();
							}
							ActivityUtil.show(getActivity(), "更改头像成功。");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							if(mIProgressControllor!=null){
								mIProgressControllor.hideActionBarProgress();
							}
							ActivityUtil.show(getActivity(), "更新头像失败。请检查网络~");
							LogUtils.i(TAG,"更新失败2-->"+arg1);
						}
					});
				}

				@Override
				public void onProgress(Integer arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					if(mIProgressControllor!=null){
						mIProgressControllor.hideActionBarProgress();
					}
					ActivityUtil.show(getActivity(), "上传头像失败。请检查网络~");
					LogUtils.i(TAG, "上传文件失败。"+arg1);
				}
			});
		}
	}
	
	
	
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 锟斤拷锟斤拷锟斤拷锟絚rop=true锟斤拷锟斤拷锟斤拷锟节匡拷锟斤拷锟斤拷Intent锟斤拷锟斤拷锟斤拷锟斤拷示锟斤拷VIEW锟缴裁硷拷
		// aspectX aspectY 锟角匡拷叩谋锟斤拷锟�
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 锟角裁硷拷图片锟斤拷锟�
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去锟斤拷锟节憋拷
		intent.putExtra("scaleUpIfNeeded", true);// 去锟斤拷锟节憋拷
		// intent.putExtra("noFaceDetection", true);//锟斤拷锟斤拷识锟斤拷
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);

	}
	
	public String saveToSdCard(Bitmap bitmap){
		String files =CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime+"_12";
		File file=new File(files);
        try {
            FileOutputStream out=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LogUtils.i(TAG, file.getAbsolutePath());
        return file.getAbsolutePath();
	}
	/**
	 * Activity跳转
	 * @param context
	 * @param targetActivity
	 * @param bundle
	 */
	public void redictToActivity(Context context,Class<?> targetActivity,Bundle bundle){
		Intent intent = new Intent(context, targetActivity);
		if(null != bundle){
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	//************************************************************
	public class FileSizeUtil {
		public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
		public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
		public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
		public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
		/**
		* 获取文件指定文件的指定单位的大小
		* @param filePath 文件路径
		* @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
		* @return double值的大小
		*/
		public double getFileOrFilesSize(File filePath,int sizeType){
		//File file=new File(filePath);
	    File file=filePath;
		long blockSize=0;
		try {
		if(file.isDirectory()){
		blockSize = getFileSizes(file);
		}else{
		blockSize = getFileSize(file);
		}
		} catch (Exception e) {
		e.printStackTrace();
		Log.e("获取文件大小","获取失败!");
		}
		return FormetFileSize(blockSize, sizeType);
		}
		/**
		* 调用此方法自动计算指定文件或指定文件夹的大小
		* @param filePath 文件路径
		* @return 计算好的带B、KB、MB、GB的字符串
		*/
		public String getAutoFileOrFilesSize(String filePath){
		File file=new File(filePath);
		long blockSize=0;
		try {
		if(file.isDirectory()){
		blockSize = getFileSizes(file);
		}else{
		blockSize = getFileSize(file);
		}
		} catch (Exception e) {
		e.printStackTrace();
		Log.e("获取文件大小","获取失败!");
		}
		return FormetFileSize(blockSize);
		}
		/**
		* 获取指定文件大小
		* @param f
		* @return
		* @throws Exception
		*/
		private long getFileSize(File file) throws Exception
		{
		long size = 0;
		 if (file.exists()){
		 FileInputStream fis = null;
		 fis = new FileInputStream(file);
		 size = fis.available();
		 }
		 else{
		 file.createNewFile();
		 Log.e("获取文件大小","文件不存在!");
		 }
		 return size;
		}
		 
		/**
		* 获取指定文件夹
		* @param f
		* @return
		* @throws Exception
		*/
		private long getFileSizes(File f) throws Exception
		{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++){
		if (flist[i].isDirectory()){
		size = size + getFileSizes(flist[i]);
		}
		else{
		size =size + getFileSize(flist[i]);
		}
		}
		return size;
		}
		/**
		 * 转换文件大小
		 * @param fileS
		 * @return
		 */
		private String FormetFileSize(long fileS)
		{
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize="0B";
		if(fileS==0){
		return wrongSize;
		}
		if (fileS < 1024){
		fileSizeString = df.format((double) fileS) + "B";
		 }
		else if (fileS < 1048576){
		fileSizeString = df.format((double) fileS / 1024) + "KB";
		}
		else if (fileS < 1073741824){
		    fileSizeString = df.format((double) fileS / 1048576) + "MB";
		  }
		else{
		    fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		  }
		return fileSizeString;
		}
		/**
		 * 转换文件大小,指定转换的类型
		 * @param fileS 
		 * @param sizeType 
		 * @return
		 */
		private double FormetFileSize(long fileS,int sizeType)
		{
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
		case SIZETYPE_B:
		fileSizeLong=Double.valueOf(df.format((double) fileS));
		break;
		case SIZETYPE_KB:
		fileSizeLong=Double.valueOf(df.format((double) fileS / 1024));
		break;
		case SIZETYPE_MB:
		fileSizeLong=Double.valueOf(df.format((double) fileS / 1048576));
		break;
		case SIZETYPE_GB:
		fileSizeLong=Double.valueOf(df.format((double) fileS / 1073741824));
		break;
		default:
		break;
		}
		return fileSizeLong;
		}
	
		 //将SD卡文件删除
		    public void  deleteFile(File file)
		    {
		      if (file.exists())
		      {
		       if (file.isFile())
		       {
		        file.delete();
		       }
		       // 如果它是一个目录
			       else if (file.isDirectory())
			       {
			        // 声明目录下所有的文件 files[];
					        File files[] = file.listFiles();
							        for (int i = 0; i < files.length; i++)
							        { // 遍历目录下所有的文件
							         deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
							        }
					       }
			       file.delete();
			      }
		   
		    }
	
	
	
	
	
	
	
	
	
	
	
	}	
	
}
