package com.barcicki.trio.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.R;

public class MenuDescription extends FrameLayout {

	private TextView mDescription;
	private ImageView mLogo;
	private Button mLeftButton;
	private Button mRightButton;

	private MenuDescriptionType mCurrentType = MenuDescriptionType.CLASSIC;
	private MenuDescriptionListener mListener = null;

	public MenuDescription(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MenuDescription(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MenuDescription(Context context) {
		super(context);
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
		}

		mLogo = (ImageView) view.findViewById(R.id.menuLogo);
		mLeftButton = (Button) view.findViewById(R.id.menuButtonLeft);
		mRightButton = (Button) view.findViewById(R.id.menuButtonRight);

		addView(view);
		setMenuDescription(mCurrentType);
	}

	private void updateViews(int logoResource, int descriptionResource,
			int visibleButtons) {
		mLogo.setImageResource(logoResource);
		mDescription.setText(getContext().getString(descriptionResource));

		mLeftButton
				.setVisibility(visibleButtons > 1 ? View.VISIBLE : View.GONE);
	}

	private void renderClassicDescription() {
		updateViews(R.drawable.menu_classic_label,
				R.string.menu_description_classic, 2);

		mLeftButton.setEnabled(TrioSettings.isSavedGamePresent());
	}

	private void renderSpeedDescription() {
		updateViews(R.drawable.menu_speed_label,
				R.string.menu_description_speed, 1);
	}

	private void renderTripleDescription() {
		updateViews(R.drawable.menu_triple_label,
				R.string.menu_description_triple, 1);
	}

	private void renderHelpDescription() {
		updateViews(R.drawable.menu_help_label, R.string.menu_description_help,
				1);
	}
	
	public MenuDescriptionType getMenuDescription() {
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

	public interface MenuDescriptionListener {
		void onLeftButtonPressed(MenuDescriptionType type, View v);
		void onRightButtonPressed(MenuDescriptionType type, View v);
	}

	public enum MenuDescriptionType {
		CLASSIC, SPEED, TRIPLE, HELP
	}
}
