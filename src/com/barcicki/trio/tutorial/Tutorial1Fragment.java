package com.barcicki.trio.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardView;

public class Tutorial1Fragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		CardView card1 = (CardView) getView().findViewById(R.id.card1);
		CardView card2 = (CardView) getView().findViewById(R.id.card2);
		CardView card3 = (CardView) getView().findViewById(R.id.card3);
		
		card1.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE));
		card2.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_TWO));
		card3.setCard(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_THREE));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.tutorial_step1_fragment, container, false);
	}
	
}
