package com.barcicki.trio.core;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpAdapter extends PagerAdapter {
	private Context mContext;
	private ArrayList<Integer> mResources = new ArrayList<Integer>();
	
	public HelpAdapter(Context context) {
		mContext = context;
		mResources.clear();
	}
	
	public void addFragment(int fragmentId) {
		mResources.add(fragmentId);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mResources.size();
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		LayoutInflater l = LayoutInflater.from(mContext);
		
		int resourceId = mResources.get(position);
		
		View v;
		v = l.inflate(resourceId, null);
		((ViewPager) container).addView(v);
		return v;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0.equals(arg1);
	}
	
	
	
	
}
