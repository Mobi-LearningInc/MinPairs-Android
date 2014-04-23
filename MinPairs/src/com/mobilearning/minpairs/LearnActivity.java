package com.mobilearning.minpairs;

import java.io.IOException;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.mobilearning.minpairs.util.ML_Main;
import com.mobilearning.minpairs.util.MP_Item;
import com.mobilearning.minpairs.util.OneFilter;
import com.mobilearning.minpairs.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LearnActivity extends MPActivity {
	private AdView adView;
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
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_learn);

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
//					mSystemUiHider.toggle();
				} else {
//					mSystemUiHider.show();
				}
			}
		});
		
		setContent();
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
//		findViewById(R.id.home_button).setOnTouchListener(
//				mDelayHideTouchListener);
//		findViewById(R.id.stats_button).setOnTouchListener(mDelayHideTouchListener);
//		findViewById(R.id.filter_button).setOnTouchListener(mDelayHideTouchListener);
//		findViewById(R.id.content).setOnTouchListener(mDelayHideTouchListener);
//		findViewById(R.id.home_button).setOnClickListener(this);
//		findViewById(R.id.stats_button).setOnClickListener(this);
//		findViewById(R.id.filter_button).setOnClickListener(this);
		removeProgress();
	}

	private void setContent(){
		   MP_Item W;
		   MP_Button currBtn;
//		   Button currBtn;
           LinearLayout l = (LinearLayout)findViewById(R.id.content);
           
           TextView tv = (TextView) findViewById(R.id.filterLabel);
           tv.setText(mf.getFilterTitle());
           
           for (int iCnt = 1; iCnt <= mf.MyPairs.size(); iCnt++)
           {

//               RowDefinition rd = new RowDefinition();
               LinearLayout rd = new LinearLayout(this);
               rd.setGravity(Gravity.CENTER);
               int MaxHeight = 220;
               int MaxWidth = 220;
               
               W = ML_Main.ItemList.get(ML_Main.ItemPairs.get(mf.MyPairs.get(iCnt)).iP1);
//               currBtn = new MP_Button(this);
               
//               currBtn.setExampleString( W.get_Caption());
               currBtn = new MP_Button(this);
               currBtn.setOnClickListener(this);
               currBtn.setMinimumHeight(MaxHeight);
               currBtn.setMinimumWidth(MaxWidth);
               currBtn.setHeight(MaxHeight);
               currBtn.setWidth(MaxWidth);
               
               currBtn.setText(W.get_Caption());
               try{
               if (W.get_Image()!=null && W.get_Image().length()>0)
               {
            	   try{
                	   Drawable d = Drawable.createFromStream(getAssets().open("img/" + W.get_Image().toLowerCase()), null);           			
//                	   currBtn.setExampleDrawable(d);
                	   currBtn.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
//                	   currBtn.setImageDrawable(d);
            	   }catch(Exception e){
            		   currBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_launcher),
            				   null, null);
            	   }
                   
               }

               currBtn.setAudio(W.get_Audio());
               rd.addView(currBtn);
               W = ML_Main.ItemList.get(ML_Main.ItemPairs.get(mf.MyPairs.get(iCnt)).iP2);
               
//               currBtn = new MP_Button(this);
               currBtn = new MP_Button(this);
               currBtn.setOnClickListener(this);
               currBtn.setMinimumHeight(MaxHeight);
               currBtn.setMinimumWidth(MaxWidth);
               currBtn.setHeight(MaxHeight);
               currBtn.setWidth(MaxWidth);
//               currBtn.setExampleString( W.get_Caption());
               currBtn.setText(W.get_Caption());
               
               if (W.get_Image()!=null && W.get_Image().length()> 0)
               {	try{
            	   		Drawable d = Drawable.createFromStream(getAssets().open("img/" + W.get_Image().toLowerCase()), null);           			
//            	   		currBtn.setExampleDrawable(d);
//            	   	    currBtn.setImageDrawable(d);
            	   		currBtn.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
               		}catch(Exception e){
             		   currBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_launcher),
            				   null, null);
               		}
               }

               currBtn.setAudio(W.get_Audio());
               rd.addView(currBtn);
               l.addView(rd);
               }catch(Exception e){
            	   e.printStackTrace();
               }
           }
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
//			mSystemUiHider.hide();
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
		Intent i ;
		switch(v.getId()){
//			case R.id.home_button:
//                 i = new Intent(LearnActivity.this, MinPairsActivity.class);
//                startActivity(i);
//                finish();
//                break;
//			case R.id.stats_button:
//				 i = new Intent(LearnActivity.this, StatsActivity.class);
//                startActivity(i);
//                finish();
//                break;
//			case R.id.filter_button:
//				showFilter();
//				break;    
            default:
            	try{
            		if(v.getClass().equals(MP_Button.class)){
            			MP_Button b = (MP_Button)v;
            		try{	
            			if (player!=null && player.isPlaying()) {
           				 player.stop();
           				 player.release();
           				 
           		        }
            		}catch(IllegalStateException e){}
           			player = new MediaPlayer();
           			String path = "raw/"+b.getAudio();
           			AssetFileDescriptor descriptor = getAssets().openFd(path);
           			player.setOnCompletionListener(new OnCompletionListener() {
        			    public void onCompletion(MediaPlayer mp) {
        			        
        			    	mp.release();

        			    };
        			});
           			player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
           			descriptor.close();           			
           			player.prepare();
           			player.start();
            		}
            		
            	}catch(ClassCastException e){
            		e.printStackTrace();
            	} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
		}
		
	}

	private void clearContent(){
		LinearLayout l = (LinearLayout)findViewById(R.id.content);
		l.removeAllViews();
	}
	
	
	@Override
	protected void ReloadContent() {
		// TODO Auto-generated method stub
		clearContent();
		setContent();
	}
	@Override
	  public void onDestroy() {
	    if (adView != null) {
	      adView.destroy();
	    }
	    super.onDestroy();
	  }
}
