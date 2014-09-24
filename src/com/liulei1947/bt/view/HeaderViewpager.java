package com.liulei1947.bt.view;



import java.util.List;

import u.aly.T;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;
import com.liulei1947.bt.MyApplication;
import com.liulei1947.bt.R;
import com.liulei1947.bt.entity.QiangYu;
import com.liulei1947.bt.entity.SeePic;
import com.liulei1947.bt.ui.CommentActivity;
import com.liulei1947.bt.utils.ActivityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * User: special
 * Date: 13-12-22
 * Mail: specialcyci@gmail.com
 */
@SuppressLint("NewApi")
public class HeaderViewpager{
	int pagerPosition;
	int imageCount;
	int Count;
	int showpagercount=5;
	String title;
	String htmlUrl;
	String videoUrl;
	String tag;
	TextView title2;
	String englishTitle="junxun%20";
    String imageUrls;
	ViewPager mJazzy;
    List<QiangYu> QiangYuHeaderData;
    LayoutInflater newsInflater;
    HeaderImagePagerAdapter headerImagePagerAdapter;
    DisplayImageOptions options;
    ListView  listView;
    Context mcontext;
    
    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    final Handler handler=new Handler(){  
        public void handleMessage(android.os.Message msg) {  
        	if(msg.what==0x123){	
        		controlViewPagerShow();
        		
        	}
        	
        	
        };  
    };
    
    //*************************************************************************
    public HeaderViewpager(Context context,ListView listView) {
    	this.listView=listView;
    	this.mcontext=context;
        //***********************************************
	      //***********************************************
	    
    }
 
    public void Refresh(){
    	queryHeaderdata();
    }
  //****************************************************************
  		//**addView into listview
  		public void addHeadView() {
  			
  			FrameLayout pagerLayout = new FrameLayout(mcontext);
  			mJazzy =new  JazzyViewPager(mcontext, null);
  			
  	        DisplayMetrics dm = new DisplayMetrics();
  	        ((Activity) mcontext).getWindowManager().getDefaultDisplay().getMetrics(dm);
  	        //根据屏幕大小设置headerSize
  	        mJazzy.setLayoutParams(new LayoutParams(dm.widthPixels, dm.heightPixels * 2 / 5));
  	        headerImagePagerAdapter=new HeaderImagePagerAdapter(mcontext,QiangYuHeaderData);
  	        mJazzy.setAdapter(headerImagePagerAdapter);
  			pagerLayout.addView(mJazzy);
 			CirclePageIndicator circlePageIndicator = new CirclePageIndicator(mcontext);
 			circlePageIndicator.setViewPager(mJazzy);
			circlePageIndicator.setPadding(20,dm.heightPixels * 2 / 5-50, 20, 50);
 			pagerLayout.addView(circlePageIndicator);
  			listView.addHeaderView(pagerLayout);
  			queryHeaderdata();
  			
  		}
  		 //***********************************************
  	public void startchange(){	
  
        new Thread(){  
	          public void run() {  
	                while(true){  
	                   if(true){  
						handler.sendEmptyMessage(0x123);  
						try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                    }  
	                }  
	            };  
	       }.start(); 
  	}
	      //***********************************************
	
  		

  //**************************************************
  	public void onImageGridClick(int position) {

  		Intent intent = null;
		try {
			QiangYu pd = QiangYuHeaderData.get(position);
			intent = new Intent();
			intent.putExtra("data", pd);
			intent.setClass(mcontext, CommentActivity.class);
			mcontext.startActivity(intent);
			((Activity) mcontext).overridePendingTransition (R.anim.open_next, R.anim.close_main);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			ActivityUtil.show(mcontext,"网络错误");
			e.printStackTrace();
		}
		
	
	}
	
  	
    //******************************************************************************
 
	public void queryHeaderdata(){

		BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
		query.setLimit(15);
		query.addWhereEqualTo("isHeaderShow", true);
		query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		query.order("-createdAt");// 按时间排序
		query.findObjects(mcontext, new FindListener<QiangYu>() {
		    @Override
		    public void onSuccess(List<QiangYu> showpicturesinfo) {
		        // TODO Auto-generated method stub
		    	
		    	QiangYuHeaderData=showpicturesinfo;
		    	headerImagePagerAdapter.notifyDataSetChanged();
		    	headerImagePagerAdapter=new HeaderImagePagerAdapter(mcontext,showpicturesinfo);
		    	mJazzy.setAdapter(headerImagePagerAdapter);
		    	
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	
		  
		    }
		});
		
		}
	
	 //*****************************************************************************************
	
	private class HeaderImagePagerAdapter extends PagerAdapter {
			
			String url;
			
			private LayoutInflater inflater;
			List<QiangYu> info;
			public HeaderImagePagerAdapter(Context context, List<QiangYu> info2) {
				this.info=info2;
				inflater = ((Activity) mcontext).getLayoutInflater();
				//inflater=newsInflater;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
			}

			@Override
			public int getCount() {
				try {
					Count=info.size();
				} catch (Exception e) {
					// TODO Auto-generated catch block
				
					Count=5;
				}
				return Count;
			}

			@Override
			public Object instantiateItem(ViewGroup view, final int position) {
				View imageLayout = inflater.inflate(R.layout.list_header_item_pager_image, view, false);
				assert imageLayout != null;
				ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
				TextView title2=(TextView)imageLayout.findViewById(R.id.showpaper_title);
				TextView time=(TextView)imageLayout.findViewById(R.id.showpaper_time);
				try {
					title2.setText(info.get(position).getFilmName());
					time.setText(info.get(position).getCreatedAt());
					url = info.get(position).getindexImageUrl().getFileUrl();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				imageLayout.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onImageGridClick(position);
					}
					
				});
				final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
				ImageLoader.getInstance().displayImage(url, imageView, MyApplication.getInstance().getOptions(R.drawable.bg_pic_loading), new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						spinner.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
						}
						Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();

						spinner.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						spinner.setVisibility(View.GONE);
					}
				});

				view.addView(imageLayout, 0);
				return imageLayout;
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view.equals(object);
			}

			@Override
			public void restoreState(Parcelable state, ClassLoader loader) {
			}

			@Override
			public Parcelable saveState() {
				return null;
			}
			
			
			
			
			
			
			
			
		}



		//************************************************************
	    /**  
	     * 控制ViewPager轮播
	    */  
	    public void controlViewPagerShow(){
	    	//JazzyViewPager切换效果
  	    	
	    	int current = mJazzy.getCurrentItem();
		    if(current==Count-1){
		    
		    	mJazzy.setCurrentItem(0);
	    	}else{
	    	
	    		mJazzy.setCurrentItem(current+1);
	    		
	    	}
	    }  
    
    
}
