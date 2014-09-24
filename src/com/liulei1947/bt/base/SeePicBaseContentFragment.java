package com.liulei1947.bt.base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.youmi.android.offers.OffersManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.capricorn.RayMenu;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidynast.customdialog.dialog.Effectstype;
import com.lidynast.customdialog.dialog.NiftyDialogBuilder;
import com.liulei1947.bt.R;
import com.liulei1947.bt.adapter.BaseContentAdapter;
import com.liulei1947.bt.entity.SeePic;
import com.liulei1947.bt.entity.User;
import com.liulei1947.bt.ui.EditActivity;
import com.liulei1947.bt.ui.RegisterAndLoginActivity;
import com.liulei1947.bt.ui.SerchPicContentActivity;
import com.liulei1947.bt.ui.SerchTorrentContentActivity;
import com.liulei1947.bt.ui.QiangContentFragment.RefreshType;
import com.liulei1947.bt.utils.ActivityUtil;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;
import com.liulei1947.bt.view.HeaderViewpager;
import com.liulei1947.bt.view.SeePicHeaderViewpager;

public abstract class SeePicBaseContentFragment extends BaseFragment{
	
	private int pageNum;
	private String lastItemTime;
	RayMenu seepic_fragment_arc_menu;
	private View contentView;
	protected ArrayList<SeePic> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private BaseContentAdapter<SeePic> mAdapter;
	private ListView actualListView;
	private static final int[] ITEM_DRAWABLES = { R.drawable.finding, R.drawable.up,
		R.drawable.edit,R.drawable.reloading};
	SeePicHeaderViewpager addheaderview;
	private TextView networkTips;
	private ProgressBar progressbar;
	private boolean pullFromUser;
	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pageNum = 0;
		lastItemTime = getCurrentTime();
	}
	
	private String getCurrentTime(){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String times = formatter.format(new Date(System.currentTimeMillis()));
	     return times;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		contentView = inflater.inflate(R.layout.fragment_seepic_content,container,false);
		mPullRefreshListView = (PullToRefreshListView)contentView
				.findViewById(R.id.pull_refresh_list);
		seepic_fragment_arc_menu = (RayMenu) contentView.findViewById(R.id.bt_fragment_arc_menu);
		//初始化arc_menu并设置监听
						initArcMenu(seepic_fragment_arc_menu, ITEM_DRAWABLES);
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
		mListItems = new ArrayList<SeePic>();
		mAdapter = getAdapter();
		actualListView.setAdapter(mAdapter);
		//添加顶部广告轮播
		 addheaderview=new SeePicHeaderViewpager(mContext, actualListView);
				addheaderview.addHeadView();
				addheaderview.startchange();
		if(mListItems.size() == 0){
			fetchData();
		}
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				onListItemClick(parent, view, position, id);
			}
		});
		return contentView;
	}
	
	public void fetchData(){
		setState(LOADING);
		//User user = BmobUser.getCurrentUser(mContext, User.class);
		BmobQuery<SeePic> query = new BmobQuery<SeePic>();
		//query.addWhereRelatedTo("favorite", new BmobPointer(user));
		query.order("-createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		query.include("author");
		query.findObjects(getActivity(), new FindListener<SeePic>() {
			
			@Override
			public void onSuccess(List<SeePic> list) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"find success."+list.size());
				if(list.size()!=0&&list.get(list.size()-1)!=null){
					if(mRefreshType==RefreshType.REFRESH){
						mListItems.clear();
					}
					if(list.size()<Constant.NUMBERS_PER_PAGE){
						ActivityUtil.show(getActivity(), "已加载完所有数据~");
					}
					mListItems.addAll(list);
					mAdapter.notifyDataSetChanged();
					
					LogUtils.i(TAG,"DD"+(mListItems.get(mListItems.size()-1)==null));
					setState(LOADING_COMPLETED);
					mPullRefreshListView.onRefreshComplete();
				}else{
					ActivityUtil.show(getActivity(), "暂无更多数据~");
					if(list.size()==0&&mListItems.size()==0){
						
						networkTips.setText("暂无收藏。快去首页收藏几个把~");
						setState(LOADING_FAILED);
						pageNum--;
						mPullRefreshListView.onRefreshComplete();
						
						LogUtils.i(TAG,"SIZE:"+list.size()+"ssssize"+mListItems.size());
						return;
					}
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
//		                  redictToActivity(mContext, RegisterAndLoginActivity.class, null);
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
	  
	                //Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
	            }

				
	        });
	    }
	}		
//****************************************
	//***********************************************************

	protected void MySearch() {
		// TODO Auto-generated method stub
        final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(mContext);
        dialogBuilder
        .withTitle("搜索图片")                                  //.withTitle(null)  no title
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
            	Intent intent = new Intent(mContext, SerchPicContentActivity.class);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//*****************************
	
	
	public abstract BaseContentAdapter<SeePic> getAdapter();
	public abstract void onListItemClick(AdapterView<?> parent, View view,int position, long id);
}
