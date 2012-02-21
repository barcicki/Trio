package com.barcicki.trio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.Trio;

public class TrioActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Trio trio = new Trio();
        TextView[] cardsButtons = {
        		(TextView) findViewById(R.id.card1),
        		(TextView) findViewById(R.id.card2),
        		(TextView) findViewById(R.id.card3),
        		(TextView) findViewById(R.id.card4),
        		(TextView) findViewById(R.id.card5),
        		(TextView) findViewById(R.id.card6),
        		(TextView) findViewById(R.id.card7),
        		(TextView) findViewById(R.id.card8),
        		(TextView) findViewById(R.id.card9),
        		(TextView) findViewById(R.id.card10),
        		(TextView) findViewById(R.id.card11),
        		(TextView) findViewById(R.id.card12),
        };
        
        
        CardList table;
        do {
        	table = trio.getCards().getRandomRange(12);
        } while (!table.hasTrio());
        	
        final CardList selected = new CardList();
        
        for (int i = 0; i < 12; i++) {
        	cardsButtons[i].setText(table.get(i).toString());
        	cardsButtons[i].setTag(table.get(i));
        	cardsButtons[i].setOnClickListener(new OnClickListener() {
				
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
						} else {
							Toast.makeText(getApplicationContext(), "You're wrong!", Toast.LENGTH_SHORT).show();
						}
						
						selected.clear();
						
					}
				}
				
			});
        
        }
        
        
    }
}