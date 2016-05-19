package com.blanks.joy.bacitpt.operations;

import android.telephony.SmsManager;

import com.blanks.joy.bacitpt.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Joy on 18/05/16.
 */
public class Message {
	public static String getNumber(){
		return "+919769949082";//Joy
		//return "+919223172361";//BACI SMS Transport
	}

	public static String getContent(int screen, String type, String time){
		if(screen == R.id.croster){
			return "CAN "+type+" "+Message.getTodayDate()+" "+time;
			//return "ETA P 05/19/2016 1300";
		}
		return "";
	}

	private static String getTodayDate(){
		return new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
	}

	public static String getMessage(int screen, String type){
		if(screen == R.id.croster){
			switch (type.charAt(0)){
				case 'P': return "Cancelling Pick";
				case 'D': return "Cancelling Drop";
			}
		}
		return "";
	}


	public static void sendMessage(int screen, String type) {
		String[] time = new String[]{"1300","2100"};
		String sendTime = ("P".equals(type)) ? time[0]:time[1];
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(getNumber(),null,getContent(screen,type,sendTime),null,null);
	}
}
