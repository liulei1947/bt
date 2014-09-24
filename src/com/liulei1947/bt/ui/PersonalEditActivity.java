package com.liulei1947.bt.ui;

import android.os.Bundle;

import com.liulei1947.bt.base.BaseFragment;
import com.liulei1947.bt.base.BaseHomeActivity;
import com.liulei1947.bt.entity.QiangYu;
import com.liulei1947.bt.utils.ActivityUtil;
@Deprecated
public class PersonalEditActivity extends BaseHomeActivity{

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "修改个人信息";
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
		return PersonalEditFragment.newInstance();
	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub
		
	}

}
