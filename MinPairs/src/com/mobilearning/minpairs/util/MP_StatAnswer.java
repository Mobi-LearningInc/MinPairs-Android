package com.mobilearning.minpairs.util;

public class MP_StatAnswer
{
    private MPQuestionType _myType;
    public void setMyType(MPQuestionType value) { _myType = value; } 

    private long _AnswerDuration;
    public void setAnswerDuration(long value) {_AnswerDuration = value; } 

    private boolean _isCorrect;
    public boolean getIsCorrect() { return _isCorrect; } 
    public void setIsCorrect(boolean value){ _isCorrect = value; }
    
    private int _iPairId;
    public void setPairId(int value) { _iPairId = value; } 

    public String getAnswerDescr(int iSessionId)
    {
        String sRes = "";
        sRes = iSessionId + "|" + _iPairId + "|" + _myType + "|" + _isCorrect + "|" + _AnswerDuration +"\n";

        return sRes;
    }
}
