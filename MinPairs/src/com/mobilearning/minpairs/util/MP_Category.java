package com.mobilearning.minpairs.util;

import java.util.Dictionary;
import java.util.Hashtable;


public class MP_Category {

    private int _Id; 
//    public int CategoryId { set { _Id = value; } get { return _Id; } }     // category id
    private String _Caption; 
//    public string Caption { set { _Caption = value; } get { return _Caption; } } //category caption
    private String _Audio; 
//    public string Audio { set { _Audio = value; } get { return _Audio; } } // category's audio file name
    private String _Image;
//    public string Image { set { _Image = value; } get { return _Image; } } // category's image file name
    private static int _ItemId;  // current word to be played
    private boolean _IsWord;    // flag to tell the consumer to play the audio for the sound or the next word from the assigned word.   
    private Dictionary<Integer, Integer> myItems;        // the list of words assigned to this sound

	
	public MP_Category(String Id, String Caption, String Audio, String Image)
    {
        
		try{
			setId(Integer.parseInt(Id.trim()));
        }catch(NumberFormatException e){
        	setId(Integer.parseInt(Id.substring(1,Id.length())));
        }
		
        setCaption(Caption);
        setAudio(Audio);
        setImage(Image);
        _IsWord = false;
        _ItemId = 0;
        myItems = new Hashtable<Integer, Integer>();
        SetNextWordId();
    }

	public void AddItem(int ItemId)
    {
        myItems.put(myItems.size() + 1, ItemId);
    }           // builds the list of the assigned words to the sound
    
	public int[] GetCategoryItems()
    {
        int[] ItemsId = new int[myItems.size()];
        for (int iCnt = 1; iCnt <= myItems.size(); iCnt++)
        {
            ItemsId[iCnt - 1] = myItems.get(iCnt);
        }
        return ItemsId;
    }           // returns an array of words' id
    private void SetNextWordId()                // calculates which will be the next word to be played
    {
        if (_ItemId == myItems.size())
        {
            _ItemId = 1;
        }
        else
        {
            _ItemId++;
        }
    }
    public int GetNextWordId()
    {
    	if(!myItems.isEmpty())
    		return myItems.get(_ItemId);
    	else
    		return 0;
    }
    public int GetAudioId()                     // will return the audio id to be played if the return value is -1 the caller will play the audio file for the sound
    {
        // if return -1 will use the category audio else will use the id to pic the word's audion to play
        int AudioId = -1;
        if (_IsWord)
        {
            AudioId = myItems.get(_ItemId);
            
            SetNextWordId();
        }
        _IsWord = !_IsWord;
        return AudioId;
    }
    public void ResetCategory()
    {
        _IsWord = false;
    }

	public int getId() {
		return _Id;
	}

	public void setId(int _Id) {
		this._Id = _Id;
	}

	public String getCaption() {
		return _Caption;
	}

	public void setCaption(String _Caption) {
		this._Caption = _Caption;
	}

	public String getAudio() {
		return _Audio;
	}

	public void setAudio(String _Audio) {
		this._Audio = _Audio;
	}

	public String getImage() {
		return _Image;
	}

	public void setImage(String _Image) {
		this._Image = _Image;
	}
}
