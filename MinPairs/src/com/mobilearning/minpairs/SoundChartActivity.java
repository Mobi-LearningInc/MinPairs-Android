package com.mobilearning.minpairs;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

import com.mobilearning.minpairs.util.ML_Main;
import com.mobilearning.minpairs.util.MP_Category;
import com.mobilearning.minpairs.util.MP_Item;
import com.mobilearning.minpairs.util.SoudWordFlipper;
import com.mobilearning.minpairs.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SoundChartActivity extends MPActivity implements OnPreparedListener, OnCompletionListener{
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = false;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	private MediaPlayer player = new MediaPlayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sound_chart);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		
		final View contentView = findViewById(R.id.fullscreen_content);
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
//		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
//				HIDER_FLAGS);
//		mSystemUiHider.setup();
//		mSystemUiHider
//				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
//					// Cached values.
//					int mControlsHeight;
//					int mShortAnimTime;
//
//					@Override
//					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//					public void onVisibilityChange(boolean visible) {
//						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//							// If the ViewPropertyAnimator API is available
//							// (Honeycomb MR2 and later), use it to animate the
//							// in-layout UI controls at the bottom of the
//							// screen.
//							if (mControlsHeight == 0) {
//								mControlsHeight = controlsView.getHeight();
//							}
//							if (mShortAnimTime == 0) {
//								mShortAnimTime = getResources().getInteger(
//										android.R.integer.config_shortAnimTime);
//							}
//							controlsView
//									.animate()
//									.translationY(visible ? 0 : mControlsHeight)
//									.setDuration(mShortAnimTime);
//						} else {
//							// If the ViewPropertyAnimator APIs aren't
//							// available, simply show or hide the in-layout UI
//							// controls.
//							controlsView.setVisibility(visible ? View.VISIBLE
//									: View.GONE);
//						}
//
//						if (false){//visible && AUTO_HIDE) {
//							// Schedule a hide().
//							delayedHide(AUTO_HIDE_DELAY_MILLIS);
//						}
//					}
//				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
//		findViewById(R.id.fullscreen_content_controls).setOnTouchListener(
//				mDelayHideTouchListener);
		GridLayout l = (GridLayout) findViewById(R.id.gridl);
		Enumeration< Integer> cats = ML_Main.CatList.keys();
		while (cats.hasMoreElements()) {
			Integer i = cats.nextElement();
			MP_Category c = ML_Main.CatList.get(i);
/*			Button b = new Button(this);
			Resources res = getResources();
			b.setWidth((int) res.getDimension(R.dimen.btnWdth));
			b.setHeight((int) res.getDimension(R.dimen.btnWdth));
			b.setTextSize((int) res.getDimension(R.dimen.btnTxtSize));
			b.setText(c.getCaption());
			b.setId(i);
			b.setOnClickListener(this);
*/
			SoudWordFlipper b = new SoudWordFlipper(this,c);
			b.setInAnimation(this, R.anim.grow_from_middle);
			b.setOutAnimation(this, R.anim.shrink_to_middle);
			b.setId(i);
			b.setCategory(c);
/**/
			l.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GridLayout l = (GridLayout)v;
					for (int j = 0; j < l.getChildCount(); j++) {
						SoudWordFlipper f = (SoudWordFlipper)l.getChildAt(j);
						if(f.isWord())
							f.flipBack();
					}
				}
			});
			l.addView(b);			
		}
		
		
//		findViewById(R.id.home_button).setOnClickListener(this);
//		findViewById(R.id.stats_button).setOnClickListener(this);
		removeProgress();
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
				onClick(view);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
//			mSystemUiHider.hide();
		}
	};

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
		switch(v.getId()){
//			case R.id.home_button:
//                Intent i = new Intent(SoundChartActivity.this, MinPairsActivity.class);
//                startActivity(i);
//                finish();
//                break;
//			case R.id.stats_button:
//				 i = new Intent(SoundChartActivity.this, StatsActivity.class);
//                startActivity(i);
//                finish();
//                break;
            default:
            	MP_Category c = ML_Main.CatList.get(v.getId());
            	String path = "";
            	SoudWordFlipper flipper = ((SoudWordFlipper)v.getParent());
            	
            	if(flipper.isWord())
            		
            	{
            		MP_Item w = ((SoudWordFlipper)v.getParent()).nextWord();
            		if(w!=null){
            			 path = "raw/"+w.get_Audio();
            			 play(v, path);
            		}
            		
            	}
            	else
            	{
            		path = "raw/"+c.getAudio();
            		play(v, path);
            	}
            	flipper.showNext();
            	break;
		}
		
	}
	
	private void play(View v, String path){
		
		try {
			ViewFlipper flipper = (ViewFlipper)v.getParent();
			int id = flipper.getId();
			try{
			if (player!=null &&  player.isPlaying()) {
				 player.stop();
				 player.release();
				 player = null;
		        }
			}catch(IllegalStateException e){
				
			}
			player = null;
			player = new MediaPlayer();
			AssetFileDescriptor descriptor = getAssets().openFd(path);
			
			player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			player.setOnPreparedListener(this);
			player.prepare();
			player.setOnCompletionListener(new OnCompletionListener() {
			    public void onCompletion(MediaPlayer mp) {
			        mp.release();
			    	//player = null;

			    };
			});
			player.start();
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		player.start();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
//		player.release();
	}

	@Override
	protected void ReloadContent() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
