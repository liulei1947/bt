package com.liulei1947.bt.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.liulei1947.bt.MyApplication;
import com.liulei1947.bt.adapter.AIContentAdapter;
import com.liulei1947.bt.adapter.BaseContentAdapter;
import com.liulei1947.bt.base.BaseContentFragment;
import com.liulei1947.bt.entity.QiangYu;

public class MicroVideoFragment extends BaseContentFragment{

	public static MicroVideoFragment newInstance(){
		MicroVideoFragment fragment = new MicroVideoFragment();
		return fragment;
	}
	
	@Override
	public BaseContentAdapter<QiangYu> getAdapter() {
		// TODO Auto-generated method stub
		return new AIContentAdapter(mContext, mListItems,downloadManager);
	}

	@Override
	public void onListItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
//		MyApplication.getInstance().setCurrentQiangYu(mListItems.get(position-1));
		Intent intent = new Intent();
		intent.setClass(getActivity(), CommentActivity.class);
		intent.putExtra("data", mListItems.get(position-1));
		startActivity(intent);
	}
}
