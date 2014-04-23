package com.mobilearning.minpairs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class MP_Button extends Button {
	
	private Drawable drawable;	
	private String audio;
	
	public MP_Button(Context context) {
		super(context);
	}




	public String getAudio() {
		return audio;
	}




	public void setAudio(String audio) {
		this.audio = audio;
	}
}
