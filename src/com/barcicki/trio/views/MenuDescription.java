package com.barcicki.trio.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.barcicki.trio.R;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.core.Utils;

public class MenuDescription extends FrameLayout {

	private TextView mDescription;
	private ImageView mLogo;
	private Button mLeftButton;
	private Button mRightButton;

	private MenuDescriptionType mCurrentType = MenuDescriptionType.CLASSIC;
	private MenuDescriptionListener mListener = null;

	public MenuDescription(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithAttrs(attrs);
	}

	public MenuDescription(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithAttrs(attrs);
	}

	public MenuDescription(Context context) {
		super(context);
		init();
	}
	
	private void initWithAttrs(AttributeSet attrs) {
		TypedArray attrArray = getContext().obtainStyledAttributes(attrs,  R.styleable.MenuDescription);
		 
		final int count = attrArray.getIndexCount();
		for (int i = 0; i < count; ++i)
		{
		    int attr = attrArray.getIndex(i);
		    switch (attr)
		    {
		        case R.styleable.MenuDescription_alpha:
		            Utils.setAlpha(this, attrArray.getFloat(attr, 1.0f));
		            break;
		        case R.styleable.MenuDescription_type:
		        	mCurrentType = MenuDescriptionType.valueOf(attrArray.getString(attr));
		            break;
		    }
		}
		attrArray.recycle();
		init();
	}
	
	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.menu_description, null);

		mDescription = (TextView) view.findViewById(R.id.menuDescription);
		
		if (!this.isInEditMode()) {
			mDescription.setTypeface(Typeface.createFromAsset(getContext()
					.getAssets(), "poetsen.otf"));

			mLogo = (ImageView) view.findViewById(R.id.menuLogo);
			mLeftButton = (Button) view.findViewById(R.id.menuButtonLeft);
			mRightButton = (Button) view.findViewById(R.id.menuButtonRight);

			setMenuDescription(mCurrentType);
		}

		addView(view);
	}

	private void updateViews(int logoResource, int descriptionResource,
			int buttonText, int visibleButtons) {

		mLogo.setImageResource(logoResource);
		mDescription.setText(getContext().getString(descriptionResource));
		mRightButton.setText(getContext().getString(buttonText));

		mLeftButton
				.setVisibility(visibleButtons > 1 ? View.VISIBLE : View.GONE);

	}

	private void renderClassicDescription() {
		if (TrioSettings.isSavedGamePresent()) {
			updateViews(R.drawable.menu_classic_label,
					R.string.menu_description_classic,
							R.string.menu_description_button_new_game, 2);	
		} else {
			updateViews(R.drawable.menu_classic_label,
					R.string.menu_description_classic,
							R.string.menu_description_button_play, 1);
		}
	}

	private void renderSpeedDescription() {
		updateViews(R.drawable.menu_speed_label,

		R.string.menu_description_speed, R.string.menu_description_button_play,
				1);
	}

	private void renderTripleDescription() {
		updateViews(R.drawable.menu_triple_label,

		R.string.menu_description_triple,
				R.string.menu_description_button_play, 1);
	}

	private void renderHelpDescription() {
		updateViews(R.drawable.menu_help_label, R.string.menu_description_help,
				R.string.menu_description_button_learn, 1);
	}

	public MenuDescriptionType getMenuType() {
		return mCurrentType;
	}

	public void setMenuDescriptionListener(MenuDescriptionListener listener) {
		this.mListener = listener;

		if (mListener != null) {
			mLeftButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mListener.onLeftButtonPressed(mCurrentType, v);
				}
			});
			mRightButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mListener.onRightButtonPressed(mCurrentType, v);
				}
			});
		} else {
			mLeftButton.setOnClickListener(null);
			mRightButton.setOnClickListener(null);
		}
	}

	public void setMenuDescription(MenuDescriptionType item) {
		mCurrentType = item;

		switch (item) {
		case CLASSIC:
			renderClassicDescription();
			break;
		case SPEED:
			renderSpeedDescription();
			break;
		case TRIPLE:
			renderTripleDescription();
			break;
		case HELP:
			renderHelpDescription();
			break;
		}
	}
	
	public void update() {
		setMenuDescription(getMenuType());
	}

	public interface MenuDescriptionListener {
		void onLeftButtonPressed(MenuDescriptionType type, View v);

		void onRightButtonPressed(MenuDescriptionType type, View v);
	}

	public enum MenuDescriptionType {
		CLASSIC, SPEED, TRIPLE, HELP
	}

}
