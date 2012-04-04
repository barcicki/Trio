package com.barcicki.trio.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.barcicki.trio.R;

public class CardAdapter extends ArrayAdapter<Card> {
	
	private CardList mCards;
	
	public CardAdapter(Context context, int textViewResourceId,
			CardList objects) {
		super(context, textViewResourceId, objects);
		mCards = objects;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCards.size();
	}
	
	@Override
	public Card getItem(int position) {
		// TODO Auto-generated method stub
		return mCards.get(position);
	}
	
	@Override
	public long getItemId(int position) {
//		if (position > 5 && position < 12) return 1;
//		if (position == 12) return 0;
//		if (position == 14) return 0;
		return super.getItemId(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Card card = mCards.get(position);			 
		
		if (null == convertView) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.single_card, parent, false);
		}
		
		if (null != card) {
			ImageView img = (ImageView) convertView.findViewById(R.id.cardImage);
			img.setImageResource(getContext().getResources().getIdentifier("card" + card.toString(), "drawable", getContext().getPackageName()));
			img.setBackgroundColor(getContext().getResources().getColor(R.color.card_standby));
			img.setTag(card);
		}
		
		return convertView;
		
	}

}
