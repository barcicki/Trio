package com.barcicki.trio.views;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Utils;
import com.barcicki.trio.core.MenuDescription.MenuDescriptionType;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MenuDescriptionButton extends ImageView {
	
	private static final int[] STATE_ACTIVE = {R.attr.state_active};
	private boolean mIsActive = false;
	private MenuDescriptionType mType = null;
	
	public MenuDescriptionButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray attrArray = context.obtainStyledAttributes(attrs,  R.styleable.MenuDescription);
		final int count = attrArray.getIndexCount();
		for (int i = 0; i < count; ++i)
		{
		    int attr = attrArray.getIndex(i);
		    
		    if (attr == R.styleable.MenuDescription_type) {
		    	mType = MenuDescriptionType.valueOf(attrArray.getString(attr));
		    }
		}
		attrArray.recycle();
	}
	
	public void setActive(boolean isActive) {
		mIsActive = isActive;
		refreshDrawableState();
	}
	
	public boolean isActive() {
		return mIsActive;
	}
	
	public MenuDescriptionType getType() {
		return mType;
	}
	
	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		
		if (isActive()) {
			mergeDrawableStates(drawableState, STATE_ACTIVE);
		}
		
		return drawableState;
	}

	
	
}
