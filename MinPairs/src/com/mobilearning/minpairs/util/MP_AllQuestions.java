package com.mobilearning.minpairs.util;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import android.app.Activity;

import com.mobilearning.minpairs.MPActivity;

public class MP_AllQuestions
{
    public MP_AllQuestions(boolean bIsQizz, int CatPairId, Activity context)
    {
        this.BuildQuestions(bIsQizz);
        _bIsQuizz = bIsQizz;
        mySession = new MP_StatSession(bIsQizz, context);
        mySession.setCatPairId(CatPairId);
        mySession.setStartDate(System.currentTimeMillis());
    }
    
    @Override
    protected void finalize() throws Throwable {
    	// TODO Auto-generated method stub
    	super.finalize();
        if (mySession != null)
        {
            mySession.setEndDate(System.currentTimeMillis());
            mySession.SaveSession();
            mySession = null;
        }
    }

    public void EndSession()
     {
         if (mySession != null)
         {
             mySession.setEndDate(System.currentTimeMillis());
             mySession.SaveSession();
             mySession = null;
         }
     }
    private boolean _bIsQuizz = false;
    private int iQCounter=0;
    private Dictionary<Integer, MP_Question> MyQuestions = new Hashtable<Integer, MP_Question>();
    public Dictionary<Integer, Integer> RandomQuestions = new Hashtable<Integer, Integer>();
    private MP_StatSession mySession;
    
    public  Dictionary<Integer, Integer> clear_Int(Dictionary<Integer, Integer> dict){
    	Enumeration keys = dict.keys();
    	while(keys.hasMoreElements()){
    		dict.remove(keys.nextElement());
    	}
    	return dict;
    }
    public  Dictionary<Integer, MP_Question> clear(Dictionary<Integer, MP_Question> dict){
    	Enumeration keys = dict.keys();
    	while(keys.hasMoreElements()){
    		dict.remove(keys.nextElement());
    	}
    	return dict;
    }

    
    private void BuildQuestions(boolean bIsQuizz)
    {
        OneFilter myF ;
//        ML_Filter F;
//        F = (ML_Filter)PhoneApplicationService.Current.State["MF"];
        myF = MPActivity.mf;//F.GetFilter("MP");
        MyQuestions = clear(MyQuestions);
        MP_Question currQ;
        int myPairId;
        for (int iCnt = 1; iCnt <= myF.MyPairs.size(); iCnt++)
        {
            myPairId = myF.MyPairs.get(iCnt);

            currQ = new MP_Question();
            currQ.setPairId(myPairId);
            currQ.setCorrectAnswer (1);
            currQ.setQuestionType(MPQuestionType.ListenSelect);
            currQ.setIsTimed( bIsQuizz);
            currQ.setTimeOutVal ( 5);
            MyQuestions.put((iCnt-1)*6 + 1, currQ);

            currQ = new MP_Question();
            currQ.setPairId(myPairId);
            currQ.setCorrectAnswer (1);
            currQ.setQuestionType( MPQuestionType.ListenType);
            currQ.setIsTimed( bIsQuizz);
            currQ.setTimeOutVal (10);
            MyQuestions.put((iCnt-1)*6 + 2, currQ);

            currQ = new MP_Question();
            currQ.setPairId(myPairId);
            currQ.setCorrectAnswer (1);
            currQ.setQuestionType(MPQuestionType.ReadListenSelect);
            currQ.setIsTimed( bIsQuizz);
            currQ.setTimeOutVal (10);
            MyQuestions.put((iCnt - 1) * 6 + 3, currQ);

            currQ = new MP_Question();
            currQ.setPairId(myPairId);
            currQ.setCorrectAnswer (2);
            currQ.setQuestionType(MPQuestionType.ListenSelect);
            currQ.setIsTimed( bIsQuizz);
            currQ.setTimeOutVal ( 5);
            MyQuestions.put((iCnt - 1) * 6 + 4, currQ);

            currQ = new MP_Question();
            currQ.setPairId(myPairId);
            currQ.setCorrectAnswer (2);
            currQ.setQuestionType(MPQuestionType.ListenType);
            currQ.setIsTimed( bIsQuizz);
            currQ.setTimeOutVal (10);
            MyQuestions.put((iCnt - 1) * 6 + 5, currQ);

            currQ = new MP_Question();
            currQ.setPairId(myPairId);
            currQ.setCorrectAnswer (2);
            currQ.setQuestionType(MPQuestionType.ReadListenSelect);
            currQ.setIsTimed( bIsQuizz);
            currQ.setTimeOutVal (10);
            MyQuestions.put((iCnt - 1) * 6 + 6, currQ);
        }
        BuildRandomList();
    }
    private void BuildRandomList()
    {
        RandomQuestions = new Hashtable<Integer, Integer>();
        for (int iCnt = 1; iCnt <= MyQuestions.size(); iCnt++)
        {
            RandomQuestions.put(iCnt, iCnt);
            
        }
    }
    public MP_Question GetOneQuestion()
    {
        Random myRnd = new Random(System.currentTimeMillis());
        myRnd = new Random(myRnd.nextInt(Integer.MAX_VALUE));
        int iQPos = myRnd.nextInt(RandomQuestions.size() + 1)+1;
        MP_Question qRes = (MP_Question)MyQuestions.get(RandomQuestions.get(iQPos));
        if (!_bIsQuizz || iQCounter < _SetSize)
        {

            // remove the selected question from the list 

            Dictionary<Integer, Integer> rq = new Hashtable<Integer, Integer>();
            int jCnt = 1;
            for (int iCnt = 1; iCnt <= RandomQuestions.size(); iCnt++)
            {
                if (iCnt != iQPos)
                {
                    rq.put(jCnt, RandomQuestions.get(iCnt));
                    jCnt++;
                }
            }
            RandomQuestions = clear_Int(RandomQuestions);
            for (int iCnt = 1; iCnt <= rq.size(); iCnt++)
            {
                RandomQuestions.put(iCnt, rq.get(iCnt));
            }
            if (RandomQuestions.size() == 0)
            {
                BuildQuestions(_bIsQuizz);
            }
            iQCounter++;
            qRes.setQuestionNumber(iQCounter);
        }
        else
        {
            _sFullMessage = "You answered correct at " + mySession.GetPercentage() + " out of " + _SetSize + " questions";
            EndSession();
            iQCounter = 0;
            qRes = null;
            BuildQuestions(_bIsQuizz);
            
        }
        return qRes;

    }
    public void SaveAnswer(MP_Question myQ)
    {
        mySession.SetAnswer(myQ);
    }

    private int _SetSize;
    public void setSize(int value) { _SetSize = value; } 
    private String _sFullMessage;
    public String getFullMessage() { return _sFullMessage; } 

}
