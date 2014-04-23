package com.mobilearning.minpairs.util;

import java.util.Date;

import android.os.Handler;

public class MP_Question
{
    private boolean _IsTimed = false;
    public boolean getIsTimed() { return _IsTimed; }
    public void setIsTimed(boolean value) { _IsTimed = value; }  

    private int _TimeOutVal = 0;
    public int getTimeOutVal() {  return _TimeOutVal; }
    public void setTimeOutVal(int value) { _TimeOutVal = value; }

    private int _iQuestionCnt = 0;
    public void setQuestionNumber(int value) { _iQuestionCnt = value; }
    
    Handler dt;

    private MPQuestionType _QType;
    public MPQuestionType getQuestionType() { return _QType; } 
    public void setQuestionType(MPQuestionType type) { _QType = type; } 

    private int _correctAnswer = -1;
    public int getCorrectAnswer() { return _correctAnswer; } 
    public void setCorrectAnswer(int value) { _correctAnswer = value; }

    private int _iPairId = -1;
    public int getPairId() {  return _iPairId; } 
    public void setPairId(int value) { _iPairId = value; } 

    private long _dtStart;
    public long getStart() { return _dtStart; } 
    
    private long _iAnswerDuration;
    public long getAnswerDuration() { return _iAnswerDuration; } 

    private boolean _bIsCorrect = false;
    public boolean getIsCorrect() {  return _bIsCorrect; } 

    private long _dtEnd;
    public long getEnd() { return _dtEnd; } 
    
//    private Handler TimeOut = new Handler();
    
    public void StartTiming()
    {
        if (_IsTimed)
        {
//        	customHandler.postDelayed(updateTimerThread, 0);

            dt = new Handler();
//            dt.Interval = new TimeSpan(0, 0, _TimeOutVal);
//            dt.Tick += new EventHandler(dt_Tick);
//            dt.Start();
            dt.postDelayed(dt_Tick, _TimeOutVal);
        }
        _dtStart = System.currentTimeMillis();
    }
    
    private Runnable dt_Tick = new Runnable() {
    	public void run() {
	        _dtEnd = System.currentTimeMillis();
	        //dt.removeCallbacks(dt_Tick);
	        _iAnswerDuration = (_dtEnd-_dtStart)/1000;
//	        MPTimeOutEventArgs TO_Args = new MPTimeOutEventArgs(_TimeOutVal);
//	        TimeOut(this, TO_Args);
    	}
    };
    
    
    public void CheckAnswer(boolean bAnswer)
    {
        _dtEnd = System.currentTimeMillis();
        _iAnswerDuration = (int) (_dtEnd-_dtStart/1000);
        _bIsCorrect = bAnswer;

    }
//    public event MPTimeOutEventHandler TimeOut;
    
    public void EndTiming()
    {
        
        dt.removeCallbacks(dt_Tick);
    }

    public String QuestionDescription()
    {
        return "Question No: " + _iQuestionCnt;
    }

}

