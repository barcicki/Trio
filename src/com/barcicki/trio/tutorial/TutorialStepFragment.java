package com.barcicki.trio.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Card;
import com.barcicki.trio.views.CardView;

abstract public class TutorialStepFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		StepType details = getType();
		
		assignCard(R.id.card1, details.card1);
		assignCard(R.id.card2, details.card2);
		assignCard(R.id.card3, details.card3);
		
		((TextView) getView().findViewById(R.id.slideSubtitle)).setText(getString(details.titileResrouce));
	}
	
	private void assignCard(int resourceId, Card card) {
		CardView cardView = (CardView) getView().findViewById(resourceId);
		if (cardView != null) {
			cardView.setCard(card);
		} else {
			Log.e("TrioTutorial", "Wrong CardView provided");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tutorial_step1_fragment, container, false);
	}
	
	abstract StepType getType();
	
	enum StepType {
		
		// 4 features
		COLOR(
				R.string.tutorial_colour,
				new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_SQUARE, Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_ONE)
		),
		QUANTITY(
				R.string.tutorial_number,
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_TWO),
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_THREE)
		),
		FILL(
				R.string.tutorial_fill,
				new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_ONE),
				new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_HALF, Card.NUMBER_ONE)
		),
		SHAPE(
				R.string.tutorial_shape,
				new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_ONE),
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_ONE),
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_ONE)
		),
		
		// example Trio sets
		THREE_COMMON_FEATURES (
				R.string.tutorial_3common,
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_ONE)
		),
		
		TWO_COMMON_FEATURES (
				R.string.tutorial_2common,
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_ONE)
		),
		
		ONE_COMMON_FEATURE (
				R.string.tutorial_1common,
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_ONE),
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_ONE)
		),
		
		NO_COMMON_FEATURES(
				R.string.tutorial_no_common,
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_TWO),   
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_THREE)
		),
		
		// example wrong Trio sets
		WRONG_COLOR(
				R.string.tutorial_wrong_color,
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE)
		),
		
		WRONG_FILL(
				R.string.tutorial_wrong_fill,
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE),
				new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_HALF, Card.NUMBER_ONE),
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_ONE)
		)
		
		;
		
		public Card card1;
		public Card card2;
		public Card card3;
		public int titileResrouce;
		
		StepType(int titleResource, Card card1, Card card2, Card card3) {
			this.titileResrouce = titleResource;
			this.card1 = card1;
			this.card2 = card2;
			this.card3 = card3;
		}
	}

	static public class FeatureShape extends TutorialStepFragment {
		@Override
		StepType getType() {
			return StepType.SHAPE;
		}
	}
	static public class FeatureColor extends TutorialStepFragment {
		@Override
		StepType getType() {
			return StepType.COLOR;
		}
	}
	static public class FeatureQuantity extends TutorialStepFragment {
		@Override
		StepType getType() {
			return StepType.QUANTITY;
		}
	}
	static public class FeatureFill extends TutorialStepFragment {
		@Override
		StepType getType() {
			return StepType.FILL;
		}
	}
	
}


