package com.example.start;

import java.util.ArrayList;



import com.example.myqq.MyTabActivity;
import com.example.myqq.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PagerActivity extends Activity{
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	private Button startBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_activity);

		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mPagerTitleStrip = (PagerTitleStrip)findViewById(R.id.pagertitle);
		//将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.viewpager_activity_item1, null);
		View view2 = mLi.inflate(R.layout.viewpager_activity_item2, null);
		View view3 = mLi.inflate(R.layout.viewpager_activity_item3, null);

		//每个页面的Title数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);



		//填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return null;
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};

		mViewPager.setAdapter(mPagerAdapter);



	}
	public void startbutton(View v) {  
		Intent intent = new Intent();
		intent.setClass(PagerActivity.this,WhatsnewDoor.class);
		startActivity(intent);
		this.finish();
	}  

}


