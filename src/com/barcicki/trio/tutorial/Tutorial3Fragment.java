package com.barcicki.trio.tutorial;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.SoundManager;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.Trio.TrioStatus;

public class Tutorial3Fragment extends Fragment {
	private static final int NUMBER_OF_ADDITIONAL_CARDS = 4;
	private int currentSet = 0;
	private ArrayList<TrioSet> sets = new ArrayList<TrioSet>();

	private Button mPrev;
	private Button mNext;

	private CardView mCardA;
	private CardView mCardB;
	private CardView mCardToGuess;

	private CardView mOptionA;
	private CardView mOptionB;
	private CardView mOptionC;
	private CardView mOptionD;
	private CardView mOptionE;

	private static final ScheduledExecutorService mWorker = Executors
			.newSingleThreadScheduledExecutor();

	private Trio mTrio = new Trio();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mPrev = (Button) getView().findViewById(R.id.buttonPrev);
		mNext = (Button) getView().findViewById(R.id.buttonNext);
		// mQuit = (Button) getView().findViewById(R.id.buttonQuit);

		mCardA = (CardView) getView().findViewById(R.id.card1);
		mCardB = (CardView) getView().findViewById(R.id.card2);
		mCardToGuess = (CardView) getView().findViewById(R.id.card3);
		mOptionA = (CardView) getView().findViewById(R.id.card4);
		mOptionB = (CardView) getView().findViewById(R.id.card5);
		mOptionC = (CardView) getView().findViewById(R.id.card6);
		mOptionD = (CardView) getView().findViewById(R.id.card7);
		mOptionE = (CardView) getView().findViewById(R.id.card8);

		prepareSets();
		showSet();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.tutorial_step3_fragment, container,
				false);
	}

	public void onSelectCard(final View v) {
		
		TrioSet set = sets.get(currentSet);
		CardList selection = new CardList();
		CardView currentCard = (CardView) v;
		currentCard.setSelected(true);
		selection.add(set.getCardA());
		selection.add(set.getCardB());
		selection.add(currentCard.getCard());

		if (selection.hasTrio()) {

			set.setSolved(true);

			mCardToGuess.setSwitchAnimationLsitener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							onNextSetClicked(null);
						}
					}, 300);
				}
			});

			mCardToGuess.animateSwitchCard(set.getSolution());
			
			SoundManager.getInstance(getActivity()).playSound(SoundManager.SOUND_SUCCESS);

		} else {
			
			SoundManager.getInstance(getActivity()).playSound(SoundManager.SOUND_FAIL);

			currentCard.animateFail();

			EnumSet<TrioStatus> status = Trio.getTrioStatus(selection);
			ArrayList<String> errors = new ArrayList<String>();

			if (status.contains(TrioStatus.WRONG_COLOR)) {
				errors.add(getString(R.string.tutorial_colour));
			}

			if (status.contains(TrioStatus.WRONG_SHAPE)) {
				errors.add(getString(R.string.tutorial_shape));
			}

			if (status.contains(TrioStatus.WRONG_FILL)) {
				errors.add(getString(R.string.tutorial_fill));
			}

			if (status.contains(TrioStatus.WRONG_NUMBER)) {
				errors.add(getString(R.string.tutorial_number));
			}

			Toast.makeText(
					getActivity(),
					getString(R.string.tutorial_wrong_message,
							TextUtils.join(", ", errors)), Toast.LENGTH_LONG)
					.show();

		}

	}

	//
	private void prepareSets() {
		sets.add(new TrioSet(new Card(Card.SHAPE_SQUARE, Card.COLOR_RED,
				Card.FILL_EMPTY, Card.NUMBER_ONE), new Card(Card.SHAPE_SQUARE,
				Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_TWO)));
		sets.add(new TrioSet(new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED,
				Card.FILL_EMPTY, Card.NUMBER_TWO), new Card(Card.SHAPE_SQUARE,
				Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_TWO)));
		sets.add(new TrioSet(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_RED,
				Card.FILL_EMPTY, Card.NUMBER_ONE), new Card(
				Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_HALF,
				Card.NUMBER_TWO)));
		sets.add(new TrioSet(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_RED,
				Card.FILL_EMPTY, Card.NUMBER_ONE), new Card(Card.SHAPE_SQUARE,
				Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_TWO)));
		sets.add(new TrioSet(new Card(Card.SHAPE_SQUARE, Card.COLOR_GREEN,
				Card.FILL_EMPTY, Card.NUMBER_ONE), new Card(Card.SHAPE_CIRCLE,
				Card.COLOR_GREEN, Card.FILL_EMPTY, Card.NUMBER_THREE)));
		sets.add(new TrioSet(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_RED,
				Card.FILL_EMPTY, Card.NUMBER_ONE), new Card(Card.SHAPE_CIRCLE,
				Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_THREE)));
		sets.add(new TrioSet(new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE,
				Card.FILL_HALF, Card.NUMBER_THREE), new Card(Card.SHAPE_CIRCLE,
				Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_THREE)));
		sets.add(new TrioSet(new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE,
				Card.FILL_EMPTY, Card.NUMBER_TWO), new Card(Card.SHAPE_SQUARE,
				Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_THREE)));
		sets.add(new TrioSet(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN,
				Card.FILL_HALF, Card.NUMBER_ONE), new Card(Card.SHAPE_CIRCLE,
				Card.COLOR_RED, Card.FILL_HALF, Card.NUMBER_ONE)));
		sets.add(new TrioSet(new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE,
				Card.FILL_HALF, Card.NUMBER_ONE), new Card(Card.SHAPE_CIRCLE,
				Card.COLOR_GREEN, Card.FILL_EMPTY, Card.NUMBER_THREE)));

	}

	//
	public void showSet() {
		int size = sets.size();
		if (currentSet <= 0) {
			currentSet = 0;
			mPrev.setVisibility(View.INVISIBLE);
		} else {
			mPrev.setVisibility(View.VISIBLE);
		}

		if (currentSet >= size - 1) {
			currentSet = size - 1;
			mNext.setVisibility(View.INVISIBLE);
		} else {
			mNext.setVisibility(View.VISIBLE);
		}

		TextView summary = (TextView) getView().findViewById(R.id.summary);
		summary.setText((currentSet + 1) + "/" + size);

		TrioSet quiz = sets.get(currentSet);

//		if (mCardA.getCard() != null) {
//			mCardA.animateSwitchCard(quiz.getCardA());
//			mCardB.animateSwitchCard(quiz.getCardB());
//		} else {
//			mCardA.setCard(quiz.getCardA());
//			mCardB.setCard(quiz.getCardB());
//		}
		
		mCardA.setCard(quiz.getCardA());
		mCardB.setCard(quiz.getCardB());

		if (quiz.isSolved()) {
			mCardToGuess.setCard(quiz.getSolution());
		} else {
			mCardToGuess.setCard(null);
		}

		CardList cards = quiz.getTrioQuiz(mTrio.getDeck(),
				NUMBER_OF_ADDITIONAL_CARDS);

		if (Trio.LOCAL_LOGV) {
			Log.v("Trio Tutorial", "Quiz set: " + cards.toString());
		}

		if (!quiz.isSolved()) {
			cards.shuffle();
		}
		
//		if (mOptionA.getCard() != null) {
//			mOptionA.animateSwitchCard(cards.get(0));
//			mOptionB.animateSwitchCard(cards.get(1));
//			mOptionC.animateSwitchCard(cards.get(2));
//			mOptionD.animateSwitchCard(cards.get(3));
//			mOptionE.animateSwitchCard(cards.get(4));
//		} else {
		mOptionA.setCard(cards.get(0));
		mOptionA.setSelected(false);
		mOptionB.setCard(cards.get(1));
		mOptionB.setSelected(false);
		mOptionC.setCard(cards.get(2));
		mOptionC.setSelected(false);
		mOptionD.setCard(cards.get(3));
		mOptionD.setSelected(false);
		mOptionE.setCard(cards.get(4));
		mOptionE.setSelected(false);
			
//		}
		
		if (Trio.LOCAL_LOGV) {
			Log.v("Trio Tutorial", "CurrentSet" + currentSet);
		}

	}

	//
	public void onPrevSetClicked(View v) {
		currentSet -= 1;
		showSet();
	}

	public void onNextSetClicked(View v) {
		currentSet += 1;
		showSet();
	}
	//
}
