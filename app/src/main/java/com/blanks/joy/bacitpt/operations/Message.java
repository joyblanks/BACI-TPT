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

	public static String getContent(int screen, String type, String time, String date){
		if(screen == R.id.croster){
			return "CAN "+type+" "+date+" "+time;
			//return "ETA P 05/19/2016 1300";
		}else if(screen == R.id.uroster){
			return "ROSUP P "+date+" "+time;
		}else if(screen == R.id.eta){
			return "ETA P "+date+" "+time;
		}else if(screen == R.id.req_align){
			return "ALIGN P "+date+" "+time;
		}else if(screen == R.id.reachedsafe){
			return "RSAFE D "+date+" "+time;
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
		}else if(screen == R.id.uroster){
			return "Updating Roster";
		}else if(screen == R.id.eta){
			return "Requesting ETA Information";
		}else if (screen == R.id.req_align){
			return "Requesting Alignment";
		}else if(screen == R.id.reachedsafe){
			return "Sending Message";
		}
		return "";
	}


	public static void sendMessage(int screen, String type, String time, String date) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(getNumber(),null,getContent(screen,type,time,date),null,null);
	}
}
