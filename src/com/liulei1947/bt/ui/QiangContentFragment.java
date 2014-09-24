package com.liulei1947.bt.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.youmi.android.offers.OffersManager;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.liulei1947.bt.base.BaseFragment;
import com.liulei1947.bt.db.DatabaseUtil;
import com.liulei1947.bt.entity.QiangYu;
import com.liulei1947.bt.entity.User;
import com.liulei1947.bt.utils.ActivityUtil;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;
import com.liulei1947.bt.view.HeaderViewpager;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-23s
 * TODO
 */

public class QiangContentFragment extends BaseFragment{
	
	private View contentView ;
	private int currentIndex ;
	private int pageNum;
	private String lastItemTime;//当前列表结尾的条目的创建时间，
	HeaderViewpager addheaderview;
	private ArrayList<QiangYu> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private AIContentAdapter mAdapter;
	private ListView actualListView;
	RayMenu bt_fragment_arc_menu;
	private TextView networkTips;
	private ProgressBar progressbar;
	private boolean pullFromUser;
	static BaseFragment Qiangfragment;
    private static final int[] ITEM_DRAWABLES = { R.drawable.finding, R.drawable.up,
	R.drawable.reloading};//,R.drawable.arc_icon_wallpaper};

	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	
	public static BaseFragment newInstance(int index){
		Qiangfragment = new QiangContentFragment();
		Bundle args = new Bundle();
		args.putInt("page",index);
		Qiangfragment.setArguments(args);
		return Qiangfragment;
	}
	
	private String getCurrentTime(){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String times = formatter.format(new Date(System.currentTimeMillis()));
	     return times;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentIndex = getArguments().getInt("page");
		pageNum = 0;
		lastItemTime = getCurrentTime();
		LogUtils.i(TAG,"curent time:"+lastItemTime);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		contentView = inflater.inflate(R.layout.fragment_qiangcontent, null);
		mPullRefreshListView = (PullToRefreshListView)contentView
				.findViewById(R.id.pull_refresh_list);
		
		bt_fragment_arc_menu = (RayMenu) contentView.findViewById(R.id.bt_fragment_arc_menu);
		//初始化arc_menu并设置监听
						initArcMenu(bt_fragment_arc_menu, ITEM_DRAWABLES);
				
		networkTips = (TextView)contentView.findViewById(R.id.networkTips);
		progressbar = (ProgressBar)contentView.findViewById(R.id.progressBar);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mPullRefreshListView.setMode(Mode.BOTH);
				pullFromUser = true;
				mRefreshType = RefreshType.REFRESH;
				pageNum = 0;
				lastItemTime = getCurrentTime();
				addheaderview.Refresh();
				fetchData();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				mRefreshType = RefreshType.LOAD_MORE;
				fetchData();
			}
		});
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				
			}
		});
		
		actualListView = mPullRefreshListView.getRefreshableView();
		mListItems = new ArrayList<QiangYu>();
		mAdapter = new AIContentAdapter(mContext, mListItems,downloadManager);
		actualListView.setAdapter(mAdapter);
		//添加顶部广告轮播
		addheaderview=new HeaderViewpager(mContext, actualListView);
		addheaderview.addHeadView();
		addheaderview.startchange();
		if(mListItems.size() == 0){
			fetchData();
		}
		mPullRefreshListView.setState(State.RELEASE_TO_REFRESH, true);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				MyApplication.getInstance().setCurrentQiangYu(mListItems.get(position-1));
				Intent intent = new Intent();
				intent.setClass(getActivity(), CommentActivity.class);
				intent.putExtra("data", mListItems.get(position-2));
				startActivity(intent);
				getActivity().overridePendingTransition (R.anim.open_next, R.anim.close_main);

			}
		});
		return contentView;
	}
	
	public void fetchData(){
		setState(LOADING);
		BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
		
     	query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
     	query.order("-createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		query.include("author");
		query.findObjects(getActivity(), new FindListener<QiangYu>() {
			
			@Override
			public void onSuccess(List<QiangYu> list) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"find success."+list.size());
				if(list.size()!=0&&list.get(list.size()-1)!=null){
					if(mRefreshType==RefreshType.REFRESH){
						mListItems.clear();
					}
					if(list.size()<Constant.NUMBERS_PER_PAGE){
						LogUtils.i(TAG,"已加载完所有数据~");
					}
					if(MyApplication.getInstance().getCurrentUser()!=null){
						list = DatabaseUtil.getInstance(mContext).setFav(list);
					}
					mListItems.addAll(list);
					mAdapter.notifyDataSetChanged();
					
					setState(LOADING_COMPLETED);
					mPullRefreshListView.onRefreshComplete();
				}else{
					ActivityUtil.show(getActivity(), "暂无更多数据~");
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
	
	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED =3;
	private static final int NORMAL = 4;
	public void setState(int state){
		switch (state) {
		case LOADING:
			if(mListItems.size() == 0){
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
			if(mListItems.size()==0){
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
			        ImageView item = new ImageView(getActivity());
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
								 //回到ListView顶部
			
								actualListView.setSelection(1);
								mAdapter.notifyDataSetInvalidated();//通知adapter数据有变化
								break;
							case 2:
								OffersManager.getInstance(mContext).showOffersWall();
								
								
								break;
							case 3:
								
					          	//setMyWallpaper();
								break;
							case 4:
								
								break;
							case 5:
								
								break;
						}
			  
			               // Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
			            }

						
			        });
			    }
			}		
		//****************************************

			protected void MySearch() {
				// TODO Auto-generated method stub
		        final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(mContext);
		        dialogBuilder
                .withTitle("种子搜索")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                //.withDividerColor("#11000000")                              //def
                //.withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
                //.withMessageColor("#FFFFFF")                                //def
                .withIcon(R.drawable.yunpan)
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(200)                                          //def
                .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
                .withButton1Text("搜索")                                      //def gone
               // .withButton2Text("取消")                                  //def gone
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
        				getActivity().overridePendingTransition (R.anim.open_next, R.anim.close_main);

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
