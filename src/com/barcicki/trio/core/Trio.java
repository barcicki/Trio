package com.barcicki.trio.core;

import android.util.Log;

public class Trio {
	private static int MAX_CARDS = 81;
	
	
	private CardList cards;
	

	public Trio() {
		generateCards();
	}

	/*
	 * Setters and getters
	 */
	public CardList getCards() {
		if (null == cards) cards = new CardList();
		return cards;
	}

	public void setCards(CardList cards) {
		this.cards = cards;
	}
	
	
	/*
	 * Public methods
	 */
	
	public static boolean isTrio(CardList cards) {
		if (cards.size() == 3) {
			return isTrio(cards.get(0), cards.get(1), cards.get(2));
		}
		return false;
	}
	
	public static boolean isTrio(Card a, Card b, Card c) {
		if ((a.getShape() + b.getShape() + c.getShape()) % 3 != 0) return false; 
		if ((a.getColor() + b.getColor() + c.getColor()) % 3 != 0) return false; 
		if ((a.getFill() + b.getFill() + c.getFill()) % 3 != 0) return false; 
		if ((a.getNumber() + b.getNumber() + c.getNumber()) % 3 != 0) return false; 
		return true; 
	}
	
	/*
	 * Private methods
	 */
	
	private void generateCards() {
		for (int i = 0; i < MAX_CARDS; i++) {
			getCards().add(new Card( (i/27)%3, (i/9)%3, (i/3)%3, i%3 ));
		}
	}
	
	
	
	
}
