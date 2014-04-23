package com.mobilearning.minpairs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.mobilearning.minpairs.util.ML_Main;
import com.mobilearning.minpairs.util.MP_AllQuestions;
import com.mobilearning.minpairs.util.MP_Item;
import com.mobilearning.minpairs.util.MP_Question;
import com.mobilearning.minpairs.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.InputType;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class PracticeActivity extends MPActivity implements
		OnEditorActionListener {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	private static final int ANSWER_ID = 999;

	private static final int playSize = 200;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private Button answerBtn;

	MP_AllQuestions AllQ;
	MP_Question myQ;
	MP_Button currBtn;

	String sWord1 = "";
	String sWord2 = "";

	String sCtrlIdBase = "C";
	int iCtrlId = 0;
	int iCtrlMinorId = 0;
	String sCtrlId = "";

	ArrayList<MP_Button> MPBtnGroup = new ArrayList<MP_Button>();
	int iItemId = 0;
	// private int bkgColor;
	String sHelp = "This will explain how the practice screen works.......";

	private EditText input;
	private MP_Button[] btns = new MP_Button[2];

	private int cnt = 0;
	private AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_practice);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		if(mf!=null && !mf.isFilterSet())
			showFilter();
		adView = (AdView)this.findViewById(R.id.adView);
	    adView.loadAd(new AdRequest());
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					// mSystemUiHider.toggle();
				} else {
					// mSystemUiHider.show();
				}
			}
		});
		AllQ = new MP_AllQuestions(false, mf.CatPairId, this);
		setContent();
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		// findViewById(R.id.home_button).setOnTouchListener(
		// mDelayHideTouchListener);
		// findViewById(R.id.stats_button).setOnTouchListener(
		// mDelayHideTouchListener);
		// findViewById(R.id.filter_button).setOnTouchListener(
		// mDelayHideTouchListener);
		// findViewById(R.id.content).setOnTouchListener(mDelayHideTouchListener);
		// findViewById(R.id.home_button).setOnClickListener(this);
		// findViewById(R.id.stats_button).setOnClickListener(this);
		// findViewById(R.id.filter_button).setOnClickListener(this);
		removeProgress();
	}

	private void setContent() {
		MP_Item W;
		MP_Button currBtn;
		// Button currBtn;
		LinearLayout l = (LinearLayout) findViewById(R.id.content);

		TextView tv = (TextView) findViewById(R.id.filterLabel);
		tv.setText(mf.getFilterTitle());

		GetOneQuestion();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (AllQ != null) {
			AllQ.EndSession();

		}
		AllQ = null;
		removeProgress();
	}

	private void BuildLayout() {
		LinearLayout l = (LinearLayout) findViewById(R.id.content);

		String sInfoText = "";
		iCtrlId++;
		iCtrlMinorId = 0;
		iCtrlMinorId++;
		float txtSize = 30;
		sCtrlId = sCtrlIdBase + iCtrlId + iCtrlMinorId;

		clearContent();
		TextView txtBlock = (TextView) findViewById(R.id.textBlock);
		LinearLayout.LayoutParams params;
		switch (myQ.getQuestionType()) {
		case ListenSelect:
			// render two buttons for selection one play button

			sInfoText = "Listen the word and select the right option";
			txtBlock.setText(sInfoText);

			ImageButton playBtn = new ImageButton(this);
			playBtn.setBackgroundColor(getResources().getColor(R.color.bckg));
			playBtn.setImageDrawable(getResources()
					.getDrawable(R.drawable.play));
			playBtn.setBackgroundResource(R.xml.border);
			params = new LinearLayout.LayoutParams(playSize,
					playSize, 1f);
			playBtn.setScaleType(ScaleType.CENTER_INSIDE);
			playBtn.setPadding(5, 5, 5, 5);
			playBtn.setLayoutParams(params);
			playBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Play(v);
				}
			});
			if (myQ.getCorrectAnswer() == 1) {
				playBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP1);
			} else {
				playBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP2);
			}
			l.addView(playBtn);

			// currBtn.Tap += new
			// EventHandler<System.Windows.Input.GestureEventArgs>(Play);
			// Grid.SetColumn(currBtn, 0);
			// Grid.SetColumnSpan(currBtn, 2);
			// Grid.SetRow(currBtn, 1);
			// Content.Children.Add(currBtn);

			LinearLayout cd = new LinearLayout(this);
			cd = new LinearLayout(this);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					200, 1f);
			cd.setLayoutParams(params);
			cd.setOrientation(LinearLayout.HORIZONTAL);
			cd.setGravity(Gravity.CENTER);

			int MaxHeight = 220;
			int MaxWidth = ML_Main.DAS.dX / 2 - 3;
			int pid = myQ.getPairId();
			Enumeration<Integer> keys = mf.MyPairs.keys();
			Integer id = -1;
			while (keys.hasMoreElements()) {
				id = mf.MyPairs.get(keys.nextElement());
				if (id.intValue() == pid)
					break;
			}

			MP_Item W = ML_Main.ItemList.get(ML_Main.ItemPairs.get(id).iP1);
			currBtn = new MP_Button(this);
			btns[0] = currBtn;
			// bkgColor = ((ColorDrawable) currBtn.getBackground()).getColor();
			currBtn.setBackgroundResource(R.xml.border);
			currBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// iItemId = v.getId();
					SelectAnswer(v);
				}
			});
			currBtn.setMinimumHeight(MaxHeight);
			currBtn.setMinimumWidth(MaxWidth);
			currBtn.setHeight(MaxHeight);
			currBtn.setWidth(MaxWidth);
			currBtn.setText(W.get_Caption());
			currBtn.setId(1);// ML_Main.ItemPairs.get(myQ.getPairId()).iP1);
			// try{
			if (W.get_Image() != null && W.get_Image().length() > 0) {
				try {
					Drawable d = Drawable
							.createFromStream(
									getAssets().open(
											"img/"
													+ W.get_Image()
															.toLowerCase()),
									null);
					// currBtn.setExampleDrawable(d);
					currBtn.setCompoundDrawablesWithIntrinsicBounds(null, d,
							null, null);
					// currBtn.setImageDrawable(d);
				} catch (Exception e) {
					currBtn.setCompoundDrawablesWithIntrinsicBounds(null,
							getResources().getDrawable(R.drawable.ic_launcher),
							null, null);
				}

			}

			// currBtn.setAudio(W.get_Audio());
			cd.addView(currBtn);
			W = ML_Main.ItemList.get(ML_Main.ItemPairs.get(id).iP2);
			// W = ML_Main.ItemList.get(ML_Main.ItemPairs.get(mf.MyPairs.get(myQ
			// .getPairId())).iP2);

			// currBtn = new MP_Button(this);
			currBtn = new MP_Button(this);
			btns[1] = currBtn;
			currBtn.setBackgroundResource(R.xml.border);
			currBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// iItemId = v.getId();
					SelectAnswer(v);
				}
			});
			currBtn.setMinimumHeight(MaxHeight);
			currBtn.setMinimumWidth(MaxWidth);
			currBtn.setHeight(MaxHeight);
			currBtn.setWidth(MaxWidth);
			// currBtn.setExampleString( W.get_Caption());
			currBtn.setText(W.get_Caption());
			currBtn.setId(2);
			if (W.get_Image() != null && W.get_Image().length() > 0) {
				try {
					Drawable d = Drawable
							.createFromStream(
									getAssets().open(
											"img/"
													+ W.get_Image()
															.toLowerCase()),
									null);
					// currBtn.setExampleDrawable(d);
					// currBtn.setImageDrawable(d);
					currBtn.setCompoundDrawablesWithIntrinsicBounds(null, d,
							null, null);
				} catch (Exception e) {
					currBtn.setCompoundDrawablesWithIntrinsicBounds(null,
							getResources().getDrawable(R.drawable.ic_launcher),
							null, null);
				}
			}

			cd.addView(currBtn);

			l.addView(cd);

			answerBtn = new Button(this);
			answerBtn.setText("Answer");
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					100, 1f);
			answerBtn.setId(ANSWER_ID);
			answerBtn.setLayoutParams(params);
			answerBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Answer(v);
				}
			});
			if (myQ.getCorrectAnswer() == 1) {
				answerBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP1);
			} else {
				answerBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP2);
			}
			l.addView(answerBtn);

			break;
		case ListenType:

			// PracticeInfo.Text = "Listen to a word and type it";
			sInfoText = "Listen to a word and type it";
			txtBlock.setText(sInfoText);

			playBtn = new ImageButton(this);
			playBtn.setBackgroundColor(getResources().getColor(R.color.bckg));
			playBtn.setImageDrawable(getResources()
					.getDrawable(R.drawable.play));
			playBtn.setBackgroundResource(R.xml.border);
			params = new LinearLayout.LayoutParams(playSize,
					playSize, 1f);
			playBtn.setScaleType(ScaleType.CENTER_INSIDE);
			playBtn.setPadding(5, 5, 5, 5);
			playBtn.setLayoutParams(params);
			if (myQ.getCorrectAnswer() == 1) {
				playBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP1);
			} else {
				playBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP2);
			}
			playBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Play(v);
				}
			});
			l.addView(playBtn);

			TextView label = new TextView(this);
			label.setText("Answer:");
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					50, 1f);
			label.setLayoutParams(params);
			l.addView(label);
			input = new EditText(this);
			input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			input.setOnEditorActionListener(this);

			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					120, 1f);
			input.setLayoutParams(params);
			input.setBackgroundColor(Color.WHITE);
			input.setTextColor(Color.BLACK);

			input.setId(1000);
			l.addView(input);

			answerBtn = new Button(this);
			answerBtn.setId(ANSWER_ID);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					100, 1f);
			answerBtn.setLayoutParams(params);
			if (myQ.getCorrectAnswer() == 1) {
				answerBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP1);
			} else {
				answerBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP2);
			}
			answerBtn.setText("Answer");
			answerBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Answer(v);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			});
			l.addView(answerBtn);

			break;

		case ReadListenSelect:

			if (myQ.getCorrectAnswer() == 1) {
				sInfoText = "Listen to both words and select the one saying "
						+ ML_Main.ItemList.get(
								ML_Main.ItemPairs.get(myQ.getPairId()).iP1)
								.get_Caption();
			} else {
				sInfoText = "Listen to both words and select the one saying "
						+ ML_Main.ItemList.get(
								ML_Main.ItemPairs.get(myQ.getPairId()).iP2)
								.get_Caption();
			}

			txtBlock.setText(sInfoText);
			
			cd = new LinearLayout(this);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, 0.2f);
			cd.setLayoutParams(params);
			cd.setOrientation(LinearLayout.HORIZONTAL);
			cd.setGravity(Gravity.CENTER);

			playBtn = new ImageButton(this);
			playBtn.setBackgroundColor(getResources().getColor(R.color.bckg));
			playBtn.setImageDrawable(getResources()
					.getDrawable(R.drawable.play));
			playBtn.setBackgroundResource(R.xml.border);
			params = new LinearLayout.LayoutParams(playSize,
					playSize, 1f);
			playBtn.setScaleType(ScaleType.CENTER_INSIDE);
			playBtn.setPadding(5, 5, 5, 5);
			playBtn.setLayoutParams(params);

			playBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP1);

			playBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Play(v);
				}
			});
			cd.addView(playBtn);

			playBtn = new ImageButton(this);
			playBtn.setBackgroundColor(getResources().getColor(R.color.bckg));
			playBtn.setImageDrawable(getResources()
					.getDrawable(R.drawable.play));
			playBtn.setBackgroundResource(R.xml.border);
			params = new LinearLayout.LayoutParams(playSize,
					playSize, 1f);
			playBtn.setScaleType(ScaleType.CENTER_INSIDE);
			playBtn.setPadding(5, 5, 5, 5);
			playBtn.setLayoutParams(params);
			playBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP2);

			playBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Play(v);
				}
			});
			cd.addView(playBtn);

			l.addView(cd);

			cd = new LinearLayout(this);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, 0.4f);
			cd.setLayoutParams(params);
			cd.setOrientation(LinearLayout.HORIZONTAL);
			cd.setGravity(Gravity.CENTER);

			RadioGroup rg = new RadioGroup(this);
			rg.setOrientation(RadioGroup.HORIZONTAL);
			rg.setGravity(Gravity.CENTER);
			params = new LinearLayout.LayoutParams(playSize,
					playSize, 1f);
			
			rg.setId(1000);
			RadioButton r1 = new RadioButton(this);
			r1.setButtonDrawable(R.xml.mp_checkbox);
			r1.setMinimumWidth(200);
//			r1.setPadding(50, 50, 50, 50);
			r1.setLayoutParams(params);
			// r1.setText("Word 1");
			// r1.setTextSize(txtSize);
			r1.setId(1);// ML_Main.ItemPairs.get(myQ.getPairId()).iP1);
			rg.addView(r1);

			RadioButton r2 = new RadioButton(this);
			// r2.setText("Word 2");
			// r2.setTextSize(txtSize);
			r2.setId(2);// ML_Main.ItemPairs.get(myQ.getPairId()).iP2);
			r2.setButtonDrawable(R.xml.mp_checkbox);
			r2.setMinimumWidth(200);
//			r2.setPadding(50, 50, 50, 50);
			r2.setLayoutParams(params);
			rg.addView(r2);
			cd.addView(rg);
			l.addView(cd);

			answerBtn = new Button(this);
			answerBtn.setId(ANSWER_ID);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, 0.2f);
			answerBtn.setLayoutParams(params);
			if (myQ.getCorrectAnswer() == 1) {
				answerBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP1);
			} else {
				answerBtn.setId(ML_Main.ItemPairs.get(myQ.getPairId()).iP2);
			}
			answerBtn.setText("Answer");
			answerBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Answer(v);
				}
			});
			l.addView(answerBtn);

			break;
		}

	}

	private void Play(View sender) {
		MP_Button btnPlay1;
		String sAudioFile;
		int iItemId = 0;
		// btnPlay1 = (MP_Button) sender;
		iItemId = sender.getId();

		sAudioFile = ML_Main.ItemList.get(iItemId).get_Audio();
		try {
			if (player != null && player.isPlaying()) {
				player.stop();
				player.release();

			}
		} catch (IllegalStateException e) {
		}
		player = new MediaPlayer();
		String path = "raw/" + sAudioFile;
		AssetFileDescriptor descriptor;
		try {
			descriptor = getAssets().openFd(path);

			player.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

					mp.release();

				};
			});
			player.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			player.prepare();
			player.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void Answer(View sender) {

		int iAnswer = 0;
		String sAnswer = "";
		switch (myQ.getQuestionType()) {
		case ListenSelect:

			iAnswer = iItemId;
			break;
		case ListenType:
			sAnswer = input.getText().toString();
			if (myQ.getCorrectAnswer() == 1) {
				if (sAnswer
						.toLowerCase()
						.equals(ML_Main.ItemList
								.get(ML_Main.ItemPairs.get(myQ.getPairId()).iP1)
								.get_Caption().toLowerCase())) {
					iAnswer = 1;
				}
			} else {
				if (sAnswer
						.toLowerCase()
						.equals(ML_Main.ItemList
								.get(ML_Main.ItemPairs.get(myQ.getPairId()).iP2)
								.get_Caption().toLowerCase())) {
					iAnswer = 2;
				}
			}
			break;
		case ReadListenSelect:
			RadioGroup rg = (RadioGroup) findViewById(1000);
			iAnswer = rg.getCheckedRadioButtonId();
			break;
		}
		if (iAnswer == myQ.getCorrectAnswer()) {
			myQ.CheckAnswer(true);
			answerBtn.setBackgroundColor(Color.GREEN);
			Toast t = Toast
					.makeText(this, "Correct Answer", Toast.LENGTH_SHORT);
			TextView v = (TextView) t.getView().findViewById(
					android.R.id.message);
			v.setTextColor(Color.GREEN);
			v.setTextSize(20);
			t.show();// MessageBox.Show("Correct Answer");
			if (AllQ != null)
				AllQ.SaveAnswer(myQ);

			GetOneQuestion();
		} else {
			myQ.CheckAnswer(false);
			answerBtn.setBackgroundColor(Color.RED);
			Toast t = Toast.makeText(this, "Incorrect Answer",
					Toast.LENGTH_SHORT);
			TextView v = (TextView) t.getView().findViewById(
					android.R.id.message);
			v.setTextColor(Color.RED);
			v.setTextSize(20);
			t.show();
			if (AllQ != null)
				AllQ.SaveAnswer(myQ);
			cnt++;
			if (cnt >= 3) {
				cnt = 0;
				GetOneQuestion();
			} else {
				BuildLayout();
			}
		}

	}

	private void SelectAnswer(View sender) {
		// deal with highlighting
		for (int i = 0; i < btns.length; i++) {
			btns[i].setBackgroundColor(getResources().getColor(R.color.bckg));
		}

		MP_Button btn = (MP_Button) sender;
		btn.setBackgroundColor(getResources().getColor(R.color.black_overlay));
		// btn.BorderBrush = btnBrdHigh;
		// btn.BorderThickness = new Thickness(3);
		// btn.Background = btnBrdHigh;

		// set the selected answer.
		iItemId = sender.getId();
	}

	private void GetOneQuestion() {
		myQ = AllQ.GetOneQuestion();
		BuildLayout();
		// myQ.StartTiming();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			// mSystemUiHider.hide();
		}
	};

	private MediaPlayer player;

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// case R.id.home_button:
		// Intent i = new Intent(PracticeActivity.this, MinPairsActivity.class);
		// startActivity(i);
		// finish();
		// break;
		// case R.id.stats_button:
		// i = new Intent(PracticeActivity.this, StatsActivity.class);
		// startActivity(i);
		// finish();
		// break;
		// case R.id.filter_button:
		// showFilter();
		// break;
		default:
			try {
				if (v.getClass().equals(MP_Button.class)) {
					MP_Button b = (MP_Button) v;
					try {
						if (player != null && player.isPlaying()) {
							player.stop();
							player.release();

						}
					} catch (IllegalStateException e) {
					}
					player = new MediaPlayer();
					String path = "raw/" + b.getAudio();
					AssetFileDescriptor descriptor = getAssets().openFd(path);

					player.setDataSource(descriptor.getFileDescriptor(),
							descriptor.getStartOffset(), descriptor.getLength());
					descriptor.close();
					player.setOnCompletionListener(new OnCompletionListener() {
						public void onCompletion(MediaPlayer mp) {
							mp.release();

						};
					});
					player.prepare();
					player.start();
				}

			} catch (ClassCastException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

	}

	private void clearContent() {
		LinearLayout l = (LinearLayout) findViewById(R.id.content);
		l.removeAllViews();
	}

	@Override
	protected void ReloadContent() {
		// TODO Auto-generated method stub
		clearContent();
		AllQ = new MP_AllQuestions(false, mf.CatPairId, this);
		setContent();
	}

	@Override
	public boolean onEditorAction(TextView edittext, int keyCode, KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN)
				&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			// hide virtual keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
			Answer(edittext);
			return true;
		}
		return false;
	}
	@Override
	  public void onDestroy() {
	    if (adView != null) {
	      adView.destroy();
	    }
	    super.onDestroy();
	  }
}
