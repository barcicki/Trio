package com.barcicki.trio.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class CardGridSize {
	private HashMap<Integer, Integer> mMap = new HashMap<Integer, Integer>(); 
	private int mSize;
	private int mMax = 0;
	private static ArrayList<CardGridSize> mSets = new ArrayList<CardGridSize>();
	
	public CardGridSize(int size, int rows, int... columns) {
		mSize = size;
		for (int i = 0; i < rows; i++) {
			mMap.put(i, columns[i]);
			
			if (columns[i] > mMax) {
				mMax = columns[i];
			}
			
		}
	}
	
	public int getSize() {
		return mSize;
	}
	
	public int getRowSize(int row) {
		return mMap.get(row);
	}
	
	public int getRowsSize() {
		return mMap.size();
	}
	
	public int getRow(int position) {
		int passed = 0;
		for (Entry<Integer, Integer> row : mMap.entrySet()) {
			passed += row.getValue();
			if (passed > position) {
				return row.getKey();
			}
		}
		return -1;
	}
	
	public int getColumn(int position) {
		int passed = 0;
		for (Entry<Integer, Integer> row : mMap.entrySet()) {
			if (passed + row.getValue() > position) {
				return position % row.getValue();
			} else {
				passed += row.getValue();
			}
		}
		return -1;
	}
	
	public boolean isRowFull(int row) {
		return mMap.get(row) == mMax;
	}
		
	public static CardGridSize getGridSize(int size) {
		if (mSets.isEmpty()) {
			
			mSets.add(new CardGridSize(1, 1, 1));
			mSets.add(new CardGridSize(2, 1, 2));
			mSets.add(new CardGridSize(3, 2, 2, 1));
			mSets.add(new CardGridSize(4, 2, 2, 2));
			mSets.add(new CardGridSize(5, 2, 3, 2));
			mSets.add(new CardGridSize(6, 2, 3, 3));
			mSets.add(new CardGridSize(7, 3, 3, 2, 2));
			mSets.add(new CardGridSize(8, 3, 3, 3, 2));
			mSets.add(new CardGridSize(9, 3, 3, 3, 3));
			mSets.add(new CardGridSize(10, 3, 4, 3, 3));
			mSets.add(new CardGridSize(11, 3, 4, 4, 3));
			mSets.add(new CardGridSize(12, 3, 4, 4, 4));
			mSets.add(new CardGridSize(13, 3, 5, 4, 4));
			mSets.add(new CardGridSize(14, 3, 5, 5, 4));
			mSets.add(new CardGridSize(15, 3, 5, 5, 5));
			mSets.add(new CardGridSize(16, 4, 4, 4, 4, 4));
			mSets.add(new CardGridSize(17, 4, 5, 4, 4, 4));
			mSets.add(new CardGridSize(18, 4, 5, 5, 4, 4));
			mSets.add(new CardGridSize(19, 4, 5, 5, 5, 4));
			mSets.add(new CardGridSize(20, 4, 5, 5, 5, 5));
			mSets.add(new CardGridSize(21, 5, 5, 4, 4, 4, 4));
			mSets.add(new CardGridSize(22, 5, 5, 5, 4, 4, 4));
			
		}
		
		for (CardGridSize set : mSets) {
			if (set.getSize() == size) {
				return set;
			}
		}
		
		return null;
		
	}
	
	
}
