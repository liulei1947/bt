/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package astuetz.viewpager.extensions.sample.help;


import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.liulei1947.bt.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class HelpFragment1 extends Fragment{

	private static final String ARG_POSITION = "position";

	private int position;
	TextView title1;
	TextView title2;
	TextView title3;
	ImageView image1;
	ImageView image2;
	ImageView image3;
    private View parentView;
    LayoutInflater newsInflater;
    DisplayMetrics dm;
    int h ;
    int w;
    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    
    //*************************************************************************
	public static HelpFragment1 newInstance(int position) {
		
		HelpFragment1 f = new HelpFragment1();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
		//获取屏幕像素相关信息
         dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
         h = dm.heightPixels/2;
         w=dm.widthPixels/2;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.activity_help1, container, false);
		newsInflater=inflater;
		findtextView();
		findImageViews();
		return parentView;
	}

	public void findtextView(){
		
		title1 = (TextView) parentView.findViewById(R.id.title1);
		  animate(title1).setDuration(2000).alpha(1);
		  title2 = (TextView) parentView.findViewById(R.id.title2);
		  animate(title2).setDuration(2500).alpha(1);
		  title3 = (TextView) parentView.findViewById(R.id.title3);
		  animate(title3).setDuration(2500).alpha(1);
		 
		
	}
	public void findImageViews(){
			 
		image2 = (ImageView) parentView.findViewById(R.id.imageView2);
		 animate(image2).setDuration(2000).alpha(1);
	}
	    //******************
}