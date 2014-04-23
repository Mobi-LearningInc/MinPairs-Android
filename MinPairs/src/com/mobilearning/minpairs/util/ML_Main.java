package com.mobilearning.minpairs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import com.mobilearning.minpairs.MPActivity;
import com.mobilearning.minpairs.R;
import com.mobilearning.minpairs.SettingsActivity;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.text.format.DateFormat;

public class ML_Main {

	public static Dictionary<Integer, MP_Item> ItemList = new Hashtable<Integer, MP_Item>(); // list
																								// of
																								// the
																								// items
	public static Dictionary<Integer, iPairs> ItemPairs = new Hashtable<Integer, iPairs>(); // list
																							// of
																							// pairs
	public static Dictionary<Integer, MP_Category> CatList = new Hashtable<Integer, MP_Category>(); // list
																									// of
																									// categories
	public static Dictionary<Integer, iCatPairs> CatPairs = new Hashtable<Integer, iCatPairs>(); // pairs
																									// of
																									// sounds
	public static Date StatStartDate = new Date();// DateTime.Parse("Jan 01, 2000");
	public static Date StatEndDate = StatStartDate;
	public static boolean IsDateNotChanged = true;

	public static final String SOUNDS = ""; 
	
	private static AssetManager amgr;
	private static String assets = "android_assets/";
	private static String catFile = "data/MP_Categories.dat";
	private static String catPairsFile = "data/MP_CatPairs.dat";
	private static String itemsFile = "data/MP_Items.dat";
	private static String pairsFile = "data/MP_Pairs.dat";
	private static String itemsCatsFile = "data/MP_Items_Categories.dat";

	public static ArrayList<Integer> deck = new ArrayList<Integer>();
	
	
	private static String tempDirName = "tmp";
	private static String dataDirName = "data";
	
	public static DeviceSize DAS;
	
	public static void setAssetManager(AssetManager am) {
		amgr = am;
	}

	public static void LoadCategories() // stores the sounds in sounds
										// dictionary
	{
		String sRec = "";
		String[] sa;
		MP_Category ci;

		try {
			InputStream in = amgr.open(catFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, "|");
				sa = new String[3];
				sa[0] = tokenizer.nextToken();
				sa[1] = tokenizer.nextToken();
				sa[2] = tokenizer.nextToken();
				ci = new MP_Category(sa[0], sa[1], sa[2], "");

				CatList.put(ci.getId(), ci);

			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LoadItems() // stores the words into the words dictionary
	{
		String sRec = "";
		String[] sa;
		MP_Item mi;
		try {
			InputStream in = amgr.open(itemsFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, "|");
				sa = new String[4];
				sa[0] = tokenizer.nextToken();
				sa[1] = tokenizer.nextToken();
				sa[2] = tokenizer.nextToken();
				sa[3] = tokenizer.nextToken();
				mi = new MP_Item(sa[0], sa[1], sa[2], sa[3]);
				try {
					ItemList.put(Integer.parseInt(sa[0]), mi);
				} catch (NumberFormatException e) {
					ItemList.put(Integer.parseInt(sa[0].substring(1)), mi);
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LoadCatPairs() // stores the paired sounds as symetric
										// double pairs into the sound pairs
										// dictionary
	{
		/*
		 * will create the pairing items between the categories (sounds)
		 */
		String sRec = "";
		String[] sa;
		iCatPairs icp;
		int iCnt = 1;
		try {
			InputStream in = amgr.open(catPairsFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, "|");
				sa = new String[2];
				sa[0] = tokenizer.nextToken();
				try {
					Integer.parseInt(sa[0]);
				} catch (NumberFormatException e) {
					sa[0] = sa[0].substring(1);
				}
				sa[1] = tokenizer.nextToken();
				try {
					Integer.parseInt(sa[1]);
				} catch (NumberFormatException e) {
					sa[1] = sa[1].substring(1);
				}
				icp = new iCatPairs();
				icp.iCP1 = Integer.parseInt(sa[0]); // Convert.ToInt32(sa[0]);
				icp.iCP2 = Integer.parseInt(sa[1]);// Convert.ToInt32(sa[1]);
				CatPairs.put(iCnt, icp);
				icp = new iCatPairs();
				icp.iCP2 = Integer.parseInt(sa[0]); // Convert.ToInt32(sa[0]);
				icp.iCP1 = Integer.parseInt(sa[1]);// Convert.ToInt32(sa[1]);
				CatPairs.put(iCnt, icp);
				iCnt++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LoadPairs() // stores the paired words as symetric double
									// pairs into the word pairs dictionary
	{
		String sRec = "";
		String[] sa;
		iPairs ip;
		try {
			InputStream in = amgr.open(pairsFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			int iCnt = 1;
			int kCnt = 1;
			while ((line = br.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, "|");
				sa = new String[4];
				sa[0] = tokenizer.nextToken();
				try {
					Integer.parseInt(sa[0]);
				} catch (NumberFormatException e) {
					sa[0] = sa[0].substring(1);
				}
				sa[1] = tokenizer.nextToken();
				try {
					Integer.parseInt(sa[1]);
				} catch (NumberFormatException e) {
					sa[1] = sa[1].substring(1);
				}
				sa[2] = tokenizer.nextToken();
				try {
					Integer.parseInt(sa[2]);
				} catch (NumberFormatException e) {
					sa[2] = sa[2].substring(1);
				}
				sa[3] = tokenizer.nextToken();
				try {
					Integer.parseInt(sa[3]);
				} catch (NumberFormatException e) {
					sa[3] = sa[3].substring(1);
				}
				ip = new iPairs();
				ip.iP1 = Integer.parseInt(sa[0]);
				ip.iCatId1 = Integer.parseInt(sa[1]);
				ip.iP2 = Integer.parseInt(sa[2]);
				ip.iCatId2 = Integer.parseInt(sa[3]);
				ip.iPairId = kCnt;
				ItemPairs.put(iCnt, ip);

				iCnt++;
				ip = new iPairs();
				ip.iP1 = Integer.parseInt(sa[2]);
				ip.iCatId1 = Integer.parseInt(sa[3]);
				ip.iP2 = Integer.parseInt(sa[0]);
				ip.iCatId2 = Integer.parseInt(sa[1]);
				ip.iPairId = -kCnt;
				ItemPairs.put(iCnt, ip);
				iCnt++;
				kCnt++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void LoadCatItemsMap() // complets the sounds' collection of
											// words with their assigned words
	{
		String sRec = "";
		String[] sa;
		int iCatId;
		try {
			InputStream in = amgr.open(itemsCatsFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			int iCnt = 1;
			int kCnt = 1;
			while ((line = br.readLine()) != null) {

				StringTokenizer tokenizer = new StringTokenizer(line, "|");
				sa = new String[2];
				sa[0] = tokenizer.nextToken();
				sa[1] = tokenizer.nextToken();
				if (sa != null && sa[0] != null && sa[1] != null) {
					try {
						iCatId = Integer.parseInt(sa[0]);
					} catch (NumberFormatException e) {
						iCatId = Integer.parseInt(sa[0].substring(1));
					}
					MP_Category c =  CatList.get(iCatId);
					try {
						
						c.AddItem(Integer.parseInt(sa[1]));
						
					} catch (NumberFormatException e) {
						c.AddItem(
								Integer.parseInt(sa[1].substring(1)));
					}
					CatList.put(c.getId(), c);

				} else {
					System.out.println("Problem loading file");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void ResetCategories(int iCatId) {
		for (int iCnt = 1; iCnt <= CatList.size(); iCnt++) {
			if (iCnt != iCatId) {
				CatList.get(iCnt).ResetCategory();
			}
		}
	}

	//sRes = iSessionId + "|" + _iPairId + "|" + _myType + "|" + _isCorrect + "|" + _AnswerDuration;
    private static class _MySession
    {
        public int SessionId;
        public int iCatPairId;        
        public Date SessionDate;
        public boolean IsQuizz;
        @Override
        public String toString() {
        	// TODO Auto-generated method stub
        	return String.valueOf(SessionId);//SessionDate.toString();
        }
    }
    private static class _MyAnswer
    {
        public int iSessionId;
        public boolean IsCorrect;
        public int PairId;
        public String MyType;
        public long AnswerDuration;

    }
    private static ArrayList<_MySession> Sessions;
    private static ArrayList<_MyAnswer> Answers;
	
	public static void LoadData(Activity context)
    {
		ArrayList<Integer> Pairs = new ArrayList<Integer>();
		Sessions = new ArrayList<_MySession>();
		Answers = new ArrayList<_MyAnswer>();

        
        ///Select session
//        _MySession currSession;
//        if (SoundId != 0)
//        {
//            for (int iCnt = 1; iCnt <=ML_Main.CatPairs.size(); iCnt++)
//            {
//                if (ML_Main.CatPairs.get(iCnt).iCP1 == SoundId || ML_Main.CatPairs.get(iCnt).iCP2 == SoundId)
//                {
//                    Pairs.add(iCnt);
//                }
//            }
//        }
//        
//        boolean bSelect=true;
        Sessions = readSessionFile(context);
        Answers = readAnswerFile(context);
		
    }
	
	public static ArrayList<Float> getSoundSeries(int soundId){
		ArrayList<Float> res = new ArrayList<Float>();

		
		return res;
	}

	public static ArrayList<String> getSessionsLabels(){
		ArrayList<String> res = new ArrayList<String>();
		for (Iterator<_MySession> iterator = Sessions.iterator(); iterator.hasNext();) {
			_MySession type = (_MySession) iterator.next();
			res.add(type.toString());
		}
		return res;
	}
	
	public static ArrayList<Float> getQTypeSeries(MPQuestionType qType){
		String sQType;
		ArrayList<Float> res = new ArrayList<Float>();
		
		switch (qType) {
			case ListenSelect:
				sQType = "ListenSelect";
				break;
			case ListenType:
				sQType = "ListenType";
				break;
			case ReadListenSelect:
				sQType = "ReadListenSelect";
				break;
				
			default:
				sQType = "All";
//				return res;
				break;
		}
		
		if(Sessions==null || Answers==null)
			return res;
		
		float[] perc = new float[Sessions.size()];
		
		for (int i = 0; i < perc.length; i++) {
			perc[i] = 0;
		}
		int i = 0;		
		for (Iterator<_MySession> iterator2 = Sessions.iterator(); iterator2.hasNext();) {
			_MySession ses = (_MySession) iterator2.next();
			int cor = 0;
			int tot = 0;
			
			for (Iterator<_MyAnswer> iterator = Answers.iterator(); iterator.hasNext();) {
				_MyAnswer ans = (_MyAnswer) iterator.next();
				if((ans.MyType.equals(sQType)||sQType.equals("All")) && ans.iSessionId==ses.SessionId){
							
					if(ans.IsCorrect)
						cor++;
					tot++;
				
				}
			}
			perc[i] = ((float)cor/(float)tot)*100;
			res.add(perc[i]);
			i++;
			
		}
		
		return res;
	}

	
	private static boolean validateLine(String[] sa){
		boolean res = true;
		
		for(int i=0; i<sa.length; i++)
			res = res && sa[i]!=null;
		return res;
	}
	
	
	
	private static ArrayList<_MySession> readSessionFile(Activity context){
		ArrayList<_MySession> res = new ArrayList<ML_Main._MySession>();
		try{
			FileInputStream ifs;
			ifs = context.openFileInput(context
					.getString(R.string.sessionFileName));

			int _SessionId = 1;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					ifs));			
			
			String line;
			
			String[] sa;
			while ((line = br.readLine()) != null) {

				StringTokenizer tokenizer = new StringTokenizer(line, "|");
				sa = new String[4];
				sa[0] = tokenizer.nextToken();
				sa[1] = tokenizer.nextToken();
				sa[2] = tokenizer.nextToken();
				sa[3] = tokenizer.nextToken();
//				sa[4] = tokenizer.nextToken();
				
				if (validateLine(sa)) {
					try {
//sRes = iSessionId + "|" + _iCatPairId + "|" + isQuiz + "|" + sDate + "|" + eDate;
						_MySession ans = new _MySession();
						ans.SessionId = Integer.parseInt(sa[0]);
						ans.iCatPairId = Integer.parseInt(sa[1]);
						ans.IsQuizz = Boolean.parseBoolean(sa[2]);
						ans.SessionDate = new SimpleDateFormat(context.getResources().
								getString(R.string.dateFormat)).parse(sa[3]);//new DateForma(sa[3]);
						res.add(ans);
						
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				} else {
					System.out.println("Problem loading file: "+context.getString(R.string.answersFileName));
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	private static ArrayList<_MyAnswer> readAnswerFile(Activity context){
		ArrayList<_MyAnswer> res = new ArrayList<ML_Main._MyAnswer>();
		try {
			
			FileInputStream afs;
			afs = context.openFileInput(context
					.getString(R.string.answersFileName));

			BufferedReader br = new BufferedReader(new InputStreamReader(
					afs));			
			
			String line;
			
			String[] sa;
			while ((line = br.readLine()) != null) {

				StringTokenizer tokenizer = new StringTokenizer(line, "|");
				sa = new String[5];
				sa[0] = tokenizer.nextToken();
				sa[1] = tokenizer.nextToken();
				sa[2] = tokenizer.nextToken();
				sa[3] = tokenizer.nextToken();
				sa[4] = tokenizer.nextToken();
				
				if (validateLine(sa)) {
					try {
						//sRes = iSessionId + "|" + _iPairId + "|" + _myType + "|" + _isCorrect + "|" + _AnswerDuration;
						_MyAnswer ans = new _MyAnswer();
						ans.iSessionId = Integer.parseInt(sa[0]);
						ans.PairId = Integer.parseInt(sa[1]);
						ans.MyType = sa[2];
						ans.IsCorrect = Boolean.parseBoolean(sa[3]);
						ans.AnswerDuration = Long.parseLong(sa[4]);
						res.add(ans);
						
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}					
				} else {
					System.out.println("Problem loading file: "+context.getString(R.string.answersFileName));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private void ComputeData(int SoundId)
    {
		
    }
	
	
	
	public static class DeviceSize{
		public int dX;
		public int dY;
	}



	public static void resetStats(Activity _context) {
			_context.deleteFile(_context.getString(R.string.sessionFileName));
			_context.deleteFile(_context.getString(R.string.answersFileName));
	}

	public static void randomizeDeck(int deckSize) {
		// TODO Auto-generated method stub
		for(int index=deck.size(); index<0; index--)
			deck.remove(index);
		
		Random gen = new Random(System.currentTimeMillis());
		
		for(int i=0; i<deckSize/2; i++){
			int pair = gen.nextInt(ML_Main.ItemList.size());
//			ML_Main.ItemPairs.get(new Integer(gen.nextInt()));
		}
	}



//	public static void LoadData(MPActivity mpActivity) {
//		// TODO Auto-generated method stub
//		
//	}
	
}
