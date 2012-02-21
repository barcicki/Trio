package com.barcicki.trio.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardList extends ArrayList<Card> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8960343953643831424L;
	private int position = 0;

	public CardList(List<Card> subList) {
		addAll(subList);
	}
	
	public CardList() {
		// TODO Auto-generated constructor stub
	}
	
	public void shuffle() {
		Collections.shuffle(this);
	}
	
	public CardList getRandomRange(int number) {
		shuffle();
		return subList(0, number);
	}
	
	public CardList getNext(int number) {
		CardList set = subList(position, position + number);
		position += number;
		return set;
	}
	
	public boolean hasNext() {
		return size() > position;
	}
	
	public boolean hasTrio() {
		int size = size();
		if (size > 2) {
			for (int i = 0; i < size; i++) {
				for (int j = (i + 1); j < size; j++) {
					for (int k = (j + 1); k < size; k++) {
						if (Trio.isTrio(get(i), get(j), get(k))) {
							return true;
						}
					}
				}
			}
			
		}
		return false;
	}
	
	@Override
		public CardList subList(int start, int end) {
			return new CardList(super.subList(start, end));
		}
}
