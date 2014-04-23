package com.mobilearning.minpairs;

import java.util.Dictionary;
import java.util.Enumeration;

import com.mobilearning.minpairs.util.ML_Main;
import com.mobilearning.minpairs.util.ML_Main.DeviceSize;
import com.mobilearning.minpairs.util.MP_Category;
import com.mobilearning.minpairs.util.OneFilter;
import com.mobilearning.minpairs.util.iCatPairs;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class MPActivity extends Activity implements OnClickListener {

	private static final int RESULT_SETTINGS = 1;
	protected Dialog mSplashDialog;
	protected Dialog mFilter;
	public static OneFilter mf;
	protected static Dialog mProgressDialog;

	private static boolean showInfo = true;

	public static boolean isShowInfo() {
		return showInfo;
	}

	public static void setShowInfo(boolean showInfo) {
		MPActivity.showInfo = showInfo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ML_Main.setAssetManager(getAssets());

		ML_Main.LoadItems();
		ML_Main.LoadCategories();
		ML_Main.LoadCatItemsMap();
		ML_Main.LoadCatPairs();
		ML_Main.LoadPairs();
		ML_Main.LoadData(this);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		ML_Main.DAS = new ML_Main.DeviceSize();
		ML_Main.DAS.dX = size.x;
		ML_Main.DAS.dY = size.y;

		if (mf == null){
			mf = new OneFilter();
//			showFilter();
		}
		if (mf.MyPairs.isEmpty()) {
			mf.AddNewMainSound(0);
			mf.AddSecondarySound(0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_home:
			if(!this.getClass().equals(MinPairsActivity.class))
				finish();
			break;
		case R.id.menu_stats:
			startActivity(new Intent(this, StatsActivity.class));
			if(!this.getClass().equals(MinPairsActivity.class))
				finish();
			break;
		case R.id.menu_filter:
			showFilter();
			break;
		case R.id.menu_settings:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;
		case R.id.menu_about:
			showAboutScreen();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Removes the Dialog that displays the splash screen
	 */
	protected void removeSplashScreen() {
		if (mSplashDialog != null) {
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}

	/**
	 * Removes the Dialog that displays the filter screen
	 */
	protected void removeFilterScreen() {
		if (mFilter != null) {
			mFilter.dismiss();
			// mFilter = null;
		}
	}

	/**
	 * Shows the splash screen over the full Activity
	 */
	protected void showSplashScreen() {
		mSplashDialog = new Dialog(this, R.style.SplashScreen);
		mSplashDialog.setContentView(R.layout.splashscreen);
		mSplashDialog.setCancelable(false);
		mSplashDialog.show();

		// Set Runnable to remove splash screen just in case
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				removeSplashScreen();
			}
		}, 4000);
	}

	

	/**
	 * Shows the splash screen over the full Activity
	 */
	protected void showAboutScreen() {
		Intent i = new Intent(this, AboutActivity.class);
		startActivity(i);
	}
	
	
	protected void showProgress() {

		mProgressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER
				| ProgressDialog.STYLE_HORIZONTAL
				| ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle("Pleae wait ...");

		mProgressDialog.show();
	}

	protected void removeProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	protected void showFilter() {
		if (mFilter == null) {
			mFilter = new Dialog(this, R.style.FilterDialog);
			mFilter.setContentView(R.layout.filter_dialog);
			mFilter.setCancelable(true);
			mFilter.findViewById(R.id.filterOK).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							mFilter.dismiss();

							ReloadContent();
							TextView l = ((TextView) findViewById(R.id.filterLabel));
							if (l != null)
								l.setText(MPActivity.mf.getFilterTitle());
							if (showInfo) {
								Toast.makeText(
										v.getContext(),
										"The filter has been set and shared between all activities (practice and quizzes)",
										Toast.LENGTH_LONG).show();
								showInfo = !showInfo;
							}
						}
					});
			mFilter.findViewById(R.id.filterClear).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							mFilter.dismiss();
							mf.ClearFilter();

							ReloadContent();
							TextView l = ((TextView) findViewById(R.id.filterLabel));
							if (l != null)
								l.setText(MPActivity.mf.getFilterTitle());
							Toast.makeText(
									v.getContext(),
									"The filter has been cleared and shared between all activities (practice and quizzes)",
									Toast.LENGTH_LONG).show();
							if (!showInfo)
								showInfo = !showInfo;
						}
					});

			LinearLayout primaryL = (LinearLayout) mFilter
					.findViewById(R.id.primary);

			Enumeration<Integer> keys = ML_Main.CatList.keys();
			Enumeration<iCatPairs> elems = ML_Main.CatPairs.elements();
			while (keys.hasMoreElements()) {
				MP_Category c = ML_Main.CatList.get(keys.nextElement());// elems.nextElement().iCP1);//
				Button pb = new Button(this);
				pb.setText(c.getCaption());
				pb.setId(c.getId());
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				pb.setLayoutParams(lp);
				pb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						MPActivity.mf.AddNewMainSound(v.getId());
						Enumeration<Integer> skeys = MPActivity.mf.MySecCategories
								.keys();
						LinearLayout secondL = (LinearLayout) mFilter
								.findViewById(R.id.secondary);
						secondL.removeAllViews();
						TextView primLabel = (TextView) mFilter
								.findViewById(R.id.filterTitlePrim);
						MP_Category cmain = ML_Main.CatList.get(v.getId());
						primLabel.setText(cmain.getCaption());
						while (skeys.hasMoreElements()) {
							MP_Category c = ML_Main.CatList
									.get(MPActivity.mf.MySecCategories
											.get(skeys.nextElement()));
							Button sb = new Button(v.getContext());
							sb.setText(c.getCaption());
							sb.setId(c.getId());
							LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
							sb.setLayoutParams(lp);
							sb.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									TextView secLabel = (TextView) mFilter
											.findViewById(R.id.filterTitleSec);
									MP_Category cmain = ML_Main.CatList.get(v
											.getId());
									secLabel.setText(cmain.getCaption());
									MPActivity.mf.AddSecondarySound(v.getId());
								}
							});
							secondL.addView(sb);
						}
					}
				});
				primaryL.addView(pb);
			}

		}

		mFilter.show();

		// Set Runnable to remove splash screen just in case
		// final Handler handler = new Handler();
		// handler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// removeSplashScreen();
		// }
		// }, 4000);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.filterOK:
			removeFilterScreen();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			// showUserSettings();
			Toast.makeText(this, "OK", Toast.LENGTH_LONG);
			break;

		}

	}

	protected abstract void ReloadContent();

	public static Dictionary<Integer, Integer> clear(
			Dictionary<Integer, Integer> dict) {
		Enumeration keys = dict.keys();
		while (keys.hasMoreElements()) {
			dict.remove(keys.nextElement());
		}
		return dict;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		ML_Main.DAS = new ML_Main.DeviceSize();
		ML_Main.DAS.dX = size.x;
		ML_Main.DAS.dY = size.y;
	}

}
