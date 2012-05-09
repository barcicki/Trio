package com.barcicki.trio;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

import com.barcicki.trio.core.TrioActivity;
import com.barcicki.trio.core.TutorialAdapter;
import com.barcicki.trio.tutorial.Tutorial1Fragment;
import com.barcicki.trio.tutorial.Tutorial2Fragment;
import com.barcicki.trio.tutorial.Tutorial3Fragment;
import com.viewpagerindicator.CirclePageIndicator;

public class TutorialActivity extends TrioActivity {
	private TutorialAdapter mTutorialAdapter;
	private ViewPager mTutorialPager;
	private CirclePageIndicator mTutorialIndicator;
	
	private Button mPrev;
	private Button mNext;
	
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.tutorial);
		super.onCreate(arg0);
		
		mTutorialAdapter = new TutorialAdapter(this);
		mTutorialAdapter.addFragment(Tutorial1Fragment.class);
		mTutorialAdapter.addFragment(Tutorial2Fragment.class);
		mTutorialAdapter.addFragment(Tutorial3Fragment.class);
		
		mTutorialPager = (ViewPager) findViewById(R.id.tutorialHolder);
		mTutorialPager.setOffscreenPageLimit(5);
		mTutorialPager.setAdapter(mTutorialAdapter);
		
		mTutorialIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mTutorialIndicator.setViewPager(mTutorialPager);
		final float density = getResources().getDisplayMetrics().density;
		mTutorialIndicator.setRadius(10 * density);
		mTutorialIndicator.setFillColor(getResources().getColor(R.color.blue));
		mTutorialIndicator.setStrokeColor(getResources().getColor(R.color.black));
		mTutorialIndicator.setStrokeWidth(2f);
		
		mTutorialPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int arg0) {
				updateControlVisibility();
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mPrev = (Button) findViewById(R.id.slidePrev);
		mNext = (Button) findViewById(R.id.slideNext);
		
	}
	
	
	
	@Override
	protected void onResume() {
		updateControlVisibility();
		super.onResume();
	}
	
	private void updateControlVisibility() {
		int pos = mTutorialPager.getCurrentItem();
		int size = mTutorialAdapter.getCount();
		if (pos == 0) {
			mPrev.setVisibility(View.INVISIBLE);
		} else {
			mPrev.setVisibility(View.VISIBLE);
		}
		
		if ((pos+1) == size) {
			mNext.setVisibility(View.INVISIBLE);
		} else {
			mNext.setVisibility(View.VISIBLE);
		}
	}

	public void onQuitPressed(View v) {
		makeClickSound();
		setMusicContinue(true);
		finish();
	}
	
	public void onNextSlidePressed(View v) {
		makeClickSound();
		int currentPage = mTutorialPager.getCurrentItem();
		int size = mTutorialAdapter.getCount();
		int nextPage = (currentPage + 1 < size) ? currentPage + 1 : size - 1; 
		mTutorialPager.setCurrentItem( nextPage, true);
		updateControlVisibility();
	}
	
	public void onPrevSlidePressed(View v) {
		makeClickSound();
		int currentPage = mTutorialPager.getCurrentItem();
		int prevPage = (currentPage - 1 >= 0) ? currentPage - 1 : 0; 
		mTutorialPager.setCurrentItem( prevPage, true);
		updateControlVisibility();
	}
	
	
}
