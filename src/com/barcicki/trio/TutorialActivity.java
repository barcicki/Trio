package com.barcicki.trio;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

import com.barcicki.trio.core.TutorialAdapter;
import com.barcicki.trio.tutorial.TutorialStepTrioFragment;
import com.barcicki.trio.tutorial.TutorialStepFragment;
import com.barcicki.trio.tutorial.TutorialStepQuizFragment;
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
		mTutorialAdapter.addFragment(TutorialStepFragment.FeatureColor.class);
		mTutorialAdapter.addFragment(TutorialStepFragment.FeatureQuantity.class);
		mTutorialAdapter.addFragment(TutorialStepFragment.FeatureShape.class);
		mTutorialAdapter.addFragment(TutorialStepFragment.FeatureFill.class);
		mTutorialAdapter.addFragment(TutorialStepTrioFragment.TrioWithThreeSharedFeatures.class);
		mTutorialAdapter.addFragment(TutorialStepTrioFragment.TrioWithTwoSharedFeatures.class);
		mTutorialAdapter.addFragment(TutorialStepTrioFragment.TrioWithOneSharedFeature.class);
		mTutorialAdapter.addFragment(TutorialStepTrioFragment.TrioWithNoSharedFeatures.class);
		mTutorialAdapter.addFragment(TutorialStepTrioFragment.WrongTrioBadColorFeatures.class);
		mTutorialAdapter.addFragment(TutorialStepTrioFragment.WrongTrioBadFillFeatures.class);
		mTutorialAdapter.addFragment(TutorialStepQuizFragment.class);
		
		mTutorialPager = (ViewPager) findViewById(R.id.tutorialHolder);
		mTutorialPager.setOffscreenPageLimit(3);
		mTutorialPager.setAdapter(mTutorialAdapter);
		
		mTutorialIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mTutorialIndicator.setViewPager(mTutorialPager);
		final float density = getResources().getDisplayMetrics().density;
		mTutorialIndicator.setRadius(9 * density);
		mTutorialIndicator.setFillColor(getResources().getColor(R.color.blue));
		mTutorialIndicator.setStrokeColor(getResources().getColor(R.color.black));
		mTutorialIndicator.setStrokeWidth(2f);
		
		mTutorialIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				updateControlVisibility();
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			public void onPageScrollStateChanged(int arg0) {}
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
		finish();
	}
	
	public void onNextSlidePressed(View v) {
		int currentPage = mTutorialPager.getCurrentItem();
		int size = mTutorialAdapter.getCount();
		if (currentPage < size - 1) {
			makeClickSound();
			mTutorialPager.setCurrentItem(currentPage + 1, true);
		}
	}
	
	public void onPrevSlidePressed(View v) {
		int currentPage = mTutorialPager.getCurrentItem();
		if (currentPage > 0) {
			makeClickSound();
			mTutorialPager.setCurrentItem(currentPage - 1, true);
		}
	}
	
	
}
