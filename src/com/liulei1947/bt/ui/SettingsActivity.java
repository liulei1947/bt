package com.liulei1947.bt.ui;


import android.view.View;

import com.liulei1947.bt.R;
import com.liulei1947.bt.base.BaseFragment;
import com.liulei1947.bt.base.BaseHomeActivity;
import com.liulei1947.bt.ui.PersonalFragment.IProgressControllor;

public class SettingsActivity extends BaseHomeActivity implements IProgressControllor{

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return getString(R.string.settings_title);
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onHomeActionClick() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected BaseFragment getFragment() {
		// TODO Auto-generated method stub
		return SettingsFragment.newInstance();
	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(RESULT_OK);
		finish();
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
