package com.liulei1947.bt.ui;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.youmi.android.offers.OffersManager;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.lidynast.customdialog.dialog.Effectstype;
import com.lidynast.customdialog.dialog.NiftyDialogBuilder;
import com.lidynast.customdialog.dialog.ShowPicNiftyDialogBuilder;
import com.liulei1947.bt.MyApplication;
import com.liulei1947.bt.R;
import com.liulei1947.bt.adapter.CommentAdapter;
import com.liulei1947.bt.base.BasePageActivity;
import com.liulei1947.bt.base.BasePageActivity2;
import com.liulei1947.bt.db.DatabaseUtil;
import com.liulei1947.bt.db.SeePicDatabaseUtil;
import com.liulei1947.bt.entity.Comment;
import com.liulei1947.bt.entity.SeePic;
import com.liulei1947.bt.entity.User;
import com.liulei1947.bt.sns.TencentShare;
import com.liulei1947.bt.sns.TencentShareEntity;
import com.liulei1947.bt.utils.ActivityUtil;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-4-2
 * TODO
 */

@SuppressLint("NewApi")
public class SeePicCommentActivity extends BasePageActivity2 implements OnClickListener{
	long downid;
	private ActionBar actionbar;
	private ListView commentList;
	private TextView footer;
	String ImageFileUrl;
	String ImageFileName;
	String yunpanUrl1;
	private EditText commentContent ;
	private Button commentCommit;
	
	private TextView userName;
	private TextView commentItemContent;
	private ImageView commentItemImage;
	private DownloadManager downloadManager;  
	private ImageView userLogo;
	private ImageView myFav;
	private TextView comment;
	private TextView share;
	private TextView love;
	private TextView hate;
	private TextView down;
	private TextView set_wallpaper;
	private SeePic SeePic;
	private String commentEdit = "";
	Context mContext;
	private CommentAdapter mAdapter;
	String mimeString ;
	private List<Comment> comments = new ArrayList<Comment>();
	
	private int pageNum;
	
	@Override
	protected void setLayoutView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_comment_seepic);
		mContext=this;
		downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);  
        registerReceiver(receiverACTION_DOWNLOAD_COMPLETE, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));  

	}
	//******************************************
	 private BroadcastReceiver receiverACTION_DOWNLOAD_COMPLETE = new BroadcastReceiver() {   
	        @Override   
	        public void onReceive(Context context, Intent intent2) {   
	            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
	        	 downid= intent2.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
	        	
	        	 if(queryDownloadStatus()){
	        		
	    			ActivityUtil.show(mContext, "图片下载成功...图片路径"+Environment.getExternalStorageDirectory().getPath()+"/BTFile/DownloadPicture/");

	        	}
	       
	        }   
	      
	    }; 
	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		actionbar = (ActionBar)findViewById(R.id.actionbar_comment);
		commentList = (ListView)findViewById(R.id.comment_list);
		footer = (TextView)findViewById(R.id.loadmore);
		
		commentContent = (EditText)findViewById(R.id.comment_content);
		commentCommit = (Button)findViewById(R.id.comment_commit);
		
		userName = (TextView)findViewById(R.id.user_name);
		commentItemContent = (TextView)findViewById(R.id.content_text);
		commentItemImage = (ImageView)findViewById(R.id.content_image);
		
		userLogo = (ImageView)findViewById(R.id.user_logo);
		myFav = (ImageView)findViewById(R.id.item_action_fav);
		comment = (TextView)findViewById(R.id.item_action_comment);
		share = (TextView)findViewById(R.id.item_action_share);
		love = (TextView)findViewById(R.id.item_action_love);
		hate = (TextView)findViewById(R.id.item_action_hate);
		down = (TextView)findViewById(R.id.item_action_down);
		set_wallpaper = (TextView)findViewById(R.id.item_action_set_wallpaper);
		
	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		SeePic = (SeePic)getIntent().getSerializableExtra("data");//MyApplication.getInstance().getCurrentSeePic();
		pageNum = 0;
		
		
		actionbar.setTitle("发表评论");
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeAction(new Action() {
			
			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.logo;
			}
		});
		
		mAdapter = new CommentAdapter(SeePicCommentActivity.this, comments,downloadManager);
		commentList.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(commentList);
		commentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ActivityUtil.show(SeePicCommentActivity.this, "po"+position);
			}
		});
		commentList.setCacheColorHint(0);
		commentList.setScrollingCacheEnabled(false);
		commentList.setScrollContainer(false);
		commentList.setFastScrollEnabled(true);
		commentList.setSmoothScrollbarEnabled(true);
		
		initMoodView(SeePic);
	}

	private void initMoodView(SeePic mood2) {
		// TODO Auto-generated method stub
		if(mood2 == null){
			return;
		}
		userName.setText(SeePic.getAuthor().getUsername());
		commentItemContent.setText(SeePic.getFilmName());
		if(null == SeePic.getindexImageUrl()){
			commentItemImage.setVisibility(View.GONE);
		}else{
			commentItemImage.setVisibility(View.VISIBLE);
			ImageLoader.getInstance()
			.displayImage(SeePic.getindexImageUrl().getFileUrl()==null?"":SeePic.getindexImageUrl().getFileUrl(), commentItemImage, 
					MyApplication.getInstance().getOptions(R.drawable.bg_pic_loading),
					new SimpleImageLoadingListener(){
	
						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							 float[] cons=ActivityUtil.getBitmapConfiguration(loadedImage, commentItemImage, 1.0f);
	                         RelativeLayout.LayoutParams layoutParams=
	                             new RelativeLayout.LayoutParams((int)cons[0], (int)cons[1]);
	                         layoutParams.addRule(RelativeLayout.BELOW,R.id.content_text);
	                         commentItemImage.setLayoutParams(layoutParams);
						}
				
			});
		}
		
		love.setText(SeePic.getLove()+"");
		if(SeePic.getMyLove()){
			love.setTextColor(Color.parseColor("#D95555"));
		}else{
			love.setTextColor(Color.parseColor("#000000"));
		}
		hate.setText(SeePic.getHate()+"");
		if(SeePic.getMyFav()){
			myFav.setImageResource(R.drawable.ic_action_fav_choose);
		}else{
			myFav.setImageResource(R.drawable.ic_action_fav_normal);
		}
		
		
		User user = SeePic.getAuthor();
		BmobFile avatar = user.getAvatar();
		if(null != avatar){
			ImageLoader.getInstance()
			.displayImage(avatar.getFileUrl(), userLogo, 
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

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		footer.setOnClickListener(this);
		commentCommit.setOnClickListener(this);
		commentItemImage.setOnClickListener(this);
		userLogo.setOnClickListener(this);
		myFav.setOnClickListener(this);
		love.setOnClickListener(this);
		hate.setOnClickListener(this);
		down.setOnClickListener(this);
		set_wallpaper.setOnClickListener(this);
		share.setOnClickListener(this);
		comment.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		fetchComment();
	}
	
	private void fetchComment(){
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereRelatedTo("relation", new BmobPointer(SeePic));
		query.include("user");
		query.order("createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		query.findObjects(this, new FindListener<Comment>() {
			
			@Override
			public void onSuccess(List<Comment> data) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"get comment success!"+data.size());
				if(data.size()!=0 && data.get(data.size()-1)!=null){
					
					if(data.size()<Constant.NUMBERS_PER_PAGE){
						ActivityUtil.show(mContext, "已加载完所有评论~");
						footer.setText("暂无更多评论~");
					}
					
					mAdapter.getDataList().addAll(data);
					mAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(commentList);
					LogUtils.i(TAG,"refresh");
				}else{
					ActivityUtil.show(mContext, "暂无更多评论~");
					footer.setText("暂无更多评论~");
					pageNum--;
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ActivityUtil.show(SeePicCommentActivity.this, "获取评论失败。请检查网络~");
				pageNum--;
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_logo:
			onClickUserLogo();
			break;
		case R.id.loadmore:
			onClickLoadMore();
			break;
		case R.id.content_image:
			onClickShow();
			break;
		case R.id.comment_commit:
			onClickCommit();
			break;
		case R.id.item_action_fav:
			onClickFav(v);
			break;
		case R.id.item_action_love:
			onClickLove();
			break;
		case R.id.item_action_hate:
			onClickHate();
			break;
		case R.id.item_action_down:
			onClickDown();
			break;
		case R.id.item_action_set_wallpaper:
			onClickYunpan();
			break;
		case R.id.item_action_share:
			onClickShare();
			break;
		case R.id.item_action_comment:
			onClickComment();
			break;
		default:
			break;
		}
	}  
	private void onClickShow() {
		// TODO Auto-generated method stub
		//****************************************
			// TODO Auto-generated method stub
	        final ShowPicNiftyDialogBuilder dialogBuilder=ShowPicNiftyDialogBuilder.getInstance(mContext);
	        dialogBuilder            
            .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
            .withDuration(200)                                          //def
            .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
            //.withButton1Text("下载")                                      //def gone
            //.withButton2Text("")                                  //def gone
            .setCustomView(R.layout.show_pic_custom_view,mContext)
            //.setCustomView(View or ResId,context)
            .show();
	        final ImageView showImage=(ImageView) dialogBuilder.findViewById(R.id.show_image);
	        ImageLoader.getInstance()
			.displayImage(SeePic.getindexImageUrl().getFileUrl()==null?"":SeePic.getindexImageUrl().getFileUrl(), showImage, 
					MyApplication.getInstance().getOptions(R.drawable.bg_pic_loading),
					new SimpleImageLoadingListener(){
						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							
						}
				
			});
         
           
            dialogBuilder.setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             	

                	}
            })
            .setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	dialogBuilder.dismiss();
                }
            });
		}

		
		


	private void onClickYunpan() {
		// TODO Auto-generated method stub
		//云端下载
		try {
			ImageFileUrl = SeePic.getindexImageUrl().getFileUrl();
			//uTorrentFileName = entity.getFilmName()+".torrent";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			yunpanUrl1="";
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

	private void onClickDown() {
		// TODO Auto-generated method stub
		try {
			ImageFileUrl = SeePic.getindexImageUrl().getFileUrl();
			ImageFileName ="bt_sns"+SeePic.getindexImageUrl().getFilename();
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
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			File file = new File(Environment.getExternalStorageDirectory().getPath()+"/BTFile/DownloadPicture/"+ImageFileName);
			intent.setData(Uri.fromFile(file));
			intent.setDataAndType(Uri.fromFile(file), "image/*");
			mContext.startActivity(intent);

		}else{
		
		DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(DOWNLOAD_SERVICE);  
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ImageFileUrl));  
		 //sdcard的目录下的BTFile/uTorrentFile文件夹  
		request.setDestinationInExternalPublicDir("BTFile/DownloadPicture", ImageFileName);  
		request.setTitle(ImageFileName);  	
		//设置文件类型  
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();  
       String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(ImageFileUrl));  
       request.setMimeType(mimeString);   
		long id = downloadManager.enqueue(request);  
		ActivityUtil.show(mContext, "图片下载中...");
		//保存id   
		}
	}

	private void onClickUserLogo() {
		// TODO Auto-generated method stub
		//跳转到个人信息界面
		User currentUser = BmobUser.getCurrentUser(this,User.class);
		if(currentUser != null){//已登录
			Intent intent = new Intent();
			intent.setClass(MyApplication.getInstance().getTopActivity(), PersonalActivity.class);
			mContext.startActivity(intent);
		}else{//未登录
			ActivityUtil.show(this, "请先登录。");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivityForResult(intent, Constant.GO_SETTINGS);
		}
	}

	private void onClickLoadMore() {
		// TODO Auto-generated method stub
		fetchData();
	}

	
	private void onClickCommit() {
		// TODO Auto-generated method stub
		User currentUser = BmobUser.getCurrentUser(this,User.class);
		if(currentUser != null){//已登录
			commentEdit = commentContent.getText().toString().trim();
			if(TextUtils.isEmpty(commentEdit)){
				ActivityUtil.show(this, "评论内容不能为空。");
				return;
			}
			//comment now
			publishComment(currentUser,commentEdit);
		}else{//未登录
			ActivityUtil.show(this, "发表评论前请先登录。");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivityForResult(intent, Constant.PUBLISH_COMMENT);
		}
		
	}

	private void publishComment(User user,String content){
		
		final Comment comment = new Comment();
		comment.setUser(user);
		comment.setCommentContent(content);
		comment.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ActivityUtil.show(SeePicCommentActivity.this, "评论成功。");
				if(mAdapter.getDataList().size()<Constant.NUMBERS_PER_PAGE){
					mAdapter.getDataList().add(comment);
					mAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(commentList);
				}
				commentContent.setText("");
				hideSoftInput();
				
				//将该评论与强语绑定到一起
				BmobRelation relation = new BmobRelation();
				relation.add(comment);
				SeePic.setRelation(relation);
				SeePic.update(mContext, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						LogUtils.i(TAG, "更新评论成功。");
//						fetchData();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtils.i(TAG, "更新评论失败。"+arg1);
					}
				});
				
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ActivityUtil.show(SeePicCommentActivity.this, "评论失败。请检查网络~");
			}
		});
	}
	
	private void onClickFav(View v) {
		// TODO Auto-generated method stub
		
		User user = BmobUser.getCurrentUser(this, User.class);
		if(user != null && user.getSessionToken()!=null){
			BmobRelation favRelaton = new BmobRelation();
			SeePic.setMyFav(!SeePic.getMyFav());
			if(SeePic.getMyFav()){
				((ImageView)v).setImageResource(R.drawable.ic_action_fav_choose);
				favRelaton.add(SeePic);
				ActivityUtil.show(mContext, "收藏成功。");
			}else{
				((ImageView)v).setImageResource(R.drawable.ic_action_fav_normal);
				favRelaton.remove(SeePic);
				ActivityUtil.show(mContext, "取消收藏。");
			}
			
			user.setFavorite(favRelaton);
			user.update(this, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "收藏成功。");
					ActivityUtil.show(SeePicCommentActivity.this, "收藏成功。");
					//try get fav to see if fav success
//					getMyFavourite();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "收藏失败。请检查网络~");
					ActivityUtil.show(SeePicCommentActivity.this, "收藏失败。请检查网络~"+arg0);
				}
			});
		}else{
			//前往登录注册界面
			ActivityUtil.show(this, "收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivityForResult(intent, Constant.SAVE_FAVOURITE);
		}
		
	}
	
	private void getMyFavourite(){
		User user = BmobUser.getCurrentUser(this, User.class);
		if(user!=null){
			BmobQuery<SeePic> query = new BmobQuery<SeePic>();
			query.addWhereRelatedTo("favorite", new BmobPointer(user));
			query.include("user");
			query.order("createdAt");
			query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.findObjects(this, new FindListener<SeePic>() {
				
				@Override
				public void onSuccess(List<SeePic> data) {
					// TODO Auto-generated method stub
					LogUtils.i(TAG,"get fav success!"+data.size());
					ActivityUtil.show(SeePicCommentActivity.this, "fav size:"+data.size());
				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtil.show(SeePicCommentActivity.this, "获取收藏失败。请检查网络~");
				}
			});
		}else{
			//前往登录注册界面
			ActivityUtil.show(this, "获取收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivityForResult(intent, Constant.GET_FAVOURITE);
		}
	}

	boolean isFav = false;
	private void onClickLove() {
		// TODO Auto-generated method stub
		User user = BmobUser.getCurrentUser(this, User.class);
		if(user==null){
			//前往登录注册界面
			ActivityUtil.show(this, "请先登录。");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivity(intent);
			return;
		}
		if(SeePic.getMyLove()){
			ActivityUtil.show(SeePicCommentActivity.this, "您已经赞过啦");
			return;
		}
		isFav = SeePic.getMyFav();
		if(isFav){
			SeePic.setMyFav(false);
		}
		SeePic.setLove(SeePic.getLove()+1);
		love.setTextColor(Color.parseColor("#D95555"));
		love.setText(SeePic.getLove()+"");
		SeePic.increment("love",1);
		SeePic.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				SeePic.setMyLove(true);
				SeePic.setMyFav(isFav);
				SeePicDatabaseUtil.getInstance(mContext).insertFav(SeePic);
				
				ActivityUtil.show(mContext, "点赞成功~");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void onClickHate() {
		// TODO Auto-generated method stub
		SeePic.setHate(SeePic.getHate()+1);
		hate.setText(SeePic.getHate()+"");
		SeePic.increment("hate",1);
		SeePic.update(mContext, new UpdateListener() {
			
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

	private void onClickShare() {
		// TODO Auto-generated method stub
		ActivityUtil.show(SeePicCommentActivity.this, "share to ...");
		final TencentShare tencentShare=new TencentShare(MyApplication.getInstance().getTopActivity(), getQQShareEntity(SeePic));
		tencentShare.shareToQQ();
	}

	 private TencentShareEntity getQQShareEntity(SeePic qy) {
	        String title= "这里好多美丽的风景";
	        String comment="来领略最美的风景吧";
	        String img= null;
	        String targetUrl = null;
	        if(qy.getindexImageUrl()!=null){
	        	img = qy.getindexImageUrl().getFileUrl();
	        	targetUrl=qy.getuTorrentFile().getFileUrl();
	        }else{
	        	img = "http://www.codenow.cn/appwebsite/website/yyquan/uploads/53af6851d5d72.png";
	        }
	        String summary=qy.getFilmName();
	     
	        TencentShareEntity entity=new TencentShareEntity(title, img, targetUrl, summary, comment);
	        return entity;
	    }
	
	private void onClickComment() {
		// TODO Auto-generated method stub
		commentContent.requestFocus();

		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);  

		imm.showSoftInput(commentContent, 0);  
	}
	
	private void hideSoftInput(){
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);  

		imm.hideSoftInputFromWindow(commentContent.getWindowToken(), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case Constant.PUBLISH_COMMENT:
				//登录完成
				commentCommit.performClick();
				break;
			case Constant.SAVE_FAVOURITE:
				myFav.performClick();
				break;
			case Constant.GET_FAVOURITE:
				
				break;
			case Constant.GO_SETTINGS:
				userLogo.performClick();
				break;
			default:
				break;
			}
		}
		
	}
	
	
	/*** 
     * 动态设置listview的高度 
     *  item 总布局必须是linearLayout
     * @param listView 
     */  
    public void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();  
        if (listAdapter == null) {  
            return;  
        }  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight  
                + (listView.getDividerHeight() * (listAdapter.getCount()-1))  
                +15;  
        listView.setLayoutParams(params);  
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
  	        final NiftyDialogBuilder dialogBuilder2=NiftyDialogBuilder.getInstance(this);
  	       
  	        dialogBuilder2
  	                .withTitle("云端分享")                                  //.withTitle(null)  no title
  	                .withTitleColor("#FFFFFF")                                  //def
  	                //.withDividerColor("#11000000")                              //def
  	                //.withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
  	                //.withMessageColor("#FFFFFF")                                //def
  	                .withIcon(R.drawable.yunpan)
  	                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
  	                .withDuration(200)                                          //def
  	                .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
  	                //.withButton1Text("返回")                                      //def gone
  	                //.withButton2Text("取消") 
  	                //def gone
  	                .setCustomView(R.layout.custom_view,mContext)
  	                //.setCustomView(View or ResId,context)
  	                .show();
  	        final WebView detail_webview=(WebView) dialogBuilder2.findViewById(R.id.yunpan_WebView);
  	        detail_webview.getSettings().setJavaScriptEnabled(true);
  			detail_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
  			detail_webview.setWebViewClient(new SampleWebViewClient());
  			detail_webview.setDownloadListener(new DownloadListener(){

  				@Override
  				public void onDownloadStart(String url, String userAgent,
  						String contentDisposition, String mimetype, long contentLength) {
  					// TODO Auto-generated method stub
  					Intent intent=new Intent();
              		intent.setData(Uri.parse(url));
              		intent.setAction(Intent.ACTION_VIEW);
              		mContext.startActivity(intent);
  	
  				}
  				
  			});
  			detail_webview.loadUrl(url);
  			dialogBuilder2.setButton1Click(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                  	if (detail_webview.canGoBack()) {
                  		detail_webview.goBack();// 返回前一个页面
                  		  }else{
                  			  dialogBuilder2.dismiss();
                  			  }
                  	}
              })
              .setButton2Click(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                  	dialogBuilder2.dismiss();
                  }
              });
  	    }
  	 
  	 
  	//********************************
  	 private class SampleWebViewClient extends WebViewClient {
  	 	
  	 			public void onReceivedError(WebView view,int erroeCode,String description,String failingUrl){
  	 			//load.setVisibility(View.GONE);	
  	     			if(erroeCode==-2){
  	     				//Toast.makeText(getApplication(), description+"施主请回吧 ", Toast.LENGTH_LONG).show();
  	     				view.loadUrl("file:///android_asset/404error.html");
  	     			}
  	     		    
  	 			}
  	         @Override
  	         public boolean shouldOverrideUrlLoading(WebView view, String url) {
  	             // Return false so the WebView loads the url
  	         	//loadingProgressBar.setVisibility(View.VISIBLE);
  	         	view.loadUrl(url);
  	             return true;
  	         }
  	         @Override
  	         public void onPageStarted(WebView view, String url,Bitmap favicon) {
  	         	//load.setVisibility(View.VISIBLE);
  	             
  	          
  	         }
  	         @Override
  	         public void onPageFinished(WebView view, String url) {
  	            
  	         	//load.setVisibility(View.GONE);	
  	             super.onPageFinished(view, url);
  	             // If the PullToRefreshAttacher is refreshing, make it as complete
  	          
  	         }
  	 }  
  	//**************************************************
//  	  @Override
//      protected void onDestroy() {
//      	// TODO Auto-generated method stub
//      	super.onDestroy();
//          unregisterReceiver(receiverACTION_DOWNLOAD_COMPLETE);  
//
//      }
  	 
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
	    			ActivityUtil.show(mContext, "下载成功...图片路径"+Environment.getExternalStorageDirectory().getPath()+"/BTFile/DownloadPicture/");
		        	result=true;
	                break;   
	            case DownloadManager.STATUS_FAILED:   
	                //清除已下载的内容，重新下载 
		        	 Toast.makeText(SeePicCommentActivity.this, "网络错误 下载失败,请重试",Toast.LENGTH_LONG).show();
	                Log.v("down", "STATUS_FAILED");  
	                downloadManager.remove(downid);   
	                break;  
	           
	                
	            }   
	        }
			return result;  
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
		
	    
//***********************************************************
	    @Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			overridePendingTransition (R.anim.open_main, R.anim.close_next);
		}


}
