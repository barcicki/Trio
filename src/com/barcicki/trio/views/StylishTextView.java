package com.barcicki.trio.views;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Utils;
import com.barcicki.trio.views.MenuDescription.MenuDescriptionType;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class StylishTextView extends TextView {

	final static String DEFAULT_FONT = "poetsen.otf";
	
	public StylishTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		String font = DEFAULT_FONT;
		int color = getResources().getColor(android.R.color.black);
		
		TypedArray attrArray = getContext().obtainStyledAttributes(attrs,  R.styleable.StylishTextView);
		 
		final int count = attrArray.getIndexCount();
		for (int i = 0; i < count; ++i)
		{
		    int attr = attrArray.getIndex(i);
		    switch (attr)
		    {
		        case R.styleable.StylishTextView_font:
		        	font = attrArray.getString(attr);
		            break;
		        case R.styleable.StylishTextView_shadow:
		        	color = attrArray.getInteger(attr, color);
		            break;
		    }
		}
		attrArray.recycle();
		
		this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font));
		this.setShadowLayer(0.4f, 2, 2, color);
	}

}
