package com.liulei1947.bt.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.youmi.android.offers.OffersManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.capricorn.ArcMenu;
import com.capricorn.RayMenu;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidynast.customdialog.dialog.Effectstype;
import com.lidynast.customdialog.dialog.NiftyDialogBuilder;
import com.liulei1947.bt.MyApplication;
import com.liulei1947.bt.R;
import com.liulei1947.bt.adapter.AIContentAdapter;
import com.liulei1947.bt.adapter.SeePicContentAdapter;
import com.liulei1947.bt.base.BaseActivity;
import com.liulei1947.bt.base.BaseFragment;
import com.liulei1947.bt.db.DatabaseUtil;
import com.liulei1947.bt.db.SeePicDatabaseUtil;
import com.liulei1947.bt.entity.QiangYu;
import com.liulei1947.bt.entity.SeePic;
import com.liulei1947.bt.utils.ActivityUtil;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-23s
 * TODO
 */

public class SerchPicContentActivity extends BaseActivity{
	int pageNum ;
	long downid;
	boolean flag=false;
	private String lastItemTime;//当前列表结尾的条目的创建时间，
	private DownloadManager downloadManager;  
	private ArrayList<SeePic> picListItems;
	private PullToRefreshListView mPullRefreshListView;
	private SeePicContentAdapter mAdapter;
	private ListView actualListView;
	RayMenu bt_fragment_arc_menu;
	private TextView networkTips;
	private ProgressBar progressbar;
	private boolean pullFromUser;
	 Context mcontext;
	 private String serchWord;
    private static final int[] ITEM_DRAWABLES = { R.drawable.finding, R.drawable.up,
	R.drawable.edit,R.drawable.reloading};

	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	
	@SuppressLint("SimpleDateFormat")
	private String getCurrentTime(){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String times = formatter.format(new Date(System.currentTimeMillis()));
	     return times;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		serchWord=bundle.getString(Constant.serchWord);
		setContentView(R.layout.fragment_serch_content);
	
		downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);  
		pageNum = 0;
		lastItemTime = getCurrentTime();
		mcontext=this;
		initView();
        registerReceiver(receiverACTION_DOWNLOAD_COMPLETE, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));  

		LogUtils.i(TAG,"curent time:"+lastItemTime);
	}
	//******************************************
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
	        @SuppressLint("NewApi")
			@Override   
	        public void onReceive(Context context, Intent intent2) {   
	            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
	        	 downid= intent2.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
	        	
	        	 if(queryDownloadStatus() && flag==true){
	        		//启动百度云盘或手机迅雷下载
	         	    Uri url=downloadManager.getUriForDownloadedFile(downid);
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setData(url);
					intent.setDataAndType(url, "application/x-bittorrent");
					context.startActivity(intent);
	        	}else{
	        		flag=true;
	        	}
	       
	        }   
	      
	    }; 

	public void initView() {
		// TODO Auto-generated method stub
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		bt_fragment_arc_menu = (RayMenu)findViewById(R.id.bt_fragment_arc_menu);
		//初始化arc_menu并设置监听
		initArcMenu(bt_fragment_arc_menu, ITEM_DRAWABLES);
				
		networkTips = (TextView)findViewById(R.id.networkTips);
		progressbar = (ProgressBar)findViewById(R.id.progressBar);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(mcontext, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mPullRefreshListView.setMode(Mode.BOTH);
				pullFromUser = true;
				mRefreshType = RefreshType.REFRESH;
				pageNum = 0;
				lastItemTime = getCurrentTime();
				
				fetchDataForPicName();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				mRefreshType = RefreshType.LOAD_MORE;
				fetchDataForPicName();
			}
		});
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				
			}
		});
		
		actualListView = mPullRefreshListView.getRefreshableView();
		picListItems = new ArrayList<SeePic>();
		mAdapter = new SeePicContentAdapter(mContext, picListItems,downloadManager);
		actualListView.setAdapter(mAdapter);
		if(picListItems.size() == 0){
			fetchDataForPicName();
		}
		mPullRefreshListView.setState(State.RELEASE_TO_REFRESH, true);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				MyApplication.getInstance().setCurrentQiangYu(mListItems.get(position-1));
				Intent intent = new Intent();
				intent.setClass(SerchPicContentActivity.this, CommentActivity.class);
				intent.putExtra("data", picListItems.get(position-1));
				startActivity(intent);
			}
		});
		
	}
	

	
	public void fetchDataForUser(){
		setState(LOADING);
		BmobQuery<SeePic> queryForUser = new BmobQuery<SeePic>();
		queryForUser.include("author");
		queryForUser.addWhereEqualTo("author", "");
		queryForUser.order("-createdAt");
		queryForUser.setCachePolicy(CachePolicy.NETWORK_ONLY);
		queryForUser.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		queryForUser.addWhereLessThan("createdAt", date);
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		queryForUser.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		queryForUser.include("author");
		queryForUser.findObjects(this, new FindListener<SeePic>() {
			
			@Override
			public void onSuccess(List<SeePic> list) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"find success."+list.size());
				if(list.size()!=0&&list.get(list.size()-1)!=null){
					if(mRefreshType==RefreshType.REFRESH){
						picListItems.clear();
					}
					if(list.size()<Constant.NUMBERS_PER_PAGE){
						LogUtils.i(TAG,"已加载完所有数据~");
					}
					if(MyApplication.getInstance().getCurrentUser()!=null){
						list = SeePicDatabaseUtil.getInstance(mContext).setFav(list);
					}
					picListItems.addAll(list);
					mAdapter.notifyDataSetChanged();
					
					setState(LOADING_COMPLETED);
					mPullRefreshListView.onRefreshComplete();
				}else{
					ActivityUtil.show(SerchPicContentActivity.this, "暂无更多数据~");
					pageNum--;
					setState(LOADING_COMPLETED);
					mPullRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"find failed."+arg1);
				pageNum--;
				setState(LOADING_FAILED);
				mPullRefreshListView.onRefreshComplete();
			}
		});
	}
	//**********************************************
	public void fetchDataForPicName(){
		setState(LOADING);
		BmobQuery<SeePic> picQuery = new BmobQuery<SeePic>();
		picQuery.addWhereContains("FilmName", serchWord);
		picQuery.order("-createdAt");
		picQuery.setCachePolicy(CachePolicy.NETWORK_ONLY);
		picQuery.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		picQuery.addWhereLessThan("createdAt", date);
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		picQuery.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		picQuery.include("author");
		picQuery.findObjects(this, new FindListener<SeePic>() {
			
			@Override
			public void onSuccess(List<SeePic> list) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"find success."+list.size());
				if(list.size()!=0&&list.get(list.size()-1)!=null){
					if(mRefreshType==RefreshType.REFRESH){
						picListItems.clear();
					}
					if(list.size()<Constant.NUMBERS_PER_PAGE){
						LogUtils.i(TAG,"已加载完所有数据~");
					}
					if(MyApplication.getInstance().getCurrentUser()!=null){
						list = SeePicDatabaseUtil.getInstance(mContext).setFav(list);
					}
					picListItems.addAll(list);
					mAdapter.notifyDataSetChanged();
					
					setState(LOADING_COMPLETED);
					mPullRefreshListView.onRefreshComplete();
				}else{
					ActivityUtil.show(SerchPicContentActivity.this, "暂无更多数据~");
					pageNum--;
					setState(LOADING_COMPLETED);
					mPullRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"find failed."+arg1);
				pageNum--;
				setState(LOADING_FAILED);
				mPullRefreshListView.onRefreshComplete();
			}
		});
	}
	
	
	
	
	
	
	
	//********************************************
	
	
	
	
	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED =3;
	private static final int NORMAL = 4;
	public void setState(int state){
		switch (state) {
		case LOADING:
			if(picListItems.size() == 0){
				mPullRefreshListView.setVisibility(View.GONE);
				progressbar.setVisibility(View.VISIBLE);
			}
			networkTips.setVisibility(View.GONE);
			
			break;
		case LOADING_COMPLETED:
			networkTips.setVisibility(View.GONE);
			progressbar.setVisibility(View.GONE);
			
		    mPullRefreshListView.setVisibility(View.VISIBLE);
		    mPullRefreshListView.setMode(Mode.BOTH);

			
			break;
		case LOADING_FAILED:
			if(picListItems.size()==0){
				mPullRefreshListView.setVisibility(View.VISIBLE);
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
				networkTips.setVisibility(View.VISIBLE);
			}
			progressbar.setVisibility(View.GONE);
			break;
		case NORMAL:
			
			break;
		default:
			break;
		}
	}
	
	//****************************************************************
			private void initArcMenu(RayMenu bt_fragment_arc_menu2, int[] itemDrawables) {
			    final int itemCount = itemDrawables.length;
			    for (int i = 0; i < itemCount; i++) {
			        ImageView item = new ImageView(this);
			        item.setImageResource(itemDrawables[i]);

			        final int position = i;
			        bt_fragment_arc_menu2.addItem(item, new OnClickListener() {

			            @Override
			            public void onClick(View v) {
			            	switch (position) {
							case 0:
								MySearch();
								break;
							case 1:
								actualListView.setSelection(1);
								mAdapter.notifyDataSetInvalidated();//通知adapter数据有变化
								break;
							case 2:
								//当前用户登录
				                BmobUser currentUser = BmobUser.getCurrentUser(mContext);
				                if (currentUser != null) {
				                    // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
				                    String name = currentUser.getUsername();
				                    String email = currentUser.getEmail();
				                    LogUtils.i(TAG,"username:"+name+",email:"+email);
				                    Intent intent = new Intent();
				                    intent.setClass(mContext, EditActivity.class);
				                    startActivity(intent);
				                } else {
				                    // 缓存用户对象为空时， 可打开用户注册界面…
				                    Toast.makeText(mContext, "请先登录。",
				                            Toast.LENGTH_SHORT).show();
//				                  redictToActivity(mContext, RegisterAndLoginActivity.class, null);
				                    Intent intent = new Intent();
				                    intent.setClass(mContext, RegisterAndLoginActivity.class);
				                    startActivity(intent);
				                }
								break;
							case 3:
								OffersManager.getInstance(mContext).showOffersWall();
								break;
							case 4:
								
								break;
							case 5:
								
								break;
						}
			  
			               // Toast.makeText(SerchPicContentActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
			            }

						
			        });
			    }
			}		
		//****************************************
			//*******************************************
		    @SuppressLint("NewApi")
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
			        	Toast.makeText(mcontext, "下载成功,建议点击百度云保存到云端播放",Toast.LENGTH_LONG).show();
			        	result=true;
		                break;   
		            case DownloadManager.STATUS_FAILED:   
		                //清除已下载的内容，重新下载 
			        	 Toast.makeText(mcontext, "网络错误 下载失败,请重试",Toast.LENGTH_LONG).show();
		                Log.v("down", "STATUS_FAILED");  
		                downloadManager.remove(downid);   
		                break;  
		           
		                
		            }   
		        }
				return result;  
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
		            	serchWord = serchWordInput.getText().toString();
		 
		            	//Toast.makeText(mContext,"文件内容："+ serchWord, Toast.LENGTH_SHORT).show();
		            	dialogBuilder.dismiss();
		            	
		            	fetchDataForPicName();
		            	mPullRefreshListView.setRefreshing(true);
		            	mPullRefreshListView.onRefreshComplete();
		            	mPullRefreshListView.setRefreshing(true);

		            	}
		        })
		        .setButton2Click(new View.OnClickListener() {
		            @Override
		            public void onClick(View v) {
		            	dialogBuilder.dismiss();
		            }
		        });
			}
	//***********************************************************
		    @Override
			public void onBackPressed() {
				// TODO Auto-generated method stub
				super.onBackPressed();
				overridePendingTransition (R.anim.open_main, R.anim.close_next);
			}

	
}
