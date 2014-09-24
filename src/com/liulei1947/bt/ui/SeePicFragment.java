package com.liulei1947.bt.ui;

import net.youmi.android.offers.OffersManager;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;

import com.capricorn.RayMenu;
import com.lidynast.customdialog.dialog.Effectstype;
import com.lidynast.customdialog.dialog.NiftyDialogBuilder;
import com.liulei1947.bt.MyApplication;
import com.liulei1947.bt.R;
import com.liulei1947.bt.adapter.AIContentAdapter;
import com.liulei1947.bt.adapter.BaseContentAdapter;
import com.liulei1947.bt.adapter.SeePicContentAdapter;
import com.liulei1947.bt.base.BaseContentFragment;
import com.liulei1947.bt.base.SeePicBaseContentFragment;
import com.liulei1947.bt.entity.QiangYu;
import com.liulei1947.bt.entity.SeePic;
import com.liulei1947.bt.utils.Constant;
import com.liulei1947.bt.utils.LogUtils;

public class SeePicFragment extends SeePicBaseContentFragment{
	
	public static SeePicFragment newInstance(){
		SeePicFragment fragment = new SeePicFragment();
	
		return fragment;
	}
	
	@Override
	public BaseContentAdapter<SeePic> getAdapter() {
		// TODO Auto-generated method stub

	
		return new SeePicContentAdapter(mContext, mListItems,downloadManager);
	}

	@Override
	public void onListItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
//		MyApplication.getInstance().setCurrentQiangYu(mListItems.get(position-1));
		Intent intent = new Intent();
		intent.setClass(getActivity(), SeePicCommentActivity.class);
		intent.putExtra("data", mListItems.get(position-2));
		startActivity(intent);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
}
