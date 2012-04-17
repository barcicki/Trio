package com.barcicki.trio.tutorial;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.Trio;

public class TrioSet {
	private Card cardA;
	private Card cardB;
	private Card solution;
	private boolean solved = false;
	private CardList quizSet = null;
	
	public TrioSet(Card cardA, Card cardB) {
		this.cardA = cardA;
		this.cardB = cardB;
	}

	public CardList getTrio() {
		CardList set = new CardList();
		set.add(cardA);
		set.add(cardB);
		set.add( Trio.getTrioCard(cardA, cardB) );
		return set;
	}
	
	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	public Card getCardA() {
		return cardA;
	}

	public void setCardA(Card cardA) {
		this.cardA = cardA;
	}

	public Card getCardB() {
		return cardB;
	}

	public void setCardB(Card cardB) {
		this.cardB = cardB;
	}
	
	

	public Card getSolution() {
		if (null == solution) solution = Trio.getTrioCard(cardA, cardB);
		return solution;
	}

	public void setSolution(Card solution) {
		this.solution = solution;
	}

	public CardList getTrioQuiz(CardList deck, int numberOfAdditionalCards) {
		if (quizSet == null) {
			CardList set = new CardList();
			
			set.add( getSolution() );
			
			deck.shuffle();
			int i = 0;
			do {
			
				Card card = deck.get(i);
				if (!card.isEqual( getSolution() ) && !card.isEqual( getCardA() ) && !card.isEqual( getCardB() )) {
					set.add(card);
				}
				
				i++;
			} while (set.size() <= numberOfAdditionalCards);
			
			quizSet = set;
		}
		return quizSet;
	}
	
	
}
