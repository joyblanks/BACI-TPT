package com.blanks.joy.bacitpt.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blanks.joy.bacitpt.R;
import com.blanks.joy.bacitpt.utils.Constants;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment{


	public SettingFragment() {
		// Required empty public constructor
	}

	//private FragmentCommute mCallback;
	View frag;

	private TextView timeInTextView;
	private TextView timeOutTextView;
	private TextInputEditText nbk;
	private Activity activity;

	@Override
	public void onAttach(Context context) {
		activity = (Activity) context;
		super.onAttach(context);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		frag = inflater.inflate(R.layout.fragment_setting, container, false);

		timeInTextView = (TextView)frag.findViewById(R.id.timein_textview);
		timeOutTextView = (TextView)frag.findViewById(R.id.timeout_textview);
		Button timeInButton = (Button)frag.findViewById(R.id.timein_button);
		Button timeOutButton = (Button)frag.findViewById(R.id.timeout_button);


		SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.BACITPT,Context.MODE_PRIVATE);
		timeInTextView.setText(sharedPref.getString("timeIn", null));
		timeOutTextView.setText(sharedPref.getString("timeOut", null));

		
		// Show a timepicker when the timeButton is clicked
		timeInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar now = Calendar.getInstance();
				TimePickerDialog tpd = TimePickerDialog.newInstance(
						timeIn,
						now.get(Calendar.HOUR_OF_DAY),
						now.get(Calendar.MINUTE),
						true
				);
				tpd.setThemeDark(false);
				tpd.vibrate(false);
				tpd.dismissOnPause(true);
				tpd.setTitle("Set Regular Pick Time");
				tpd.enableSeconds(false);
				if (false) {
					tpd.setAccentColor(Color.parseColor("#9C27B0"));
				}


				tpd.setTimeInterval(1, 5, 60);

				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {
						Log.d("TimePicker", "Dialog was cancelled");
					}
				});
				tpd.show(activity.getFragmentManager(), "TimeInpickerdialog");
			}
		});

		// Show a timepicker when the timeButton is clicked
		timeOutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar now = Calendar.getInstance();
				TimePickerDialog tpd = TimePickerDialog.newInstance(
						timeOut,
						now.get(Calendar.HOUR_OF_DAY),
						now.get(Calendar.MINUTE),
						true
				);
				tpd.setThemeDark(false);
				tpd.vibrate(false);
				tpd.dismissOnPause(true);
				tpd.enableSeconds(false);
				tpd.setTitle("Set Regular Drop Time");
				tpd.setTimeInterval(1, 5, 60);
				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {
						Log.d("TimePicker", "Dialog was cancelled");
					}
				});
				tpd.show(activity.getFragmentManager(), "TimeOutpickerdialog");
			}
		});
		return frag;
	}

	@Override
	public void onResume() {
		super.onResume();
		TimePickerDialog tInpd = (TimePickerDialog) activity.getFragmentManager().findFragmentByTag("TimeInpickerdialog");
		if(tInpd != null) tInpd.setOnTimeSetListener(timeIn);

		TimePickerDialog tOutpd = (TimePickerDialog) activity.getFragmentManager().findFragmentByTag("TimeOutpickerdialog");
		if(tOutpd != null) tOutpd.setOnTimeSetListener(timeOut);

	}



	public TimePickerDialog.OnTimeSetListener timeIn = new TimePickerDialog.OnTimeSetListener(){
		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
			String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
			String minuteString = minute < 10 ? "0"+minute : ""+minute;
			//String secondString = second < 10 ? "0"+second : ""+second;
			String time = ""+hourString+":"+minuteString+"";
			timeInTextView.setText(time);
			SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.BACITPT,Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("timeIn", time);
			editor.commit();

		}
	};
	public TimePickerDialog.OnTimeSetListener timeOut = new TimePickerDialog.OnTimeSetListener(){
		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
			String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
			String minuteString = minute < 10 ? "0"+minute : ""+minute;
			//String secondString = second < 10 ? "0"+second : ""+second;
			String time = ""+hourString+":"+minuteString+"";
			timeOutTextView.setText(time);
			SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.BACITPT,Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("timeOut", time);
			editor.commit();
		}
	};


}
