package com.mobilearning.minpairs.util;

public class MP_Item {
    private int _Id;
//    public int PairId { set { _Id = value; } get { return _Id; } }     // word id
    private String _Caption; 
//    public String Caption { set { _Caption = value; } get { return _Caption; } } // word's caption
    private String _Audio; 
//    public String Audio { set { _Audio = value; } get { return _Audio; } } //word's sound file name
    private String _Image; 
//    public String Image { set { _Image = value; } get { return _Image; } } // word's image file name

    public MP_Item(String Id, String Caption, String Audio, String Image)
    {
        try{
        	_Id = Integer.parseInt(Id.trim());
        }catch(NumberFormatException e){
        	_Id = Integer.parseInt(Id.substring(1));
        }
        _Caption = Caption;
        _Audio = Audio;
        _Image = Image;
    }

	public int get_Id() {
		return _Id;
	}

	public void set_Id(int _Id) {
		this._Id = _Id;
	}

	public String get_Caption() {
		return _Caption;
	}

	public void set_Caption(String _Caption) {
		this._Caption = _Caption;
	}

	public String get_Audio() {
		return _Audio;
	}

	public void set_Audio(String _Audio) {
		this._Audio = _Audio;
	}

	public String get_Image() {
		return _Image;
	}

	public void set_Image(String _Image) {
		this._Image = _Image;
	}

}
