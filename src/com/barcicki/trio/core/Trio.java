package com.barcicki.trio.core;

public class Trio {
	public static int MAX_CARDS = 81;
	public static int DEFUALT_TABLE_SIZE = 12;
	public static int DEFAULT_TABLE_ADDON = 1;
	
	private CardList deck;
	private CardList game;
	private CardList table;
	
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
	
	public CardList getGame() {
		if (null == game) this.game = getDeck();
		return game;
	}
	
	public void setGame(CardList game) {
		this.game = game;
	}
	
	/*
	 * Game logic
	 */
	
	public void newGame() {
		getDeck().shuffle();
		setGame(getDeck());
		prepareTable();
	}
	
	public void prepareTable() {
		if (null == table) table = new CardList(getGame().getNext(DEFUALT_TABLE_SIZE));
	
		int missing = DEFUALT_TABLE_SIZE - table.size();
		table.addAll(getGame().getNext(missing));
		
		while (!table.hasTrio()) {
			table.addAll(getGame().getNext(DEFAULT_TABLE_ADDON));
		}
		
	}
	
	public CardList getTable() {
		return table;
	}
	
	public CardList foundTrio(CardList trio) {
		
		getTable().removeAll(trio);
		prepareTable();
		return getTable();
		
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

//	public CardList updateTable(CardList table, CardList selected) {
//		
//		table.removeAll(selected);
//		table.addAll(getDeck().getNext(3));
//		
//		while (!table.hasTrio() && getDeck().hasNext()) {
//			table.addAll(getDeck().getNext(DEFAULT_TABLE_ADDON));
//		}
//		
//		return table;
//		
//	}
	
	
	
	
}
