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

import com.barcicki.trio.HomeActivity;
import com.barcicki.trio.R;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.core.Utils;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MenuDescription extends FrameLayout {

	private TextView mDescription;
	private ImageView mLogo;
	private Button mLeftButton;
	private Button mRightButton;

	private MenuDescriptionType mCurrentType = MenuDescriptionType.CLASSIC;
	private MenuDescriptionListener mListener = null;

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

		if (!isInEditMode()) {
		
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
	}

	private void updateViews(int logoResource, int descriptionResource, Integer... buttonLabels) {

		mLogo.setImageResource(logoResource);
		mDescription.setText(getContext().getString(descriptionResource));
		
		mRightButton.setText(getContext().getString(buttonLabels[0]));
		
		if (buttonLabels.length == 2) {
			mLeftButton.setVisibility(View.VISIBLE);
			mLeftButton.setText(getContext().getString(buttonLabels[1]));
		} else {
			mLeftButton.setVisibility(View.GONE);
		}
	}

	private void renderClassicDescription() {
		if (TrioSettings.isSavedGamePresent()) {
			updateViews(R.drawable.menu_classic_label,
					R.string.menu_description_classic,
							R.string.menu_description_button_new_game, R.string.menu_description_button_continue);	
		} else {
			updateViews(R.drawable.menu_classic_label,
					R.string.menu_description_classic,
							R.string.menu_description_button_play);
		}
	}

	private void renderSpeedDescription() {
		updateViews(R.drawable.menu_speed_label,

		R.string.menu_description_speed, R.string.menu_description_button_play);
	}

	private void renderTripleDescription() {
		updateViews(R.drawable.menu_triple_label,

		R.string.menu_description_triple,
				R.string.menu_description_button_play);
	}

	private void renderHelpDescription() {
		updateViews(R.drawable.menu_help_label, R.string.menu_description_help,
				R.string.menu_description_button_learn);
	}
	
	private void renderPlayGamesDescription() {
		if (((HomeActivity) getContext()).isSignedIn()) {
			updateViews(R.drawable.menu_play_games_label, R.string.menu_description_play_games, R.string.menu_description_button_achievements, R.string.menu_description_button_leaderbaords);	
		} else {
			updateViews(R.drawable.menu_play_games_label, R.string.menu_description_play_games, R.string.menu_description_button_sign_in);
		}
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
		case PLAY_GAMES:
			renderPlayGamesDescription();
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
		CLASSIC, SPEED, TRIPLE, HELP, PLAY_GAMES, SETTINGS
	}

}
