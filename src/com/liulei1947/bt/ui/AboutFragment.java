package com.liulei1947.bt.ui;

import com.liulei1947.bt.R;
import com.liulei1947.bt.base.BaseHomeFragment;
import com.liulei1947.bt.utils.ActivityUtil;
import com.liulei1947.bt.view.ChangeLogDialog;
import com.umeng.fb.FeedbackAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class AboutFragment extends BaseHomeFragment{
	private TextView versionName;
	TextView sendFeedback ;
	TextView appweb;
	TextView rightOfPrivacy;
	TextView change_log;
	static Context mcontext;
	public static AboutFragment newInstance(){
		AboutFragment fragment = new AboutFragment();
		return fragment;
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_about;
	}

	@Override
	protected void findViews(View view) {
		// TODO Auto-generated method stub
		mcontext=getActivity();
		versionName = (TextView)view.findViewById(R.id.version_name);
		sendFeedback = (TextView)view.findViewById(R.id.sendFeedback);
		appweb = (TextView)view.findViewById(R.id.appWebSide);
		rightOfPrivacy = (TextView)view.findViewById(R.id.rightOfPrivacy);
		change_log = (TextView)view.findViewById(R.id.change_log);

	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		versionName.setText("当前版本："+ActivityUtil.getVersionName(mContext));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		sendFeedback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FeedbackAgent agent = new FeedbackAgent(getActivity());
        	    agent.startFeedbackActivity();

        	    getActivity().overridePendingTransition (R.anim.open_next, R.anim.close_main);

			}
			
		});
		appweb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
        	    Intent intent=new Intent();
				intent.setData(Uri.parse("http://utorrent.bmob.cn/"));
				intent.setAction(Intent.ACTION_VIEW);
				mContext.startActivity(intent);

        	    getActivity().overridePendingTransition (R.anim.open_next, R.anim.close_main);

			}
			
		});
		
	
		rightOfPrivacy.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setData(Uri.parse("http://utorrent.bmob.cn/"));
				intent.setAction(Intent.ACTION_VIEW);
				mContext.startActivity(intent);

				getActivity().overridePendingTransition (R.anim.open_next, R.anim.close_main);

			}
			
		});
		change_log.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Launch change log dialog
				final ChangeLogDialog changeLogDialog = new ChangeLogDialog(mContext);
				changeLogDialog.show();

			}
			
		});
	
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		
	}



}
