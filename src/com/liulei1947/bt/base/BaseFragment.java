package com.liulei1947.bt.base;


import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.Sputil;
import com.umeng.analytics.MobclickAgent;

import android.R;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-23
 * TODO
 */

public abstract class BaseFragment extends Fragment{
	public static String TAG;
	protected Context mContext;
	protected Sputil sputil;
	protected DownloadManager downloadManager;  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		mContext = getActivity();
		downloadManager = (DownloadManager)getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);  
		if(null == sputil){
			sputil = new Sputil(mContext, Constant.PRE_NAME);
		}
		
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}
}
