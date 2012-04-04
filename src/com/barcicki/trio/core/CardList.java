package com.barcicki.trio.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

public class CardList extends ArrayList<Card> {
	private static final long serialVersionUID = 8960343953643831424L;

	public CardList(List<Card> subList) {
		addAll(subList);
	}
	
	public CardList() {
		// TODO Auto-generated constructor stub
	}
	
	public CardList(Card... cards) {
		for (Card c : cards) {
			add(c);
		}
	}
	
	
	public void shuffle() {
		Collections.shuffle(this);
	}
	
	/**
	 * Return number set of cards in random order
	 * @param number 
	 * @return
	 */
	public CardList getRandomRange(int number) {
		shuffle();
		return subList(0, number);
	}
	
	/**
	 * Extracts max_number cards from the begining of list and then returns it
	 * @param max_number Max number of cards to return (if list is shorter, whole list is returned)
	 * @return Extracted cards
	 */
	public CardList getNext(int max_number) {
		CardList next = new CardList();
		
		if (size() > 0) {
			int limit = max_number > size() ? size() : max_number;
			next = subList(0, limit);
			removeRange(0, limit);
		}
		
		if (Trio.LOCAL_LOGV) Log.v("CardList", "Extracted " + next.size() + " cards");
		return next;
	}
	
	/**
	 * Checks if there're any cards left in the list
	 * @return
	 */
	public boolean hasNext() {
		return !isEmpty();
	}
	
	/**
	 * Checks if current list has at least one trio
	 * @return True if three cards that makes trio exists in list
	 */
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
	
	/**
	 * Returns number of trio that can be found in this list
	 * @return
	 */
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
		if (Trio.LOCAL_LOGV) Log.v("CardList", "Set has " + count + " trios");
		return count;
	}
	
	/**
	 * Return array of sets of three cards from a collection that makes a trio
	 * @return
	 */
	public ArrayList<CardList> getTrios() {
		int size = size();
		ArrayList<CardList> trios = new ArrayList<CardList>();
		
		if (size > 2) {
			for (int i = 0; i < size; i++) {
				for (int j = (i + 1); j < size; j++) {
					for (int k = (j + 1); k < size; k++) {
						if (Trio.isTrio(get(i), get(j), get(k))) {
							trios.add(new CardList(get(i), get(j), get(k)));
							if (Trio.LOCAL_LOGV) Log.v("CardList", "Trio collected");
						}
					}
				}
			}
			
		}
		return trios;
	}
	
	@Override
	/**
	 * Sublist returning CardList instead of ArrayList
	 */
	public CardList subList(int start, int end) {
		return new CardList(super.subList(start, end));
	}
	
	public Card find( String cardString ) {
		for (Card c : this) {
			if (c.toString().equals(cardString)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Gets string with cards ids  
	 */
	@Override
	public String toString() {
		return TextUtils.join(" ", this);
	}
	
	/**
	 * 
	 * @param sourceDeck
	 * @param cardListString
	 */
	public static CardList fromString(CardList sourceDeck, String cardListString) {
		CardList cards = new CardList();
		for (String cardString : TextUtils.split(cardListString, " ")) {
			Card card = sourceDeck.find( cardString );
			if (null != card) {
				cards.add(card);
			}
		}
		return cards;
	}
	
	
}
