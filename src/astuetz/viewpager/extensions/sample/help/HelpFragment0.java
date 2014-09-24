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
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import android.annotation.SuppressLint;
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

public class HelpFragment0 extends Fragment{

	private static final String ARG_POSITION = "position";

	private int position;
	
	TextView title1;
	TextView title2;
	TextView title3;
	ImageView image21;
	ImageView image22;
	ImageView image23;
	ImageView image31;
	ImageView image32;
	ImageView image33;
	ImageView image41;
	ImageView image42;
	ImageView image43;
	ImageView image11;
	ImageView image12;
	ImageView image13;
    private View parentView;
    LayoutInflater newsInflater;
   
    DisplayMetrics dm;
    int h ;
    int w=0;
   
    //*************************************************************************
	public static HelpFragment0 newInstance(int position) {
		
		HelpFragment0 f = new HelpFragment0();
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
         h = dm.heightPixels/3;
         w=dm.widthPixels/2;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.activity_help0, container, false);
		newsInflater=inflater;
		//ImageView zoom = (ImageView) parentView.findViewById(R.id.zoom);
		  //animate(zoom).setDuration(2000).x(w).y(0);
		 // animate(zoom).setDuration(2000).alpha(1);
		findImageViews();
		findtextView();
		return parentView;
	}
public void findtextView(){
	
	  title1 = (TextView) parentView.findViewById(R.id.title1);
	  animate(title1).setDuration(2000).alpha(1);
	  title2 = (TextView) parentView.findViewById(R.id.title2);
	  animate(title2).setDuration(2000).alpha(1);
	 
	
}
	public void findImageViews(){
		image11 = (ImageView) parentView.findViewById(R.id.imageView11);
		  animate(image11).setDuration(2000).alpha(1);
		image12 = (ImageView) parentView.findViewById(R.id.imageView12);
		 animate(image12).setDuration(2400).alpha(1);
		image13 = (ImageView) parentView.findViewById(R.id.imageView13);
		 animate(image13).setDuration(2800).alpha(1);
		 image21 = (ImageView) parentView.findViewById(R.id.imageView21);
		  animate(image21).setDuration(3200).alpha(1);
		image22 = (ImageView) parentView.findViewById(R.id.imageView22);
		 animate(image22).setDuration(3600).alpha(1);
		image23 = (ImageView) parentView.findViewById(R.id.imageView23);
		 animate(image23).setDuration(4000).alpha(1);
		 image31 = (ImageView) parentView.findViewById(R.id.imageView31);
		  animate(image31).setDuration(4400).alpha(1);
		image32 = (ImageView) parentView.findViewById(R.id.imageView32);
		 animate(image32).setDuration(4800).alpha(1);
		image33 = (ImageView) parentView.findViewById(R.id.imageView33);
		 animate(image33).setDuration(5200).alpha(1);
		 image41 = (ImageView) parentView.findViewById(R.id.imageView41);
		  animate(image41).setDuration(5600).alpha(1);
		image42 = (ImageView) parentView.findViewById(R.id.imageView42);
		 animate(image42).setDuration(6000).alpha(1);
		image43 = (ImageView) parentView.findViewById(R.id.imageView43);
		 animate(image43).setDuration(6400).alpha(1);
		
		
	}

	    //******************
}