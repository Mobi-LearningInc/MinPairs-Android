package com.mobilearning.minpairs.util;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.mobilearning.minpairs.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ViewFlipper;

public class SoudWordFlipper extends ViewFlipper {

	private MP_Category category;
	private Button catBtn;
	private Button wordBtn;
	// private int id = 8989;
	private boolean flip = false;
	private Context context;
	private boolean isSound = true;
	private int wid;
	
	public SoudWordFlipper(Context context, MP_Category cat) {
		super(context);
		this.context = context;
		// this.setId(id);
		category = cat;
		catBtn = new Button(context);
		catBtn.setText(category.getCaption());
		catBtn.setOnClickListener((OnClickListener) context);
		catBtn.setId(cat.getId());
		Resources res = getResources();
		catBtn.setHeight((int)res.getDimension(R.dimen.btnWdth));
		catBtn.setWidth((int)res.getDimension(R.dimen.btnWdth));
		catBtn.setTextSize(res.getDimension(R.dimen.btnTxtSize));
		this.addView(catBtn);

		wordBtn = new Button(context);
		wid = category.GetNextWordId();
		MP_Item W = null;
		if(wid!=0){
			W = ML_Main.ItemList.get(wid);
			wordBtn.setText(W.get_Caption());
		}
		wordBtn.setOnClickListener((OnClickListener) context);
		wordBtn.setId(cat.getId());
		
		 if (W.get_Image()!=null && W.get_Image().length()>0)
         {
      	   try{
          	   Drawable d = Drawable.createFromStream(context.getAssets().open("img/" + W.get_Image().toLowerCase()), null);           			
//          	   currBtn.setExampleDrawable(d);
          	   wordBtn.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
//          	   currBtn.setImageDrawable(d);
      	   }catch(Exception e){
      		   wordBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_launcher),
      				   null, null);
      	   }
             
         }
		wordBtn.setHeight((int)res.getDimension(R.dimen.btnWdth));
		wordBtn.setWidth((int)res.getDimension(R.dimen.btnWdth));
		wordBtn.setTextSize(res.getDimension(R.dimen.btnTxtSize)/2);
		this.addView(wordBtn);
	}

	public MP_Category getCategory() {
		return category;
	}

	public void setCategory(MP_Category category) {
		this.category = category;
	}

	public MP_Item nextWord() {
		wid = category.GetNextWordId();
		return ML_Main.ItemList.get(wid);
	}

	public boolean isWord(){
		return !isSound;
	}
	
	@Override
	public void showNext() {
		// TODO Auto-generated method stub
//		if (flip) {
//			flip = false;			
			super.showNext();
			isSound = !isSound;
				
			//((Activity)context).runOnUiThread(new MyTimerTask(this));
//		} else {
//			flip = true;
//		}
//			Timer t = new Timer();
//			t.schedule(new MyTimerTask(this), 1000);
			//wordBtn.setText(nextWord().getWord());
			
	}

	
	public void flipBack(){
		super.showNext();
	}
	class MyTimerTask extends TimerTask{

		SoudWordFlipper flipper;
		
		public MyTimerTask(SoudWordFlipper flipper) {
			// TODO Auto-generated constructor stub
			this.flipper = flipper;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(3000);
				flipper.flipBack();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
