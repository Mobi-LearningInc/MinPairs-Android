package com.mobilearning.minpairs;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import com.iguanaui.controls.DataChart;
import com.iguanaui.controls.Series;
import com.iguanaui.controls.TrendlineType;
import com.iguanaui.controls.axes.CategoryAxis;
import com.iguanaui.controls.axes.CategoryXAxis;
import com.iguanaui.controls.axes.NumericAxis;
import com.iguanaui.controls.axes.NumericYAxis;
import com.iguanaui.controls.valuecategory.AreaSeries;
import com.iguanaui.controls.valuecategory.ColumnSeries;
import com.iguanaui.controls.valuecategory.LineSeries;
import com.iguanaui.controls.valuecategory.ValueCategorySeries;


import com.mobilearning.minpairs.util.ML_Main;
import com.mobilearning.minpairs.util.MPQuestionType;
import com.mobilearning.minpairs.util.MP_Category;
import com.mobilearning.minpairs.util.MP_Item;
import com.mobilearning.minpairs.util.SoudWordFlipper;
import com.mobilearning.minpairs.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler.Value;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class StatsActivity extends MPActivity {
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

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
//	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_stats);
        createData();
        
        createChart();
        createUI();
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
//						if (visible && AUTO_HIDE) {
//							// Schedule a hide().
//							delayedHide(AUTO_HIDE_DELAY_MILLIS);
//						}
//					}
//				});
//
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

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
//		findViewById(R.id.home_button).setOnTouchListener(
//				mDelayHideTouchListener);
//		findViewById(R.id.home_button).setOnClickListener(this);
//		findViewById(R.id.filter_button).setOnClickListener(this);
		removeProgress();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
//			case R.id.home_button:
//                Intent i = new Intent(StatsActivity.this, MinPairsActivity.class);
//                startActivity(i);
//                finish();
//                break;
//			case R.id.filter_button:
//				showFilter();
//                break;
            default:
            	
            	break;
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

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	private void createData() {
//		MPQuestionType[] types = MPQuestionType.values();
//		for(int i=0; i<types.length; i++){
//			column1.add(ML_Main.getQTypeSeries(types[i]));
//		}
		column1.add(ML_Main.getQTypeSeries(MPQuestionType.All));
		categories = ML_Main.getSessionsLabels();
//        Random random=new Random();
//        float value1=25.0f;
//        float value2=25.0f;
//        
//        for(int i=0; i<1000; ++i) {
//        	value1+=(random.nextFloat()-0.5f);
//        	value2+=(random.nextFloat()-0.5f);
//        	
//        	categories.add(Integer.toString(i));
//        	column1.add(value1);
//        	column2.add(value2);
//        }		
	}
	private void updateData() {
		
//		MPQuestionType[] types = MPQuestionType.values();
//		
//		for(int i=0; i<types.length; i++){
//			column1.add(ML_Main.getQTypeSeries(types[i]));
//		}
//		categories = ML_Main.getSessionsLabels();
//        Random random=new Random();
//        float value1=25.0f;
//        float value2=25.0f;
//        
//        for(int i=0; i<categories.size(); ++i) {
//        	value1+=(random.nextFloat()-0.5f);
//        	value2+=(random.nextFloat()-0.5f);
//        	
//        	column1.set(i, value1);
//        	column2.set(i, value2);
//        }
	}

	private void createChart() {
		
		dataChart=(DataChart)findViewById(R.id.dataChart);	// get the empty chart view from the activity layout
        dataChart.setHorizontalZoomEnabled(true);			// allow horizontal zooming
        dataChart.setVerticalZoomEnabled(false);			// don't allow vertical zooming

        // set up an x axis
        
        CategoryXAxis categoryAxis=new CategoryXAxis(); 
        categoryAxis.setDataSource(categories);			// tell the axis about the data table
        categoryAxis.setLabelFormatter(new CategoryAxis.LabelFormatter() {
			public String format(CategoryAxis axis, Object item) {
				return (String)item;						// return the axis item as a string
			}
		});
        dataChart.scales().add(categoryAxis);				// all axes must be added to the chart's scales collection
     
        // set up a y axis
        
        NumericYAxis valueAxis=new NumericYAxis();
        valueAxis.setMinimumValue(0.0f);					// the random data look much nicer with a fixed axis range
        valueAxis.setMaximumValue(110.0f);					// the random data look much nicer with a fixed axis range
        valueAxis.setLabelFormatter(new NumericAxis.LabelFormatter() {
			public String format(NumericAxis axis, float item, int precision) {
				if(precision!=numberFormat.getMinimumFractionDigits()) {
					numberFormat.setMinimumFractionDigits(precision);	// set the formatting precision
					numberFormat.setMaximumFractionDigits(precision);	// set the formatting precision
				}
				
				return numberFormat.format(item);						// return item as a string
			}
        
        	final NumberFormat numberFormat=NumberFormat.getInstance();	// numeric formatter for axis values
        });
        dataChart.scales().add(valueAxis);					// all axes must be added to the chart's scales collection
 
        {
	        ValueCategorySeries series=new ColumnSeries();	
	        series.setCategoryAxis(categoryAxis);			// tell the series its x axis
	        series.setValueAxis(valueAxis);					// tell the series its y axis
	        series.setValueMember("");						// tell the series the data rows are the values
	        series.setDataSource(column1);					// tell the series the data table
	        
	        dataChart.series().add(series);					// add the series to the chart
        }
		
//		dataChart=(DataChart)findViewById(R.id.dataChart);	// get the empty chart view from the activity layout
//        dataChart.setHorizontalZoomEnabled(true);			// allow horizontal zooming
//        dataChart.setVerticalZoomEnabled(false);			// don't allow vertical zooming
//
//        // set up an x axis
//        
//        CategoryXAxis categoryAxis=new CategoryXAxis(); 
//        categoryAxis.setDataSource(categories);				// tell the axis about the data table
//        categoryAxis.setLabelFormatter(new CategoryAxis.LabelFormatter() {
//			public String format(CategoryAxis axis, Object item) {
//				return (String)item;						// return the axis item as a string
//			}
//		});
//        dataChart.scales().add(categoryAxis);				// all axes must be added to the chart's scales collection
//     
//        // set up a y axis
//        
//        NumericYAxis valueAxis=new NumericYAxis();
//        valueAxis.setMinimumValue(0.0f);					// the random data look much nicer with a fixed axis range
//        valueAxis.setMaximumValue(100.0f);					// the random data look much nicer with a fixed axis range
//        valueAxis.setLabelFormatter(new NumericAxis.LabelFormatter() {
//			public String format(NumericAxis axis, float item, int precision) {
//				if(precision!=numberFormat.getMinimumFractionDigits()) {
//					numberFormat.setMinimumFractionDigits(precision);	// set the formatting precision
//					numberFormat.setMaximumFractionDigits(precision);	// set the formatting precision
//				}
//				
//				return numberFormat.format(item);						// return item as a string
//			}
//        
//        	final NumberFormat numberFormat=NumberFormat.getInstance();	// numeric formatter for axis values
//        });
//        dataChart.scales().add(valueAxis);					// all axes must be added to the chart's scales collection
//        dataChart.setBackgroundColor(Color.BLACK);
//		MPQuestionType[] types = MPQuestionType.values();
//		for(int i=0; i<column1.size(); i++)
//        {
//	        ValueCategorySeries series=new LineSeries();	
//	        series.setCategoryAxis(categoryAxis);			// tell the series its x axis
//	        series.setValueAxis(valueAxis);					// tell the series its y axis
//	        series.setValueMember("");						// tell the series the data rows are the values
//	        series.setDataSource(column1.get(i));					// tell the series the data table
//	        series.setThickness(3);
//	        
//	        dataChart.series().add(series);					// add the series to the chart
//        }
//        
//        {
//	        ValueCategorySeries series=new LineSeries();	
//	        series.setCategoryAxis(categoryAxis);			// tell the series its x axis
//	        series.setValueAxis(valueAxis);					// tell the series its y axis
//	        series.setValueMember("");						// tell the series the data rows are the values
//	        series.setDataSource(column2);					// tell the series the data table
//	        
//	        dataChart.series().add(series);					// add the series to the chart
//        }
	}
	private void createUI() {
		// the trendlines spinner
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.trendlineTypes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    

        Enumeration< MP_Category> sounds = ML_Main.CatList.elements();
        ArrayList<String> caps = new ArrayList<String>();
        while(sounds.hasMoreElements())
        	caps.add(sounds.nextElement().getCaption());
        ArrayAdapter<?> soundAdapter = 
        		new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, caps);//(this, R.array.trendlineTypes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    

        
        Spinner trendlineSpinner=(Spinner)findViewById(R.id.trendlineSpinner);
//        Spinner soundSpinner=(Spinner)findViewById(R.id.soundSpinner);
        
        trendlineSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				TrendlineType trendlineType=TrendlineType.valueOf(((String)(parent.getItemAtPosition(position))).toUpperCase());
				MPQuestionType[] types = MPQuestionType.values();
				column1.clear();
				column1.add(ML_Main.getQTypeSeries(types[position]));
				categories = ML_Main.getSessionsLabels();
				updateData();
				int i = 0;
				for(Series series: dataChart.series()) {
					series.setDataSource(column1.get(i));
					series.notifyDataReset();
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				column1.clear();
				column1.add(ML_Main.getQTypeSeries(MPQuestionType.All));
				categories = ML_Main.getSessionsLabels();
				updateData();
				int i = 0;
				for(Series series: dataChart.series()) {
					series.setDataSource(column1.get(i));
					series.notifyDataReset();
				}
			} 
		});

//        soundSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////				TrendlineType trendlineType=TrendlineType.valueOf(((String)(parent.getItemAtPosition(position))).toUpperCase());
//				
//				Enumeration< MP_Category> sounds = ML_Main.CatList.elements();
//				Enumeration<Integer> keys = ML_Main.CatList.keys();
//				
//				int i = 0;
//				column1.clear();
//				column1.add(ML_Main.getSoundSeries(sounds.nextElement().getId()));
//				categories = ML_Main.getSessionsLabels();
//				updateData();
//				
//				for(Series series: dataChart.series()) {
//					series.setDataSource(column1.get(i));
//					series.notifyDataReset();
//					i++;
//				}
//			}
//
//			public void onNothingSelected(AdapterView<?> arg0) {
//			} 
//		});
        
        trendlineSpinner.setPrompt("Question Type");
        trendlineSpinner.setAdapter(adapter);
        
//        soundSpinner.setPrompt("Sound");
//        soundSpinner.setAdapter(soundAdapter);
//		// the update button
//		
//        Button updateButton=(Button)findViewById(R.id.updateButton);
//        
//        updateButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
//				updateData();
//
//				for(Series series: dataChart.series()) {
//					series.notifyDataReset();
//				}
//			}
//		});
	}
	
	private DataChart dataChart;
	private List<String> categories=new ArrayList<String>();
	private ArrayList<ArrayList<Float>> column1=new ArrayList<ArrayList<Float>>();
//	private List<Float> column2=new ArrayList<Float>();

	@Override
	protected void ReloadContent() {
		// TODO Auto-generated method stub
		
	}
}
