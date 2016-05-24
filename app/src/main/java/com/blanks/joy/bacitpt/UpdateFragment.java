package com.blanks.joy.bacitpt;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.blanks.joy.bacitpt.interfaces.FragmentCommute;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

	private FragmentCommute mCallback;
	String rosterType;
	View frag;

	private TextView timeTextView;
	private TextView dateTextView;
	private Button timeButton;
	private Button dateButton;


	private Activity activity;

	private String time;
	private Date date;
	private String[] timeSlots = new String[2];

	public UpdateFragment() {
		// Required empty public constructor
	}
	@Override
	public void onAttach(Context context)
	{
		activity = (Activity)context;
		super.onAttach(context);
		try {
			mCallback = (FragmentCommute) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement FragmentCommute");
		}
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		timeSlots[0] = sharedPref.getString("timeIn", null);
		timeSlots[1] = sharedPref.getString("timeOut", null);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		frag = inflater.inflate(R.layout.fragment_update, container, false);
		//Bundle activityBundle = getArguments();
		timeTextView = (TextView)frag.findViewById(R.id.time_textview);
		dateTextView = (TextView)frag.findViewById(R.id.date_textview);
		timeButton = (Button)frag.findViewById(R.id.time_button);
		dateButton = (Button)frag.findViewById(R.id.date_button);

		rosterType = "P";//picks only
		((Switch)frag.findViewById(R.id.rosterType)).setChecked(true);
		((Switch)frag.findViewById(R.id.rosterType)).setEnabled(false);
		mCallback.setRosterType(rosterType);

		// Show a timepicker when the timeButton is clicked
		timeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] timeSplit = time != null ? time.split(":") : null;
				Calendar now = Calendar.getInstance();
				TimePickerDialog tpd = TimePickerDialog.newInstance(
						UpdateFragment.this,
						(timeSplit != null && timeSplit[0] != null ? Integer.parseInt(timeSplit[0]) : now.get(Calendar.HOUR_OF_DAY)),
						(timeSplit != null && timeSplit[1] != null ? Integer.parseInt(timeSplit[1]) : now.get(Calendar.MINUTE)),
						true
				);
				//tpd.setThemeDark(true);
				tpd.vibrate(false);
				tpd.dismissOnPause(true);
				tpd.enableSeconds(false);
				tpd.setTimeInterval(1, 5, 60);
				tpd.setTitle("Select a time for Pick");
				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {Log.d("TimePicker", "Dialog was cancelled");
					}
				});
				tpd.show(activity.getFragmentManager(), "Timepickerdialog");
			}
		});

		// Show a datepicker when the dateButton is clicked
		dateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar now = Calendar.getInstance();
				DatePickerDialog dpd = DatePickerDialog.newInstance(
						UpdateFragment.this,
						now.get(Calendar.YEAR),
						now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH)
				);
				//dpd.setThemeDark(true);
				dpd.vibrate(false);
				dpd.dismissOnPause(true);
				dpd.showYearPickerFirst(false);
				//dpd.setAccentColor(Color.parseColor("#9C27B0"));
				//dpd.setTitle("DatePicker Title");
				dpd.setTitle("Select a date for Pick");


				/*Calendar[] dates = new Calendar[13];
				for(int i = -6; i <= 6; i++) {
					Calendar date = Calendar.getInstance();
					date.add(Calendar.MONTH, i);
					dates[i+6] = date;
				}
				dpd.setSelectableDays(dates);*/

				if(date != null) {

					Calendar dateShow = Calendar.getInstance();
					dateShow.set(date.getYear(),date.getMonth(),date.getDate());
					Calendar[] dates = {dateShow};

					dpd.setHighlightedDays(dates);
				}
				dpd.show(activity.getFragmentManager(), "Datepickerdialog");
			}
		});
		return frag;
	}


	@Override
	public void onResume() {
		super.onResume();
		TimePickerDialog tpd = (TimePickerDialog) activity.getFragmentManager().findFragmentByTag("Timepickerdialog");
		DatePickerDialog dpd = (DatePickerDialog) activity.getFragmentManager().findFragmentByTag("Datepickerdialog");

		if(tpd != null) tpd.setOnTimeSetListener(this);
		if(dpd != null) dpd.setOnDateSetListener(this);
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
		String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
		String minuteString = minute < 10 ? "0"+minute : ""+minute;
		String secondString = second < 10 ? "0"+second : ""+second;
		String timeSelected = ""+hourString+":"+minuteString+"";
		timeTextView.setText(timeSelected);
		time = timeSelected;
		mCallback.setTime(hourString+minuteString);
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		String dateSelected = ((monthOfYear < 9) ? "0"+(monthOfYear+1) : (monthOfYear+1))+"/"+dayOfMonth+"/"+year;
		mCallback.setDate(dateSelected);

		date = new Date(year,monthOfYear,dayOfMonth);
		Calendar c = Calendar.getInstance(),today = Calendar.getInstance();
		c.set(year,monthOfYear,dayOfMonth);

		//Calendar today = Calendar.getInstance();
		if(c.compareTo(today)==0){
			dateTextView.setText( "TODAY");
		}else {
			dateTextView.setText(dateSelected);
		}
	}
}
