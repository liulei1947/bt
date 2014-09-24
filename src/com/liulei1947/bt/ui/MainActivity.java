package com.liulei1947.bt.ui;

import java.io.File;
import java.io.FileNotFoundException;

import net.youmi.android.offers.OffersManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import com.capricorn.RayMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidynast.customdialog.dialog.Effectstype;
import com.lidynast.customdialog.dialog.NiftyDialogBuilder;
import com.liulei1947.bt.MyApplication;
import com.liulei1947.bt.R;
import com.liulei1947.bt.entity.User;
import com.liulei1947.bt.utils.ActivityUtil;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;
import com.markupartist.android.widget.ActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;


@SuppressLint("NewApi")
public class MainActivity extends SlidingFragmentActivity implements OnClickListener{
	private DownloadManager downloadManager;  
	private SharedPreferences appprefs;  
	private static final String DL_ID = "downloadId";  
	long downid;
	boolean flag=false;
    public static final String TAG = "MainActivity";
	private NaviFragment naviFragment;
	private ImageView leftMenu;
	private ImageView rightMenu;
	private SlidingMenu mSlidingMenu;
	Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.center_frame);
		mContext=this;
		//启动应用更新
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.forceUpdate(this);
		downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);  
		//在应用的主Activity onCreate() 函数中开启推送服务
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		
		leftMenu = (ImageView)findViewById(R.id.topbar_menu_left);
		//setUserlogo();	
		rightMenu = (ImageView)findViewById(R.id.topbar_menu_right);
		leftMenu.setOnClickListener(this);
		rightMenu.setOnClickListener(this);
		
		initFragment();
        registerReceiver(receiverACTION_DOWNLOAD_COMPLETE, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));  
        //registerReceiver(receiverACTION_NOTIFICATION_CLICKED, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));  

	

	}
	 @Override  
	    protected void onPause() {  
	        // TODO Auto-generated method stub  
	        super.onPause();  
	        unregisterReceiver(receiverACTION_DOWNLOAD_COMPLETE);  
	    }  
	    @Override  
	    protected void onResume() {  
	        // TODO Auto-generated method stub  
	        super.onResume();  
	        registerReceiver(receiverACTION_DOWNLOAD_COMPLETE, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));  

	    }
	//******************************************
		 private BroadcastReceiver receiverACTION_DOWNLOAD_COMPLETE = new BroadcastReceiver() {   
		        @Override   
		        public void onReceive(Context context, Intent intent2) {   
		            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
		        	 downid= intent2.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
		        	
		        	 if(queryDownloadStatus()){
		        		//启动百度云盘或手机迅雷下载
		         	    Uri url=downloadManager.getUriForDownloadedFile(downid);
		         	    String mimeType = downloadManager.getMimeTypeForDownloadedFile(downid);
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.setData(url);
						if(mimeType.equals("application/x-bittorrent")){
							intent.setDataAndType(url, "application/x-bittorrent");
							context.startActivity(intent);
				        	Toast.makeText(MainActivity.this, "建议点击百度云保存到云端播放",Toast.LENGTH_LONG).show();

						}
						

		        	}
		       
		        }   
		      
		    }; 
		    
//private BroadcastReceiver receiverACTION_NOTIFICATION_CLICKED = new BroadcastReceiver() {   
//		        @Override   
//		        public void onReceive(Context context, Intent intent1) {   
//		            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听 
//		        long clickid = intent1.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
//				  Uri url=downloadManager.getUriForDownloadedFile(clickid);
//				//启动百度云盘或手机迅雷下载
//					Intent intent = new Intent();
//					intent.setAction(android.content.Intent.ACTION_VIEW);
//					intent.addCategory(Intent.CATEGORY_DEFAULT);
//					//File file = new File(Environment.getExternalStorageDirectory().getPath()+"/BTFile/uTorrentFile/"+uTorrentFileName);
//					intent.setData(url);
//					intent.setDataAndType(url, "application/x-bittorrent");
//					context.startActivity(intent);
//		        }   
//		    };   
//	
	//*******************************************
		    private boolean queryDownloadStatus() { 
		    	boolean result=false;
		        DownloadManager.Query query = new DownloadManager.Query();   
		        query.setFilterById(downid);   
		        Cursor c = downloadManager.query(query);   
		        if(c.moveToFirst()) {   
		            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));   
		            switch(status) {   
		            case DownloadManager.STATUS_PAUSED:   
		                Log.v("down", "STATUS_PAUSED");  
		            case DownloadManager.STATUS_PENDING:   
		                Log.v("down", "STATUS_PENDING");  
		            case DownloadManager.STATUS_RUNNING:   
		                //正在下载，不做任何事情  
		                Log.v("down", "STATUS_RUNNING");  
		                break;   
		            case DownloadManager.STATUS_SUCCESSFUL:   
		                //完成  
		                Log.v("down", "下载完成");  
			        	Toast.makeText(MainActivity.this, "下载成功",Toast.LENGTH_LONG).show();
			        	result=true;
		                break;   
		            case DownloadManager.STATUS_FAILED:   
		                //清除已下载的内容，重新下载 
			        	 Toast.makeText(MainActivity.this, "网络错误 下载失败,请重试",Toast.LENGTH_LONG).show();
		                Log.v("down", "STATUS_FAILED");  
		                downloadManager.remove(downid);   
		                break;  
		           
		                
		            }   
		        }
				return result;  
		    }  
	//***********************************************************
	
		    
//**************************************************		    
		   

	private void initFragment() {
		mSlidingMenu = getSlidingMenu();
		setBehindContentView(R.layout.frame_navi); // 给滑出的slidingmenu的fragment制定layout
		naviFragment = new NaviFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_navi, naviFragment).commit();
		// 设置slidingmenu的属性
		mSlidingMenu.setAboveOffset(48);
		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置slidingmeni从哪侧出现
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 只有在边上才可以打开
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 偏移量
		mSlidingMenu.setFadeEnabled(true);
		mSlidingMenu.setFadeDegree(0.5f);
		mSlidingMenu.setMenu(R.layout.frame_navi);
		
		
		Bundle mBundle = null;
		// 导航打开监听事件
		mSlidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				
				
				
				
				
				
			}
		});
		// 导航关闭监听事件
		mSlidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
			}
		});
	
		
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {
            case R.id.topbar_menu_left:
                mSlidingMenu.toggle();
                break;
            case R.id.topbar_menu_right:
              //当前用户登录
            	MySearch();
//                User currentUser = BmobUser.getCurrentUser(MainActivity.this,User.class);
//                if (currentUser != null) {
//                    // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
//                    String name = currentUser.getUsername();
//                    String email = currentUser.getEmail();
//                    LogUtils.i(TAG,"username:"+name+",email:"+email);
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this, EditActivity.class);
//                    startActivity(intent);
//                } else {
//                    // 缓存用户对象为空时， 可打开用户注册界面…
//                    Toast.makeText(MainActivity.this, "请先登录。",
//                            Toast.LENGTH_SHORT).show();
////                  redictToActivity(mContext, RegisterAndLoginActivity.class, null);
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this, RegisterAndLoginActivity.class);
//                    startActivity(intent);
//                }
//                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	OffersManager.getInstance(MainActivity.this).onAppExit();

    }
    
	private static long firstTime;
	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (firstTime + 2000 > System.currentTimeMillis()) {
			MyApplication.getInstance().exit();
			super.onBackPressed();
		} else {
			ActivityUtil.show(MainActivity.this, "再按一次退出程序");
		}
		firstTime = System.currentTimeMillis();
	}
	
	//*********************************
	public void setUserlogo(){
		 //当前用户登录
        User currentUser = BmobUser.getCurrentUser(MainActivity.this,User.class);
        if (currentUser!=null&&currentUser.getAvatar() != null) {
            // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
            ImageLoader.getInstance()
			.displayImage(currentUser.getAvatar().getFileUrl(), leftMenu, 
					MyApplication.getInstance().getOptions(R.drawable.content_image_default),
					new SimpleImageLoadingListener(){
	
						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							LogUtils.i(TAG,"load personal icon completed.");
						}
				
			});
            
        } 
	}
	
	
	//***********************************************************

	protected void MySearch() {
		// TODO Auto-generated method stub
        final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(mContext);
        dialogBuilder
        .withTitle("搜索")                                  //.withTitle(null)  no title
        .withTitleColor("#FFFFFF")                                  //def
        //.withDividerColor("#11000000")                              //def
        //.withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
        //.withMessageColor("#FFFFFF")                                //def
        .withIcon(R.drawable.yunpan)
        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
        .withDuration(200)                                          //def
        .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
        .withButton1Text("搜索")                                      //def gone
        //.withButton2Text("取消")                                  //def gone
        .setCustomView(R.layout.serch_custom_view,mContext)
        //.setCustomView(View or ResId,context)
        .show();
        final TextView serchWordInput=(TextView) dialogBuilder.findViewById(R.id.serch_word_input);
      	
     
       
        dialogBuilder.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	final String serchWord = serchWordInput.getText().toString();		
            	//Toast.makeText(mContext,"文件内容："+ serchWord, Toast.LENGTH_SHORT).show();
            	dialogBuilder.dismiss();
            	Intent intent = new Intent(mContext, SerchTorrentContentActivity.class);
            	intent.putExtra(Constant.serchWord, serchWord);
        		startActivity(intent);
				overridePendingTransition (R.anim.open_next, R.anim.close_main);

            	}
        })
        .setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	dialogBuilder.dismiss();
            }
        });
	}
	
		
	
	
	
	
	
	
	
	
	
	
	
	
}
