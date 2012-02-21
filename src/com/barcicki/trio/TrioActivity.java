package com.barcicki.trio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.Trio;

public class TrioActivity extends Activity {
	final private Trio trio = new Trio();
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        trio.newGame();
        redrawCards();      	
        
    }
    
    private void redrawCards() {
    	
    	GridLayout grid = (GridLayout) findViewById(R.id.cardsContainer);
    	CardList table = trio.getTable(); 
    	final CardList selected = new CardList();
    	
    	grid.removeAllViews();
    	
    	for (int i = 0; i < table.size(); i++) {
        	
        	ImageButton btn = new ImageButton(this);
        	btn.setImageResource(getResources().getIdentifier("card" + table.get(i).toString(), "drawable", getPackageName()));
        	btn.setTag(table.get(i));
        	btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Card c = (Card) v.getTag();
					
					if (selected.contains(c)) {
						selected.remove(c);
					} else {
						selected.add(c);
					}
					
					if (selected.size() == 3) {
						
						if (selected.hasTrio()) {
							Toast.makeText(getApplicationContext(), "Trio found", Toast.LENGTH_SHORT).show();
							
							trio.foundTrio(selected);
							
							if (trio.getGame().hasNext()) {
								redrawCards();
							} else {
								Toast.makeText(getApplicationContext(), "Trio Game has ended", Toast.LENGTH_SHORT);
							}
							
						} else {
							Toast.makeText(getApplicationContext(), "You're wrong!", Toast.LENGTH_SHORT).show();
						}
						
						selected.clear();
						
					}
				}
				
			});
        
        	grid.addView(btn);
        }
    }
}