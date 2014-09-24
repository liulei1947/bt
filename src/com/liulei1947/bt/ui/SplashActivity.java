package com.liulei1947.bt.ui;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import cn.bmob.v3.Bmob;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import com.liulei1947.bt.R;
import com.liulei1947.bt.base.BaseActivity;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;
import com.liulei1947.bt.utils.UmengStat;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import astuetz.viewpager.extensions.sample.help.HelpFragmentActivity;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-21
 * TODO 闪屏界面，根据指定时间进行跳转
 * 		在activity_splash.xml中加入background属性并传入图片资源ID即可
 */
public class SplashActivity extends BaseActivity {
	private SharedPreferences appprefs;  
	private static final long DELAY_TIME = 2000L;
	 private static final String showHelpPager = "showHelpPager";  
	TextView appname;
	TextView copyright;
	 RelativeLayout RelativeLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//Bmob SDK初始化--只需要这一段代码即可完成初始化
		Bmob.initialize(this,Constant.BMOB_APP_ID);		
		LogUtils.i(TAG,TAG + " Launched ！");
		//获取屏幕像素相关信息
        DisplayMetrics dm = new DisplayMetrics();
      
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int h = dm.heightPixels/3;
        appprefs = PreferenceManager.getDefaultSharedPreferences(this);   
        
        //int w = dm.widthPixels;
		
		
	  RelativeLayout RelativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
	   appname=(TextView)findViewById(R.id.logo_name);
	   copyright=(TextView)findViewById(R.id.copyright);
	   animate(appname).setDuration(DELAY_TIME).x(0).y(h);
	   //faldin
		animate(appname).setDuration(DELAY_TIME).alpha(1);
		animate(copyright).setDuration(DELAY_TIME).alpha(1);
		MobclickAgent.openActivityDurationTrack(UmengStat.IS_OPEN_ACTIVITY_AUTO_STAT);
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		redirectByTime();		
		if(sputil.getValue("isPushOn", true)){
			PushAgent mPushAgent = PushAgent.getInstance(mContext);
			mPushAgent.enable();
			LogUtils.i(TAG,"device_token:"+UmengRegistrar.getRegistrationId(mContext));
		}else{
			PushAgent mPushAgent = PushAgent.getInstance(mContext);
			mPushAgent.disable();
		}
		
		AdManager.getInstance(mContext).init("bc69a03a8887a975", "294a0605a4cfe199", false);
		OffersManager.getInstance(mContext);
	}
	
	/**
	 * 根据时间进行页面跳转
	 */
	private void redirectByTime() {
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean isshow = appprefs.getBoolean(showHelpPager,false);
				if(isshow){
				redictToActivity(SplashActivity.this, MainActivity.class, null);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

				finish();
				}else{
					redictToActivity(SplashActivity.this, HelpFragmentActivity.class, null);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					finish();
				}
			}
		}, DELAY_TIME);
	}

}
