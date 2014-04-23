package com.mobilearning.minpairs;

import java.util.Locale;

import com.mobilearning.minpairs.util.MP_AllQuestions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class AboutActivity extends FragmentActivity implements OnEditorActionListener{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}
	private Dialog mInfoDialog;
	
	private boolean sendEmail(){
		String body = ((EditText)findViewById(R.id.Body)).getText()+"\n"+
						((EditText)findViewById(R.id.editName)).getText();
		
		
		Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from MinimalPairs ...");
		intent.putExtra(Intent.EXTRA_TEXT   , body);
		intent.setData(Uri.parse("mailto:pawluk@gmail.com")); // or just "mailto:" for blank
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		try {
		    startActivity(Intent.createChooser(intent, "Send mail..."));
		    return true;
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(AboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		    return false;
		}

	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.feedbackCancelBtn){
			clear();
		}
		if(v.getId()==R.id.feedbackSubmitBtn){
			mInfoDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Thank You").setMessage(" Your message has been sent.\nThank you for your feedback.")
	        .setIcon(android.R.drawable.ic_dialog_email)
	        .show();
			sendEmail();
			clear();
			finish();
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					removeInfoScreen();
				}
			}, 2000);
			 InputMethodManager imm = 
		              (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		           imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//			v.focusSearch(View.GONE);
		}
	}
	
	protected void removeInfoScreen() {
		if (mInfoDialog != null) {
			mInfoDialog.dismiss();
			mInfoDialog = null;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_home:
			if(!this.getClass().equals(MinPairsActivity.class))
				finish();
			break;		
		}

		return super.onOptionsItemSelected(item);
	}
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch (position) {
			case 0:
				Fragment fragment = new AboutAppFragment();
				Bundle args = new Bundle();
				args.putInt(AboutAppFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;				
			case 1:
				fragment = new AboutCompanyFragment();
				args = new Bundle();
				args.putInt(AboutAppFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;				

			case 2:
				fragment = new AboutFeedbackFragment();
				args = new Bundle();
				args.putInt(AboutAppFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;				


			default:
				fragment = new AboutAppFragment();
				args = new Bundle();
				args.putInt(AboutAppFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;
			}

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Application";
			case 1:
				return "Company";//getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return "Feedback";//getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class AboutAppFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public AboutAppFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.about_app,
					container, false);
//			TextView dummyTextView = (TextView) rootView
//					.findViewById(R.id.section_label);
//			dummyTextView.setText("About App");
			return rootView;
		}
	}
	public static class AboutCompanyFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public AboutCompanyFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.about_company,
					container, false);
//			TextView dummyTextView = (TextView) rootView
//					.findViewById(R.id.section_label);
//			dummyTextView.setText("About App");
			return rootView;
		}
	}
	public static class AboutFeedbackFragment extends Fragment implements OnClickListener{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public AboutFeedbackFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.about_feedback,
					container, false);

//			Button b = (Button) getActivity().findViewById(R.id.feedbackCancelBtn);
//			b.setOnClickListener(this);
//			b = (Button) getActivity().findViewById(R.id.feedbackSubmitBtn);
//			b.setOnClickListener(this);

			return rootView;
		}

		
		

		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		}
	}
	private void clear(){
		EditText t = (EditText) findViewById(R.id.Body);
		t.setText("");
		t = (EditText) findViewById(R.id.editEmail);
		t.setText("");
		t = (EditText) findViewById(R.id.editName);
		t.setText("");
//		t = (EditText) findViewById(R.id.editSubject);
//		t.setText("");
		
	}
	
	@Override
	public boolean onEditorAction(TextView edittext, int keyCode,
			KeyEvent event) {
		if(edittext.getId()!=R.id.Body)
		if ( (event.getAction() == KeyEvent.ACTION_DOWN  ) &&
	             (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)   )
	        {               
	           // hide virtual keyboard
	           InputMethodManager imm = 
	              (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	           imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
	           edittext.focusSearch(View.FOCUS_FORWARD);
	           return true;
	        }
	        return false;
	}
}
