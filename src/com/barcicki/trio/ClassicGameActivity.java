package com.barcicki.trio;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardGridView;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.Trio;

public class ClassicGameActivity extends Activity {
	
	private CardList selectedCards = new CardList();
	
	private CardGridView cardGrid;
	private Trio trio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classic);
		
		applyAnimations();
		
		trio = new Trio();
		trio.newGame();
		
//		CardTableView table = (CardTableView) findViewById(R.id.cardsContainer);
//		table.setCards(trio.getTable());
		
		cardGrid = (CardGridView) findViewById(R.id.cardsContainer);
		
		cardGrid.setCards(trio.getTable());
		cardGrid.renderGrid();
		cardGrid.setOnCardClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CardView cv = (CardView) v;
				Card card = cv.getCard();
				
				if (!selectedCards.contains(card)) {
					
					selectedCards.add(card);
					cv.setSelected(true);
					
					if (Trio.LOCAL_LOGV)
						Log.v("Classic Game", "Selected " + card.toString());
				} else {
					
					selectedCards.remove(card);
					cv.setSelected(false);
					
					if (Trio.LOCAL_LOGV)
						Log.v("Classic Game", "Deselected " + card.toString());
				}
				
				if (selectedCards.size() == 3) {
					
					if (selectedCards.hasTrio()) {
						onTrioFound();
					} else {
						onNotTrioFound();
					}
					
					selectedCards.clear();
					cardGrid.deselectAll();
					
					
				} 			
				
			}

				
		});
				
//		GridView grid = (GridView) findViewById(R.id.gridView1);
//		DynamicCardAdapter adapter = new DynamicCardAdapter(this, R.layout.card, trio.getTable());
//		grid.setAdapter(adapter);
	}
	
	protected void onNotTrioFound() {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio NOT found");
	}

	protected void onTrioFound() {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio found");
		
		trio.foundTrio(selectedCards);
		cardGrid.renderGrid();
	}

	private void applyAnimations() {
		ImageView clouds = (ImageView) findViewById(R.id.layoutClouds);
		Animation cloudsAnimation = AnimationUtils.loadAnimation(this, R.anim.clouds);
		clouds.startAnimation(cloudsAnimation);
	}
}
