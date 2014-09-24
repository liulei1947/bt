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


import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.MainActivity;
import com.liulei1947.bt.R;
import com.viewpagerindicator.CirclePageIndicator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
public class HelpFragmentActivity extends FragmentActivity {
	private SharedPreferences appprefs;  
	private static final long DELAY_TIME = 2000L;
	 private static final String showHelpPager = "showHelpPager";  
	private MyPagerAdapter adapter;
	Fragment f;
	private JazzyViewPager mJazzy;
	CirclePageIndicator circlePageIndicator ;
	TextView StartMainActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		appprefs = PreferenceManager.getDefaultSharedPreferences(this);   
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);
	    circlePageIndicator = (CirclePageIndicator)findViewById(R.id.circle);
	    StartMainActivity = (TextView)findViewById(R.id.starmainActivity);
	    mJazzy.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		circlePageIndicator.setViewPager(mJazzy);
		StartMainActivity.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HelpFragmentActivity.this, com.liulei1947.bt.ui.MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				appprefs.edit().putBoolean(showHelpPager, true).commit();   
				finish();
			}
			
		});
	}


	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "资源","BT", "种子下载", "看图","发图","搜索"};

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			if(position==0){
			
				f=HelpFragment0.newInstance(position);
			}
			if(position==1){
				f=HelpFragment1.newInstance(position);
			}
				if(position==2){
				f=HelpFragment2.newInstance(position);
			
			}
				if(position==3){
			f=HelpFragment3.newInstance(position);
				
				}
				if(position==4){
					f=HelpFragment4.newInstance(position);
						
						}
				if(position==5){
					f=HelpFragment5.newInstance(position);
						
						}
				
			return f;
		}

		
		
	}
	
	
	
	
}