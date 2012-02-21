package com.barcicki.trio.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardList extends ArrayList<Card> {
	
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
