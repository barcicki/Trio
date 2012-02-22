package com.barcicki.trio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardAdapter;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.Trio;

public class TrioActivity extends Activity {
	final private Trio trio = new Trio();
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        trio.newGame();
        redrawCards();      	
        
    }
    
    private void redrawCards() {
    	
    	GridView grid = (GridView) findViewById(R.id.cardContainer);

    	final CardList selectedCards = new CardList();
    	final CardAdapter adapter = new CardAdapter(this, R.layout.single_card, trio.getTable());
    	
    	grid.setAdapter(adapter);
    	grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Card card = (Card) arg1.getTag();
				
				if (!selectedCards.contains(card)) {
					selectedCards.add(card);
					arg1.setBackgroundColor(getResources().getColor(R.color.card_selected));
				} else {
					selectedCards.remove(card);
					arg1.setBackgroundColor(getResources().getColor(R.color.card_standby));
				}
				
				
				
				if (selectedCards.size() == 3) {
					
					if (selectedCards.hasTrio()) {
						
						Toast.makeText(getApplicationContext(), "Trio found", Toast.LENGTH_SHORT).show();
						
						trio.foundTrio(selectedCards);
						
						if (trio.getGame().hasNext()) {
							
						} else {
							Toast.makeText(getApplicationContext(), "Trio Game has ended", Toast.LENGTH_SHORT);
						}
						
					} else {
						Toast.makeText(getApplicationContext(), "You're wrong!", Toast.LENGTH_SHORT).show();
					}
					
					selectedCards.clear();
					adapter.notifyDataSetChanged();
					
				}
				
			}
    		
		});
    	
//    	for (int i = 0; i < table.size(); i++) {
//        	
//        	ImageButton btn = new ImageButton(this);
//        	btn.setImageResource(getResources().getIdentifier("card" + table.get(i).toString(), "drawable", getPackageName()));
//        	btn.setTag(table.get(i));
//        	btn.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Card c = (Card) v.getTag();
//					
//					if (selected.contains(c)) {
//						selected.remove(c);
//					} else {
//						selected.add(c);
//					}
//					
//					if (selected.size() == 3) {
//						
//						if (selected.hasTrio()) {
//							Toast.makeText(getApplicationContext(), "Trio found", Toast.LENGTH_SHORT).show();
//							
//							trio.foundTrio(selected);
//							
//							if (trio.getGame().hasNext()) {
//								redrawCards();
//							} else {
//								Toast.makeText(getApplicationContext(), "Trio Game has ended", Toast.LENGTH_SHORT);
//							}
//							
//						} else {
//							Toast.makeText(getApplicationContext(), "You're wrong!", Toast.LENGTH_SHORT).show();
//						}
//						
//						selected.clear();
//						
//					}
//				}
//				
//			});
//        
//        	grid.addView(btn);
//        }
    }
}