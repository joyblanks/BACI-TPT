package com.blanks.joy.bacitpt.operations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.telephony.SmsMessage;
import android.view.View;

import com.blanks.joy.bacitpt.MainActivity;
import com.blanks.joy.bacitpt.R;

/**
 * Created by Joy on 19/05/16.
 */
public class MessageReceiver extends BroadcastReceiver {
	private Snackbar snackBar; //make it as global
	@Override
	public void onReceive(Context context, Intent intent) {
		if(MainActivity.activity != null) {
			Bundle pudsBundle = intent.getExtras();
			Object[] pdus = (Object[]) pudsBundle.get("pdus");
			SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
			//Log.i("bacitpt",  messages.getMessageBody());
			if(messages.getOriginatingAddress().equalsIgnoreCase("VM-BACTPT")){
				//updateMessage("VM-BACTPT: " + messages.getMessageBody());
				snackBar = Snackbar.make(MainActivity.activity.findViewById(R.id.fab), "VM-BACTPT: " + messages.getMessageBody(), Snackbar.LENGTH_INDEFINITE);
				snackBar.setAction("OK", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						snackBar.dismiss();
					}
				});
				snackBar.show();
			}
		}
	}

	//protected abstract void updateMessage(String msg);
}
