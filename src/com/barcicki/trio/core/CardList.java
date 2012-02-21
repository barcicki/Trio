package com.barcicki.trio.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardList extends ArrayList<Card> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8960343953643831424L;

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
	
	public CardList getNext(int max_number) {
		int limit = max_number > size() ? size() : max_number;
		CardList next = subList(0, limit);
		removeRange(0, limit);
		return next;
	}
	
	public boolean hasNext() {
		return size() > 0;
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
	
	public int numberOfTrios() {
		int size = size();
		int count = 0;
		if (size > 2) {
			for (int i = 0; i < size; i++) {
				for (int j = (i + 1); j < size; j++) {
					for (int k = (j + 1); k < size; k++) {
						if (Trio.isTrio(get(i), get(j), get(k))) {
							count += 1;
						}
					}
				}
			}
			
		}
		return count;
	}
	
	@Override
		public CardList subList(int start, int end) {
			return new CardList(super.subList(start, end));
		}
}
