package com.barcicki.trio.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.barcicki.trio.R;

public class CardAdapter extends ArrayAdapter<Card> {
	
	private CardList cards;

	public CardAdapter(Context context, int textViewResourceId,
			CardList objects) {
		super(context, textViewResourceId, objects);
		cards = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Card card = cards.get(position);
		
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
