package com.barcicki.trio.core;

public class Trio {
	public static int MAX_CARDS = 81;
	public static int DEFUALT_TABLE_SIZE = 12;
	public static int DEFAULT_TABLE_ADDON = 1;
	
	private CardList deck;
	

	public Trio() {
		generateCards();
	}

	/*
	 * Setters and getters
	 */
	public CardList getDeck() {
		if (null == deck) this.deck = new CardList();
		return deck;
	}

	public void setDeck(CardList cards) {
		this.deck = cards;
	}
	
	public CardList getTable() {
		CardList table = getDeck().getNext(DEFUALT_TABLE_SIZE);
		while (!table.hasTrio()) {
			table.addAll(getDeck().getNext(DEFAULT_TABLE_ADDON));
		}
		return table;
	}
		
	/*
	 * Public methods
	 */

	
	public void shuffle() {
		getDeck().shuffle();
	}
	
	
	
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
			getDeck().add(new Card( (i/27)%3, (i/9)%3, (i/3)%3, i%3 ));
		}
	}

	public CardList updateTable(CardList table, CardList selected) {
		
		table.removeAll(selected);
		table.addAll(getDeck().getNext(3));
		
		while (!table.hasTrio() && getDeck().hasNext()) {
			table.addAll(getDeck().getNext(DEFAULT_TABLE_ADDON));
		}
		
		return table;
		
	}
	
	
	
	
}
