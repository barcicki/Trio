package com.barcicki.trio.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class TrioCardTableView extends GridView {

	public TrioCardTableView(Context context) {
		super(context);
	}
	
	public TrioCardTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TrioCardTableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		if (getAdapter() != null) {
			int size = getAdapter().getCount();
			if (size <= 12 ) {
				setNumColumns(6);
			} else if (size <= 14) {
				setNumColumns(7);
			} else {
				setNumColumns(8);
			}
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
