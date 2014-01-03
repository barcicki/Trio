package com.barcicki.trio.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Card;
import com.barcicki.trio.tutorial.TutorialStepFragment.StepType;
import com.barcicki.trio.views.CardView;

abstract public class TutorialStepTrioFragment extends TutorialStepFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.tutorial_step2_fragment, container, false);
	}
	
	static public class TrioWithThreeSharedFeatures extends TutorialStepTrioFragment {
		@Override
		StepType getType() {
			return StepType.THREE_COMMON_FEATURES;
		}
	}
	static public class TrioWithTwoSharedFeatures extends TutorialStepTrioFragment {
		@Override
		StepType getType() {
			return StepType.TWO_COMMON_FEATURES;
		}
	}
	static public class TrioWithOneSharedFeature extends TutorialStepTrioFragment {
		@Override
		StepType getType() {
			return StepType.ONE_COMMON_FEATURE;
		}
	}
	static public class TrioWithNoSharedFeatures extends TutorialStepTrioFragment {
		@Override
		StepType getType() {
			return StepType.NO_COMMON_FEATURES;
		}
	}
	
	static public class WrongTrioBadColorFeatures extends TutorialStepTrioFragment {
		@Override
		StepType getType() {
			return StepType.WRONG_COLOR;
		}
	}
	
	static public class WrongTrioBadFillFeatures extends TutorialStepTrioFragment {
		@Override
		StepType getType() {
			return StepType.WRONG_FILL;
		}
	}
}
