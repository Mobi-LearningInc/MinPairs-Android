package com.mobilearning.minpairs.util;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class OneFilter
{

    private String _FilterTitle;            // holds the caption representing the current state of the filter
    private boolean _FilterSet = false;
    private int _iCatPairId;
    public int CatPairId;
    public int getCatPairId(){ return _iCatPairId; }
    public Dictionary<Integer, Integer> MyPairs = new Hashtable<Integer, Integer>();                       // the filtered pairs collection
    public Dictionary<Integer, Integer> MySecCategories = new Hashtable<Integer, Integer>();               // the filtered secondary sounds by main filter
    public Dictionary<Integer, Integer> MyMainCategories = new Hashtable<Integer, Integer>();              // the filtered main sounds
    public Dictionary<Integer, Integer> MySelectedSecCategories = new Hashtable<Integer, Integer>();       // the filtered secondary sounds by user
//    private ML_Main ML_Main;                                    // the main class instance
//    public ML_Main MyMain { set { ML_Main = value; } }

    private Dictionary<Integer, Integer> clear(Dictionary<Integer, Integer> dict){
    	Enumeration keys = dict.keys();
    	while(keys.hasMoreElements()){
    		dict.remove(keys.nextElement());
    	}
    	return dict;
    }
    
    public void AddNewMainSound(int iMainSound)
    {
        MyMainCategories = clear(MyMainCategories);
        MyMainCategories.put(MyMainCategories.size() + 1, iMainSound);
        BuildSecList();
        

    }
    
    public void ClearFilter(){
    	AddNewMainSound(0);
    	MakeTitle();
    	BuildSecList();
    	BuildPairsList();
    }
    
    public void RemoveMainSound(int iMainSound)
    {
        if (MyMainCategories.get(new Integer(iMainSound))!=null)
        {
            int[] tempSounds = new int[MyMainCategories.size()];
            int kCnt = 1;
            Enumeration<Integer> elems =  MyMainCategories.elements();
            int i=0;
            while(elems.hasMoreElements()){
            	tempSounds[i] = elems.nextElement();
            }
            //CopyTo(tempSounds, 0);
            MyMainCategories = clear(MyMainCategories);
            for (int iCnt = 0; iCnt < tempSounds.length; iCnt++)
            {
                if (tempSounds[iCnt] != iMainSound)
                {
                    MyMainCategories.put(kCnt, tempSounds[iCnt]);
                    kCnt++;

                }
            }
            BuildSecList();
        }
    }
    public void AddSecondarySound(int iSecSound)
    {
            MySelectedSecCategories=clear(MySelectedSecCategories);
            MySelectedSecCategories.put(MySelectedSecCategories.size() + 1, iSecSound);
            BuildPairsList();
            if(iSecSound>0)
            	_FilterSet = true;
            else
            	_FilterSet = false;
    }
    public void RemoveSecondarySound(int iSecSound)
    {
        if (MySelectedSecCategories.get(new Integer(iSecSound))!=null)
        {
            int[] tempSounds = new int[MySelectedSecCategories.size()];
            int kCnt = 1;
            //MySelectedSecCategories.Values.CopyTo(tempSounds, 0);
            Enumeration<Integer> elems = MySelectedSecCategories.elements();
            int i = 0;
            while(elems.hasMoreElements()){
            	tempSounds[i] = elems.nextElement();
            }
            
            MySelectedSecCategories=clear(MySelectedSecCategories);
            
            for (int iCnt = 0; iCnt < tempSounds.length; iCnt++)
            {
                if (tempSounds[iCnt] != iSecSound)
                {
                    MySelectedSecCategories.put(kCnt, tempSounds[iCnt]);
                }
            }

        }
    }
    
    private void BuildSecList()
    {
        MySecCategories=clear(MySecCategories);

        for (int iCnt = 1; iCnt <= MyMainCategories.size(); iCnt++)
        {
            for (int jCnt = 1; jCnt <= ML_Main.CatPairs.size(); jCnt++)
            {
                if (MyMainCategories.get(iCnt).intValue() == ML_Main.CatPairs.get(jCnt).iCP1 || 
                		MyMainCategories.get(iCnt).intValue()==0)
                {
                    MySecCategories.put(MySecCategories.size()+1,ML_Main.CatPairs.get(jCnt).iCP2);
                }
            }
        }
        
    }
    
    public boolean isFilterSet()
    {
//       if (MyPairs.size() > 0)
//            { _FilterSet = true; }
//            else
//            { _FilterSet = false; }
            return _FilterSet;
        
    }
    
    private void BuildPairsList()
    {
        MyPairs=clear(MyPairs);
        if(MyMainCategories.size()==1 && MyMainCategories.get(1).intValue()==0){
        	for (int iCnt = 1; iCnt <= ML_Main.ItemPairs.size(); iCnt++){
        		MyPairs.put(MyPairs.size() + 1, iCnt);
        	}
        }
        else{
	        for (int iCnt = 1; iCnt <= ML_Main.ItemPairs.size(); iCnt++)
	        {
	            if ((ML_Main.ItemPairs.get(iCnt).iCatId1 == MyMainCategories.get(1).intValue() && 
	            		ML_Main.ItemPairs.get(iCnt).iCatId2 == MySelectedSecCategories.get(1).intValue())||
	            	(MyMainCategories.get(1).intValue()==0)	
	            	)
	            {
	                MyPairs.put(MyPairs.size() + 1, iCnt);
	            }
	        }
        }
        MakeTitle();
    }
    

    private void MakeTitle()
    {
    	if(MyMainCategories.get(1).intValue()==0)
    		_FilterTitle = "All"; 
    	else
    		_FilterTitle = ML_Main.CatList.get(MyMainCategories.get(1)).getCaption() + " vs " + ML_Main.CatList.get(MySelectedSecCategories.get(1)).getCaption();
        for (int iCnt = 1; iCnt <= ML_Main.CatPairs.size(); iCnt++)
        {
            if (ML_Main.CatPairs.get(iCnt).iCP1 == MyMainCategories.get(1) && ML_Main.CatPairs.get(iCnt).iCP2 == MySelectedSecCategories.get(1))
            {
                _iCatPairId = iCnt;
                break;
            }
        }

    }
    public String getFilterTitle()
    {
         return _FilterTitle; 
    }

    
}
