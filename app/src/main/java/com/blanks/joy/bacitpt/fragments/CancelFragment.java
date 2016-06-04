package com.blanks.joy.bacitpt.fragments;


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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.blanks.joy.bacitpt.R;
import com.blanks.joy.bacitpt.interfaces.FragmentCommute;
import com.blanks.joy.bacitpt.utils.Constants;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CancelFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

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

	public CancelFragment() {
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
		SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.BACITPT,Context.MODE_PRIVATE);
		timeSlots[0] = sharedPref.getString("timeIn", null);
		timeSlots[1] = sharedPref.getString("timeOut", null);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		frag = inflater.inflate(R.layout.fragment_cancel, container, false);
		//Bundle activityBundle = getArguments();
		timeTextView = (TextView)frag.findViewById(R.id.time_textview);
		dateTextView = (TextView)frag.findViewById(R.id.date_textview);
		timeButton = (Button)frag.findViewById(R.id.time_button);
		dateButton = (Button)frag.findViewById(R.id.date_button);

		rosterType = "P";//rosterType==null ? activityBundle.getString("rosterType") : rosterType;
		((Switch)frag.findViewById(R.id.rosterType)).setChecked("P".equals(rosterType));
		mCallback.setRosterType(rosterType);

		if(timeSlots[0] != null && timeSlots[1] != null){
			time = timeSlots["P".equals(rosterType)?0:1];
			timeTextView.setText(time);
			mCallback.setTime(time.replace(":",""));
		}
		dateTextView.setText("TODAY");
		date = new Date();
		mCallback.setDate(new SimpleDateFormat("MM/dd/yyyy").format(date));

		((Switch)frag.findViewById(R.id.rosterType)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCallback.setRosterType(isChecked?"P":"D");
				if(timeSlots[0] != null && timeSlots[1] != null && ((String)timeTextView.getText()).equalsIgnoreCase(timeSlots[0])
						|| ((String)timeTextView.getText()).equalsIgnoreCase(timeSlots[1])
						|| ((String)timeTextView.getText()).equalsIgnoreCase("")){
					time = timeSlots[isChecked ? 0 : 1];
					timeTextView.setText(time);
					mCallback.setTime(time.replace(":",""));
				}
				timeButton.setText(isChecked?"Pick Time":"Drop Time");
				dateButton.setText(isChecked?"Pick Date":"Drop Date");
			}
		});


		// Show a timepicker when the timeButton is clicked
		timeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] timeSplit = time != null ? time.split(":") : null;

				Calendar now = Calendar.getInstance();
				TimePickerDialog tpd = TimePickerDialog.newInstance(
						CancelFragment.this,
						(timeSplit != null && timeSplit[0] != null ? Integer.parseInt(timeSplit[0]) : now.get(Calendar.HOUR_OF_DAY)),
						(timeSplit != null && timeSplit[1] != null ? Integer.parseInt(timeSplit[1]) : now.get(Calendar.MINUTE)),
						true
				);
				tpd.setThemeDark(false);
				tpd.vibrate(false);
				tpd.dismissOnPause(true);
				tpd.enableSeconds(false);
				tpd.setTimeInterval(1, 5, 60);
				tpd.setTitle("Select a time for "+("P".equalsIgnoreCase(rosterType)?"Pick":"Drop"));
				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {
						Log.d("TimePicker", "Dialog was cancelled");
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
						CancelFragment.this,
						now.get(Calendar.YEAR),
						now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH)
				);
				dpd.setThemeDark(false);
				dpd.vibrate(false);
				dpd.dismissOnPause(true);
				dpd.showYearPickerFirst(false);
				//dpd.setAccentColor(Color.parseColor("#9C27B0"));
				//dpd.setTitle("DatePicker Title");
				dpd.setTitle("Select a date for "+("P".equalsIgnoreCase(rosterType)?"Pick":"Drop"));


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
