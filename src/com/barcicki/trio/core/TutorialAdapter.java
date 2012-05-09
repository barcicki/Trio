package com.barcicki.trio.core;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

public class TutorialAdapter extends FragmentPagerAdapter {

	private Context mContext;
//	private ArrayList<Class<?>> mFragments = new ArrayList<Class<?>>();
	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	
	
	public TutorialAdapter(FragmentActivity context) {
		super(context.getSupportFragmentManager());
		mContext = context;
		mFragments.clear();
	}
	
	public void addFragment(Class<?> clss) {
		mFragments.add(Fragment.instantiate(mContext, clss.getName()));
	}
	
	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}
	
	
}
