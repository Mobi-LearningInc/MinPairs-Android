package com.mobilearning.minpairs.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;

import com.mobilearning.minpairs.R;

public class MP_StatSession {
	public MP_StatSession(boolean bIsQuizz, Activity context) {
		_IsQuizz = bIsQuizz;
		_context = context;
		GetSessionId();
		myAnswers = new Hashtable<Integer, MP_StatAnswer>();
		
	}

	private Activity _context;
	private char sSep = '|';
	private String sDateFormat = "MM-dd-yyyy hh:mm:ss";

	private int _SessionId;
	private int _CatPairId;

	public void setCatPairId(int value) {
		_CatPairId = value;
	}

	private boolean _IsQuizz;
	private Dictionary<Integer, MP_StatAnswer> myAnswers;

	public int getSessionId() {
		return _SessionId;
	}

	public void setSessionId(int value) {
		_SessionId = value;
	}

	private long _StartDate;// DateTime _StartDate;

	public long getStartDate() {
		return _StartDate;
	}

	public void setStartDate(long value) {
		_StartDate = value;
	}

	private long _EndDate;

	public long getEndDate() {
		return _EndDate;
	}

	public void setEndDate(long value) {
		_EndDate = value;
	}

	public void SaveSession() {
		// IsolatedStorageFile iFile =
		// IsolatedStorageFile.GetUserStoreForApplication();
		FileOutputStream ifs;
		FileOutputStream afs;
		// IsolatedStorageFileStream ifs;
		// IsolatedStorageFileStream afs;
		// if (iFile.FileExists(sSessionFileName))
		// {
		try {
			ifs = _context.openFileOutput(
					_context.getString(R.string.sessionFileName),
					Context.MODE_APPEND);

			afs = _context.openFileOutput(
					_context.getString(R.string.answersFileName),
					Context.MODE_APPEND);

			StoreSessionInfo(ifs, afs);

			ifs.close();
			afs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void StoreSessionInfo(FileOutputStream ifs, FileOutputStream afs) {
		// CultureInfo ci = CultureInfo.InvariantCulture;

		StringBuilder sSessionData = new StringBuilder();
		sSessionData.append(_SessionId);
		sSessionData.append(sSep);
		sSessionData.append(_CatPairId);
		sSessionData.append(sSep);
		sSessionData.append(String.valueOf(_IsQuizz));
		sSessionData.append(sSep);
		sSessionData.append(DateFormat
				.format(sDateFormat, new Date(_StartDate)));// _StartDate.ToString(sDateFormat,
															// ci) +
															// sSep.ToString());
		sSessionData.append(sSep);
		sSessionData.append(DateFormat.format(sDateFormat, new Date(_EndDate)));
		sSessionData.append("\n");
		// StreamWriter sw = new StreamWriter(ifs);
		try {
			ifs.write(sSessionData.toString().getBytes());

			MP_StatAnswer myAnswer;
			for (int iCnt = 1; iCnt <= myAnswers.size(); iCnt++) {
				myAnswer = (MP_StatAnswer) myAnswers.get(iCnt);
				afs.write(myAnswer.getAnswerDescr(_SessionId).getBytes());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void GetSessionId() {
		FileInputStream ifs;
		// FileOutputStream afs;
		try {
			ifs = _context.openFileInput(_context
					.getString(R.string.sessionFileName));

			_SessionId = 1;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ifs));
			String sLine = null;
			while ((sLine = reader.readLine()) != null) {

				_SessionId++;
			}
			ifs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SetAnswer(MP_Question oQ) {
		MP_StatAnswer oAnswer = new MP_StatAnswer();
		oAnswer.setIsCorrect(oQ.getIsCorrect());
		oAnswer.setAnswerDuration(oQ.getAnswerDuration());
		oAnswer.setPairId(oQ.getPairId());
		oAnswer.setMyType(oQ.getQuestionType());
		myAnswers.put(myAnswers.size() + 1, oAnswer);

	}

	public int GetPercentage() {
		int iRes = 0;
		MP_StatAnswer currAnsewer;
		for (int iCnt = 1; iCnt <= myAnswers.size(); iCnt++) {
			currAnsewer = myAnswers.get(iCnt);
			if (currAnsewer.getIsCorrect()) {
				iRes++;
			}
		}
		return iRes;
	}
}
