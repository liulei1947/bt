package com.liulei1947.bt.ui;


import net.youmi.android.offers.OffersManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;

import com.liulei1947.bt.R;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;



/**
 * 导航栏
 * 
 * @author yl
 * 
 */
public class NaviFragment extends Fragment implements OnClickListener
         {

    private static final int HOMEFRAGMENT = 0;
    private static final int SETTINGSFRAGMENT = 1;
    private static final int FEEDBACKFRAGMENT = 2;
    private static final int INTROFRAGMENT = 3;
    private static final int ABOUTFRAGMENT = 4;
    private static final int DINNERFRAGMENT = 5;
    private static final int SEEPICFRAGMENT = 6;
    private static final int MICROVIDEOFRAGMENT = 7;
    private TextView topbar_title;
    private MainActivity mActivity;
    private TextView navi_home;
    private TextView navi_settings;
    private TextView navi_feedback;
    private TextView navi_intro;
    private TextView navi_about;
    private TextView navi_seePic;
    private TextView navi_microVideo;
    Mainfragment mMainFMainfragment;
    SeePicFragment seePicfragment;
    MicroVideoFragment microVideoFragment;
    SettingsFragment mSettingsfragment;
    AboutFragment mAboutFragment;
    FavFragment mFavFragment;

    private FragmentManager fragmentManager;

    public NaviFragment(){
    	
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View rootView;// 缓存Fragment view

    /**
     * 显示左边导航栏fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_navi, null);
        }

        fragmentManager = getFragmentManager();

        init();

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = (MainActivity) activity;
    	topbar_title = (TextView)mActivity.findViewById(R.id.topbar_title);

        super.onAttach(activity);
    }

    /**
     * 初始化，设置点击事件
     */
    private void init() {
        navi_home = (TextView) rootView.findViewById(R.id.tv_navi_main);
        navi_feedback = (TextView) rootView.findViewById(R.id.tv_navi_feedback);
        navi_settings = (TextView) rootView.findViewById(R.id.tv_navi_settings);
        navi_seePic = (TextView) rootView.findViewById(R.id.tv_navi_seePic);
        //navi_microVideo = (TextView) rootView.findViewById(R.id.tv_navi_microVideo);
        navi_intro = (TextView) rootView.findViewById(R.id.tv_navi_intro);
        navi_about = (TextView) rootView.findViewById(R.id.tv_navi_about);

        navi_home.setSelected(true);// 默认选中菜单
        navi_feedback.setSelected(false);
        navi_settings.setSelected(false);
        navi_intro.setSelected(false);
        navi_about.setSelected(false);
        navi_seePic.setSelected(false);
       // navi_microVideo.setSelected(false);
        OnTabSelected(HOMEFRAGMENT);

        navi_home.setOnClickListener(this);
        navi_seePic.setOnClickListener(this);
        //navi_microVideo.setOnClickListener(this);
        navi_feedback.setOnClickListener(this);
        navi_settings.setOnClickListener(this);
        navi_intro.setOnClickListener(this);
        navi_about.setOnClickListener(this);
        
    }

    /**
     * 点击导航栏切换 同时更改标题
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.tv_navi_main:
        	
            navi_home.setSelected(true);// 菜单设置为被选中状态，其余设置为非选中状态
            navi_feedback.setSelected(false);
            navi_settings.setSelected(false);
            navi_intro.setSelected(false);
            navi_about.setSelected(false);
            navi_seePic.setSelected(false);
           // navi_microVideo.setSelected(false);
            OnTabSelected(HOMEFRAGMENT);
            break;
        case R.id.tv_navi_feedback:

            navi_home.setSelected(false);
            navi_feedback.setSelected(true);
            navi_settings.setSelected(false);
            navi_intro.setSelected(false);
            navi_about.setSelected(false);
            navi_seePic.setSelected(false);
           // navi_microVideo.setSelected(false);
            OnTabSelected(FEEDBACKFRAGMENT);
            break;
        case R.id.tv_navi_seePic:
       	 navi_home.setSelected(false);
            navi_feedback.setSelected(false);
            navi_settings.setSelected(false);
            navi_intro.setSelected(false);
            navi_about.setSelected(false);
            navi_seePic.setSelected(true);
            //navi_microVideo.setSelected(false);
            OnTabSelected(SEEPICFRAGMENT);
       	break;
//        case R.id.tv_navi_microVideo:
//       	 navi_home.setSelected(false);
//            navi_feedback.setSelected(false);
//            navi_settings.setSelected(false);
//            navi_intro.setSelected(false);
//            navi_about.setSelected(false);
//            navi_seePic.setSelected(false);
//            navi_microVideo.setSelected(true);
//            OnTabSelected(MICROVIDEOFRAGMENT);
//       	break;
        case R.id.tv_navi_settings:// 

            navi_home.setSelected(false);
            navi_feedback.setSelected(false);
            navi_settings.setSelected(false);
            navi_intro.setSelected(false);
            navi_about.setSelected(false);
            navi_seePic.setSelected(false);
            //navi_microVideo.setSelected(false);
            OnTabSelected(SETTINGSFRAGMENT);
            break;
        case R.id.tv_navi_intro:

            navi_home.setSelected(false);
            navi_feedback.setSelected(false);
            navi_settings.setSelected(false);
            navi_intro.setSelected(true);
            navi_about.setSelected(false);
            navi_seePic.setSelected(false);
           // navi_microVideo.setSelected(false);
            OnTabSelected(INTROFRAGMENT);
            break;
        case R.id.tv_navi_about:
        	 navi_home.setSelected(false);
             navi_feedback.setSelected(false);
             navi_settings.setSelected(false);
             navi_intro.setSelected(false);
             navi_about.setSelected(true);
             navi_seePic.setSelected(false);
            // navi_microVideo.setSelected(false);
             OnTabSelected(ABOUTFRAGMENT);
        	break;
        }
        mActivity.getSlidingMenu().toggle();
    }
    
    //选中导航中对应的tab选项
    public void OnTabSelected(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        switch (index) {
        case HOMEFRAGMENT:
        	hideFragments(transaction);
          if (null == mMainFMainfragment) {
              mMainFMainfragment = new Mainfragment();
              transaction.add(R.id.center, mMainFMainfragment);
              topbar_title.setText("发现");

          } else {
              transaction.show(mMainFMainfragment);
              topbar_title.setText("发现");
          }
            break;
        case SEEPICFRAGMENT:
        	hideFragments(transaction);
        	if(null == seePicfragment){
        		seePicfragment = new SeePicFragment();
        		transaction.add(R.id.center, seePicfragment);
        		topbar_title.setText("看图说话");
        	}else{
        		transaction.show(seePicfragment);
        		topbar_title.setText("看图说话");
        	}
              break;
        case MICROVIDEOFRAGMENT:
        	
        	hideFragments(transaction);
        	if(null == microVideoFragment){
        		microVideoFragment = new MicroVideoFragment();
        		transaction.add(R.id.center, microVideoFragment);
        		topbar_title.setText("微电影");
        	}else{
        		transaction.show(microVideoFragment);
        		topbar_title.setText("微电影");
        	}
              break;
        case SETTINGSFRAGMENT: 

        	hideFragments(transaction);
        	if (null == mSettingsfragment) {
        		mSettingsfragment = new SettingsFragment();
                transaction.add(R.id.center, mSettingsfragment);
                topbar_title.setText("用户设置");
            } else {
                transaction.show(mSettingsfragment);
                topbar_title.setText("用户设置");
            }
            break;
        case FEEDBACKFRAGMENT:

//        	FeedbackAgent agent = new FeedbackAgent(mActivity);
//			agent.startFeedbackActivity();
        	BmobUser currentUser = BmobUser.getCurrentUser(mActivity);
			if (currentUser != null) {
				// 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
				hideFragments(transaction);
	        	if(null == mFavFragment){
	        		mFavFragment = new FavFragment();
	        		transaction.add(R.id.center, mFavFragment);
	        		topbar_title.setText("我的收藏");
	        	}else{
	        		transaction.show(mFavFragment);
	        		topbar_title.setText("我的收藏");
	        	}
			} else {
				// 缓存用户对象为空时， 可打开用户注册界面…
				Toast.makeText(mActivity, "请先登录。",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(mActivity, RegisterAndLoginActivity.class);
				startActivity(intent);
			}
            break;
        case INTROFRAGMENT:

        	OffersManager.getInstance(mActivity).showOffersWall();
            break;
        case ABOUTFRAGMENT:
        	hideFragments(transaction);
        	if(null == mAboutFragment){
        		mAboutFragment = new AboutFragment();
        		transaction.add(R.id.center, mAboutFragment);
        		topbar_title.setText("关于");
        	}else{
        		transaction.show(mAboutFragment);
        		topbar_title.setText("关于");
        	}
        	break;
        }
        transaction.commit();
    }
//****************************************
    
    
    
   
    /**
     * 将所有fragment都置为隐藏状态
     * 
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
    	if(mMainFMainfragment!=null){
    		transaction.hide(mMainFMainfragment);
    	}
    	if(mSettingsfragment!=null){
    		transaction.hide(mSettingsfragment);
    	}
    	if(seePicfragment!=null){
    		transaction.hide(seePicfragment);
    	}
    	
    	if(microVideoFragment!=null){
    		transaction.hide(microVideoFragment);
    	}
    	
    	if(mAboutFragment!=null){
    		transaction.hide(mAboutFragment);
    	}
    	if(mFavFragment!=null){
    		transaction.hide(mFavFragment);
    	}
    }

}

