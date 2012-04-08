package com.barcicki.trio.core;

import android.util.Log;

public class Trio {
	public static int MAX_CARDS = 81;
	public static int DEFUALT_TABLE_SIZE = 12;
	public static int DEFAULT_TABLE_ADDON = 1;
	
	public static boolean LOCAL_LOGD = true;
	public static boolean LOCAL_LOGV = true;
	
	private CardList mDeck;
	private CardList mGame;
	private String mGameString;
	private CardList mTable;
	
	public Trio() {
		generateCards();
	}

	/*
	 * Setters and getters
	 */
	public CardList getDeck() {
		if (null == mDeck) this.mDeck = new CardList();
		return mDeck;
	}

	public void setDeck(CardList cards) {
		this.mDeck = cards;
	}
	
	public CardList getGame() {
		if (null == mGame) this.mGame = getDeck();
		return mGame;
	}
	
	public void setGame(CardList game) {
		this.mGame = game;
	}
	
	public String getGameString() {
		return mGameString;
	}
	
	public void setGameString(String gameString) {
		this.mGameString = gameString;
	}
	
	
	/*
	 * Game logic
	 */
	

	/**
	 * Starts new game
	 */
	public void newGame() {
		mDeck.shuffle();
		mGame = new CardList(mDeck); 
		mGameString = mGame.toString();
		mTable = new CardList();
		updateTable();
	}
	
	/**
	 * Resets game progress.
	 */
	public void restartGame(String gameString) {
		mGame = CardList.fromString(mDeck, gameString);
		mTable = new CardList();
		updateTable();
	}
	
	/**
	 * Updates table deck of cards so it have at least DEFAULT_TABLE_SIZE cards and at least one Trio
	 */
	public void updateTable() {
		
		if (getGame().size() > 0) {
			
			// if empty, create new set of 12 cards
			if (getTable().size() <= 0) {
				mTable = new CardList(getGame().getNext(DEFUALT_TABLE_SIZE));
			}
			
			// 
			if (getTable().size() < DEFUALT_TABLE_SIZE) {
				
				int missing = DEFUALT_TABLE_SIZE - getTable().size();
				
				if (missing > getGame().size()) {
					missing = getGame().size();
				}
				
				if (missing > 0) {
					getTable().addAll(getGame().getNext(missing));	
					if (LOCAL_LOGV) Log.v("Table", "Table replenished to DEFAULT number");
				} else {
					if (LOCAL_LOGV) Log.v("Table", "No more cards to replenish");
				}
				
			}
			
		} else {
			if (LOCAL_LOGD) Log.d("Game Deck", "Game Deck is empty");
		}
		
		
		while (!getTable().hasTrio() && getGame().size() > 0) {
			getTable().addAll(getGame().getNext(DEFAULT_TABLE_ADDON));
			if (LOCAL_LOGV) Log.v("Table", "Added additional cards");
		}
		
		if (LOCAL_LOGV) Log.v("Table", "Table prepared");
	}
	
	public CardList getTable() {
		if (null == mTable) mTable = new CardList();
		return mTable;
	}
	
	public void setTable(CardList table) {
		this.mTable = table;
	}
	
	/**
	 * Assuming that trio was found, given cards are removed from table deck
	 * @param trio Set of cards that make trio
	 * @return New table set
	 */
	public CardList foundTrio(CardList trio) {
		getTable().removeAll(trio);
		updateTable();
		return getTable();
	}
	
	/**
	 * Shuffles the deck
	 */
	public void shuffle() {
		getDeck().shuffle();
		if (LOCAL_LOGV) Log.v("Deck", "Deck shuffled");
	}
	
	/**
	 * Checks if set consisting of three cards make trio
	 * @param cards Set of cards
	 * @return True if cards make trio
	 */
	public static boolean isTrio(CardList cards) {
		if (cards.size() == 3) {
			return isTrio(cards.get(0), cards.get(1), cards.get(2));
		}
		return false;
	}
	
	/**
	 * Checks if given cards make trio
	 * @param cardA 
	 * @param cardB 
	 * @param cardC 
	 * @return True if cards make trio
	 */
	public static boolean isTrio(Card cardA, Card cardB, Card cardC) {
		if ((cardA.getShape() + cardB.getShape() + cardC.getShape()) % 3 != 0) return false; 
		if ((cardA.getColor() + cardB.getColor() + cardC.getColor()) % 3 != 0) return false; 
		if ((cardA.getFill() + cardB.getFill() + cardC.getFill()) % 3 != 0) return false; 
		if ((cardA.getNumber() + cardB.getNumber() + cardC.getNumber()) % 3 != 0) return false; 
		return true; 
	}
	
	public CardList getSetWithTrios(int numberOfTrios) {
		CardList set = new CardList(getDeck());
		CardList table;
		do {
			table = set.getRandomRange(DEFUALT_TABLE_SIZE);
		} while (table.numberOfTrios() != numberOfTrios);
		return table;
	}
	
	/*
	 * Private methods
	 */
	
	/**
	 * Populates deck list with basic cards
	 */
	private void generateCards() {
		for (int i = 0; i < MAX_CARDS; i++) {
			getDeck().add(new Card( (i/27)%3, (i/9)%3, (i/3)%3, i%3 ));
		}
		if (LOCAL_LOGV) Log.v("Card Generator", "All cards have been generated");
	}

	
}
