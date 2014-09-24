package com.liulei1947.bt.ui;

import android.view.View;

import com.liulei1947.bt.R;
import com.liulei1947.bt.base.BaseFragment;
import com.liulei1947.bt.base.BaseHomeActivity;
import com.liulei1947.bt.ui.PersonalFragment.IProgressControllor;
import com.markupartist.android.widget.ActionBar.Action;

public class PersonalActivity extends BaseHomeActivity implements IProgressControllor{

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "个人中心";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onHomeActionClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected BaseFragment getFragment() {
		// TODO Auto-generated method stub
		return PersonalFragment.newInstance();
	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void showActionBarProgress(){
		actionBar.setProgressBarVisibility(View.VISIBLE);
	}
	
	@Override
	public void hideActionBarProgress(){
		actionBar.setProgressBarVisibility(View.GONE);
	}
}
