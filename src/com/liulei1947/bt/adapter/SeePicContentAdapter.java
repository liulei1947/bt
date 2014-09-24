package com.liulei1947.bt.adapter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import com.lidynast.customdialog.dialog.Effectstype;
import com.lidynast.customdialog.dialog.NiftyDialogBuilder;
import com.liulei1947.bt.MyApplication;
import com.liulei1947.bt.R;
import com.liulei1947.bt.db.SeePicDatabaseUtil;
import com.liulei1947.bt.entity.SeePic;
import com.liulei1947.bt.entity.SeePic;
import com.liulei1947.bt.entity.User;
import com.liulei1947.bt.sns.TencentShare;
import com.liulei1947.bt.sns.TencentShareEntity;
import com.liulei1947.bt.ui.CommentActivity;
import com.liulei1947.bt.ui.PersonalActivity;
import com.liulei1947.bt.ui.RegisterAndLoginActivity;
import com.liulei1947.bt.ui.SeePicPersonalActivity;
import com.liulei1947.bt.utils.ActivityUtil;
import com.liulei1947.bt.utils.CacheUtils;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;
import com.liulei1947.bt.view.VideoViewBuffer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-24
 * TODO
 */
@SuppressLint("NewApi")
public class SeePicContentAdapter extends BaseContentAdapter<SeePic>{
	public static final String TAG = "AIContentAdapter";
	public static final int SAVE_FAVOURITE = 2;
	protected static final String DOWNLOAD_SERVICE = "download";
	private static final String PIC_CROP = null;
	String ImageFileUrl;
	String yunpanUrl;
	String ImageFileName;
	
	public SeePicContentAdapter(Context context, List<SeePic> list,DownloadManager mydownloadManager) {
		super(context, list, mydownloadManager);
		// TODO Auto-generated constructor stub

	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    final ViewHolder viewHolder;
	    
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.ai_item_seepic, null);
			viewHolder.userName = (TextView)convertView.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView)convertView.findViewById(R.id.user_logo);
			viewHolder.favMark = (ImageView)convertView.findViewById(R.id.item_action_fav);
			viewHolder.contentText = (TextView)convertView.findViewById(R.id.content_text);
			viewHolder.contentImage = (ImageView)convertView.findViewById(R.id.content_image);
			viewHolder.love = (TextView)convertView.findViewById(R.id.item_action_love);
			viewHolder.downImage = (TextView)convertView.findViewById(R.id.item_action_down);
			viewHolder.set_wallpaper = (TextView)convertView.findViewById(R.id.item_action_set_wallpaper);
			viewHolder.hate = (TextView)convertView.findViewById(R.id.item_action_hate);
			viewHolder.share = (TextView)convertView.findViewById(R.id.item_action_share);
			viewHolder.comment = (TextView)convertView.findViewById(R.id.item_action_comment);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		final SeePic seePicentity = dataList.get(position);
		LogUtils.i("user",seePicentity.toString());
		User user = seePicentity.getAuthor();
		if(user == null){
			LogUtils.i("user","USER IS NULL");
		}
		if(user.getAvatar()==null){
			LogUtils.i("user","USER avatar IS NULL");
		}
		String avatarUrl = null;
		if(user.getAvatar()!=null){
			avatarUrl = user.getAvatar().getFileUrl();
		}
		ImageLoader.getInstance()
		.displayImage(avatarUrl, viewHolder.userLogo, 
				MyApplication.getInstance().getOptions(R.drawable.user_icon_default_main),
				new SimpleImageLoadingListener(){

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
					}
			
		});
		viewHolder.userLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(MyApplication.getInstance().getCurrentUser()==null){
					ActivityUtil.show(mContext, "请先登录。");
					Intent intent = new Intent();
					intent.setClass(mContext, RegisterAndLoginActivity.class);
					MyApplication.getInstance().getTopActivity().startActivity(intent);
					return;
				}
				MyApplication.getInstance().setCurrentSeePic(seePicentity);
//				User currentUser = BmobUser.getCurrentUser(mContext,User.class);
//				if(currentUser != null){//已登录
					Intent intent = new Intent();
					intent.setClass(MyApplication.getInstance().getTopActivity(), SeePicPersonalActivity.class);
					mContext.startActivity(intent);
//				}else{//未登录
//					ActivityUtil.show(mContext, "请先登录。");
//					Intent intent = new Intent();
//					intent.setClass(MyApplication.getInstance().getTopActivity(), RegisterAndLoginActivity.class);
//					MyApplication.getInstance().getTopActivity().startActivityForResult(intent, Constant.GO_SETTINGS);
//				}
			}
		});
		viewHolder.userName.setText(seePicentity.getAuthor().getUsername());
	
		viewHolder.contentText.setText(seePicentity.getFilmName());
		if(null == seePicentity.getindexImageUrl()){
			viewHolder.contentImage.setVisibility(View.GONE);
		}else{
			viewHolder.contentImage.setVisibility(View.VISIBLE);
			ImageLoader.getInstance()
			.displayImage(seePicentity.getindexImageUrl().getFileUrl()==null?"":seePicentity.getindexImageUrl().getFileUrl(), viewHolder.contentImage, 
					MyApplication.getInstance().getOptions(R.drawable.bg_pic_loading),
					new SimpleImageLoadingListener(){
	
						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							 float[] cons=ActivityUtil.getBitmapConfiguration(loadedImage, viewHolder.contentImage, 1.0f);
	                         RelativeLayout.LayoutParams layoutParams=
	                             new RelativeLayout.LayoutParams((int)cons[0], (int)cons[1]);
	                         layoutParams.addRule(RelativeLayout.BELOW,R.id.content_text);
	                         viewHolder.contentImage.setLayoutParams(layoutParams);
						}
				
			});
		}
		viewHolder.love.setText(seePicentity.getLove()+"");
		LogUtils.i("love",seePicentity.getMyLove()+"..");
		if(seePicentity.getMyLove()){
			viewHolder.love.setTextColor(Color.parseColor("#D95555"));
		}else{
			viewHolder.love.setTextColor(Color.parseColor("#000000"));
		}
		viewHolder.hate.setText(seePicentity.getHate()+"");
		viewHolder.love.setOnClickListener(new OnClickListener() {
			boolean oldFav = seePicentity.getMyFav();
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(MyApplication.getInstance().getCurrentUser()==null){
					ActivityUtil.show(mContext, "请先登录。");
					Intent intent = new Intent();
					intent.setClass(mContext, RegisterAndLoginActivity.class);
					MyApplication.getInstance().getTopActivity().startActivity(intent);
					return;
				}
				if(seePicentity.getMyLove()){
					ActivityUtil.show(mContext, "您已赞过啦");
					return;
				}
				
				if(SeePicDatabaseUtil.getInstance(mContext).isLoved(seePicentity)){
					ActivityUtil.show(mContext, "您已赞过啦");
					seePicentity.setMyLove(true);
					seePicentity.setLove(seePicentity.getLove()+1);
					viewHolder.love.setTextColor(Color.parseColor("#D95555"));
					viewHolder.love.setText(seePicentity.getLove()+"");
					return;
				}
				
				seePicentity.setLove(seePicentity.getLove()+1);
				viewHolder.love.setTextColor(Color.parseColor("#D95555"));
				viewHolder.love.setText(seePicentity.getLove()+"");

				seePicentity.increment("love",1);
				if(seePicentity.getMyFav()){
					seePicentity.setMyFav(false);
				}
				seePicentity.update(mContext, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						seePicentity.setMyLove(true);
						seePicentity.setMyFav(oldFav);
						SeePicDatabaseUtil.getInstance(mContext).insertFav(seePicentity);
//						SeePicDatabaseUtil.getInstance(mContext).queryFav();
						LogUtils.i(TAG, "点赞成功~");
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						seePicentity.setMyLove(true);
						seePicentity.setMyFav(oldFav);
					}
				});
			}
		});
	
		viewHolder.hate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				seePicentity.setHate(seePicentity.getHate()+1);
				viewHolder.hate.setText(seePicentity.getHate()+"");
				seePicentity.increment("hate",1);
				seePicentity.update(mContext, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ActivityUtil.show(mContext, "点踩成功~");
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		viewHolder.share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//share to sociaty
				ActivityUtil.show(mContext, "分享给好友看哦~");
				final TencentShare tencentShare=new TencentShare(MyApplication.getInstance().getTopActivity(), getQQShareEntity(seePicentity));
				tencentShare.shareToQQ();
			}
		});
		
		//*******************************************************
		
		viewHolder.downImage.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	
				try {
					ImageFileUrl = seePicentity.getindexImageUrl().getFileUrl();
					ImageFileName ="bt_sns"+seePicentity.getindexImageUrl().getFilename();
					
					//String filname = encodeGB(uTorrentFileName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(fileIsExists(ImageFileName)==true){
					ActivityUtil.show(mContext, "图片路径"+Environment.getExternalStorageDirectory().getPath()+"/BTFile/DownloadPicture/");
		
					//启动其他应用打开
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					File file = new File(Environment.getExternalStorageDirectory().getPath()+"/BTFile/DownloadPicture/"+ImageFileName);
					intent.setData(Uri.fromFile(file));
					intent.setDataAndType(Uri.fromFile(file), "image/*");
					mContext.startActivity(intent);

				}else{
					
					ActivityUtil.show(mContext, "图片下载中...");
					//downloadManager = (DownloadManager)mContext.getSystemService(DOWNLOAD_SERVICE);  
				DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ImageFileUrl));  
				 //sdcard的目录下的BTFile/DownloadPicture文件夹  
				request.setDestinationInExternalPublicDir("/BTFile/DownloadPicture", ImageFileName);  
				request.setTitle(ImageFileName);
				
				// request.setDescription("MeiLiShuo desc");  
				//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  
				//request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);  
				//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);  
				//设置文件类型 
	            //MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();  
	            //String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(ImageFileUrl));  
	            request.setMimeType("image/*");   
				long id = downloadManager.enqueue(request);  
				//保存id   
	            //appprefs.edit().putLong(DL_ID, id).commit();  
				//final TencentShare tencentShare=new TencentShare(MyApplication.getInstance().getTopActivity(), getQQShareEntity(entity));
				//tencentShare.shareToQQ();
				
				
			}
				
				
			}
		});
		
		
	//*******************************************************	
		
		viewHolder.set_wallpaper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//云端下载
				try {
					ImageFileUrl = seePicentity.getindexImageUrl().getFileUrl();
					//uTorrentFileName = entity.getFilmName()+".torrent";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ImageFileUrl="";
					e.printStackTrace();
				}

				if(!(ImageFileUrl==null)){
					
					try {
						setwallpaper(ImageFileUrl);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					
					ActivityUtil.show(mContext, "获取数据出错");
				}
			}
		});
		
		
		
		//**************************************************
		
		viewHolder.comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//评论
//				MyApplication.getInstance().setCurrentSeePic(entity);
				if(MyApplication.getInstance().getCurrentUser()==null){
					ActivityUtil.show(mContext, "请先登录。");
					Intent intent = new Intent();
					intent.setClass(mContext, RegisterAndLoginActivity.class);
					MyApplication.getInstance().getTopActivity().startActivity(intent);
					return;
				}
				Intent intent = new Intent();
				intent.setClass(MyApplication.getInstance().getTopActivity(), CommentActivity.class);
				intent.putExtra("data", seePicentity);
				mContext.startActivity(intent);
			}
		});
		
		if(seePicentity.getMyFav()){
			viewHolder.favMark.setImageResource(R.drawable.ic_action_fav_choose);
		}else{
			viewHolder.favMark.setImageResource(R.drawable.ic_action_fav_normal);
		}
		viewHolder.favMark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//收藏
				ActivityUtil.show(mContext, "收藏");
				onClickFav(v,seePicentity);
				
			}
		});
		return convertView;
	}
	

	private TencentShareEntity getQQShareEntity(SeePic qy) {
		 String title= "BT种子社区";
	        String comment="电影、游戏、图片、电子书各种资源应有尽有，等你发现";
	        String targetUrl = null;
	        String img= null;
	        if(qy.getindexImageUrl()!=null){
	        	img = qy.getindexImageUrl().getFileUrl();
	        	targetUrl=qy.getuTorrentFile().getFileUrl();
	        }else{
	        	img = "http://liulei.qiniudn.com/BT-icon.png";
	        	targetUrl="http://utorrent.bmob.cn/";
	        }
	        String summary=qy.getFilmName();
	        
	       
	        TencentShareEntity entity=new TencentShareEntity(title, img, targetUrl, summary, comment);
	        return entity;
	    }


	public static class ViewHolder{
		public ImageView userLogo;
		public TextView userName;
		public TextView contentText;
		public ImageView contentImage;
		
		public ImageView favMark;
		public TextView love;
		public TextView downImage;
		public TextView set_wallpaper;
		public TextView hate;
		public TextView share;
		public TextView comment;
	}
	
	private void onClickFav(View v,final SeePic SeePic) {
		// TODO Auto-generated method stub
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user != null && user.getSessionToken()!=null){
			BmobRelation favRelaton = new BmobRelation();
			
			SeePic.setMyFav(!SeePic.getMyFav());
			if(SeePic.getMyFav()){
				((ImageView)v).setImageResource(R.drawable.ic_action_fav_choose);
				favRelaton.add(SeePic);
				user.setFavorite(favRelaton);
				ActivityUtil.show(mContext, "收藏成功。");
				user.update(mContext, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						SeePicDatabaseUtil.getInstance(mContext).insertFav(SeePic);
						LogUtils.i(TAG, "收藏成功。");
						//try get fav to see if fav success
//						getMyFavourite();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtils.i(TAG, "收藏失败。请检查网络~");
						ActivityUtil.show(mContext, "收藏失败。请检查网络~"+arg0);
					}
				});
				
			}else{
				((ImageView)v).setImageResource(R.drawable.ic_action_fav_normal);
				favRelaton.remove(SeePic);
				user.setFavorite(favRelaton);
				ActivityUtil.show(mContext, "取消收藏。");
				user.update(mContext, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						SeePicDatabaseUtil.getInstance(mContext).deleteFav(SeePic);
						LogUtils.i(TAG, "取消收藏。");
						//try get fav to see if fav success
//						getMyFavourite();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtils.i(TAG, "取消收藏失败。请检查网络~");
						ActivityUtil.show(mContext, "取消收藏失败。请检查网络~"+arg0);
					}
				});
			}
			

		}else{
			//前往登录注册界面
			ActivityUtil.show(mContext, "收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, RegisterAndLoginActivity.class);
			MyApplication.getInstance().getTopActivity().startActivityForResult(intent, SAVE_FAVOURITE);
		}
	}
	
	private void getMyFavourite(){
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user!=null){
			BmobQuery<SeePic> query = new BmobQuery<SeePic>();
			query.addWhereRelatedTo("favorite", new BmobPointer(user));
			query.include("user");
			query.order("createdAt");
			query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.findObjects(mContext, new FindListener<SeePic>() {
				
				@Override
				public void onSuccess(List<SeePic> data) {
					// TODO Auto-generated method stub
					LogUtils.i(TAG,"get fav success!"+data.size());
					ActivityUtil.show(mContext, "fav size:"+data.size());
				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtil.show(mContext, "获取收藏失败。请检查网络~");
				}
			});
		}else{
			//前往登录注册界面
			ActivityUtil.show(mContext, "获取收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, RegisterAndLoginActivity.class);
			MyApplication.getInstance().getTopActivity().startActivityForResult(intent,Constant.GET_FAVOURITE);
		}
		
		
		
		
	}

	 
	//***********************************************************
	 public boolean fileIsExists(String fileName){
         try{
                 File f=new File(Environment.getExternalStorageDirectory().getPath()+"/BTFile/DownloadPicture/"+fileName);
                 if(!f.exists()){
                         return false;
                 }
                 
         }catch (Exception e) {
                 // TODO: handle exception
                 return false;
         }
         return true;
 }

	 
	 //***********************
	 public void dialogShow(int effectId,String url){
	        final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(mContext);
	       
	        dialogBuilder
	                .withTitle("云端提示：点击下载即可播放视频")                                  //.withTitle(null)  no title
	                .withTitleColor("#FFFFFF")                                  //def
	                //.withDividerColor("#11000000")                              //def
	                //.withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
	                //.withMessageColor("#FFFFFF")                                //def
	                .withIcon(R.drawable.yunpan)
	                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
	                .withDuration(200)                                          //def
	                .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
	                .withButton1Text("返回")                                      //def gone
	                .withButton2Text("取消")                                  //def gone
	                .setCustomView(R.layout.custom_view,mContext)
	                //.setCustomView(View or ResId,context)
	                .show();
	        final WebView detail_webview=(WebView) dialogBuilder.findViewById(R.id.yunpan_WebView);
	        detail_webview.getSettings().setJavaScriptEnabled(true);
			detail_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			
			detail_webview.setDownloadListener(new DownloadListener(){

				@Override
				public void onDownloadStart(String url, String userAgent,
						String contentDisposition, String mimetype, long contentLength) {
					// TODO Auto-generated method stub
					Toast.makeText(
							mContext,
			    	          "Path is："
			    	              + url, Toast.LENGTH_LONG).show();
					String url2=url+".mp4";
					
					Intent intent=new Intent();
					intent.setClass(mContext, VideoViewBuffer.class);
					intent.setData(Uri.parse(url));
            	    intent.putExtra(Constant.VideoUrl, url);
            		//intent.setAction(Intent.ACTION_VIEW);
            		mContext.startActivity(intent);
	
				}
				
			});
	
			
			dialogBuilder.setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	if (detail_webview.canGoBack()) {
                		detail_webview.goBack();// 返回前一个页面
                		  }else{
                			  dialogBuilder.dismiss();
                			  }
                	}
            })
            .setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	dialogBuilder.dismiss();
                }
            });
	    }
	 
	 
	//***********************
	 public void setwallpaper(final String imageFileName) throws IOException{
		    //File file = new File(Environment.getExternalStorageDirectory().getPath()+"/BTFile/uTorrentFile/"+imageFileName);
		    //Uri uri = Uri.fromFile(file);
		    new Thread(){  
			      public void run() {  
						try {
							 Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imageFileName);
							    int size=Math.min(bitmap.getHeight(), bitmap.getWidth());
							int x=(bitmap.getWidth()-size)/2;
							int y=(bitmap.getHeight()-size)/2;
							Bitmap result = Bitmap.createBitmap(bitmap, x,y,size, size);

						    WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);  
					  
						    wallpaperManager.setBitmap(result);

							} catch (IOException e) {
								// TODO Auto-generated catch block
							}
			        };  
			   }.start();  

			   ActivityUtil.show(mContext, "设置壁纸成功");
	 }
	
	
	
}