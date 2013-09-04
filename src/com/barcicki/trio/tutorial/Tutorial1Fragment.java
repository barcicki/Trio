package com.barcicki.trio.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Card;
import com.barcicki.trio.views.CardView;

public class Tutorial1Fragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		assignCard(R.id.color1, new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE));
		assignCard(R.id.color2, new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_ONE));
		assignCard(R.id.color3, new Card(Card.SHAPE_SQUARE, Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_ONE));
		assignCard(R.id.shape1, new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_ONE));
		assignCard(R.id.shape2, new Card(Card.SHAPE_TRIANGLE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_ONE));
		assignCard(R.id.shape3, new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_ONE));
		assignCard(R.id.quantity1, new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_ONE));
		assignCard(R.id.quantity2, new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_TWO));
		assignCard(R.id.quantity3, new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_THREE));
		assignCard(R.id.fill1, new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE));
		assignCard(R.id.fill2, new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_ONE));
		assignCard(R.id.fill3, new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_HALF, Card.NUMBER_ONE));
		
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
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.tutorial_step1_fragment, container, false);
	}
	
}
