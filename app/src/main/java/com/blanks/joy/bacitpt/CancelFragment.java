package com.blanks.joy.bacitpt;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

import com.blanks.joy.bacitpt.interfaces.FragmentCommute;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CancelFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

	private FragmentCommute mCallback;
	String rosterType;
	View frag;

	private TextView timeTextView;
	private TextView dateTextView;
	private Activity activity;


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
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		frag = inflater.inflate(R.layout.fragment_cancel, container, false);
		Bundle activityBundle = getArguments();
		rosterType = rosterType==null ? activityBundle.getString("rosterType") : rosterType;
		((Switch)frag.findViewById(R.id.rosterType)).setChecked("P".equals(rosterType));
		((Switch)frag.findViewById(R.id.rosterType)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCallback.setRosterType(isChecked?"P":"D");
			}
		});
		timeTextView = (TextView)frag.findViewById(R.id.time_textview);
		dateTextView = (TextView)frag.findViewById(R.id.date_textview);
		Button timeButton = (Button)frag.findViewById(R.id.time_button);
		Button dateButton = (Button)frag.findViewById(R.id.date_button);

		// Show a timepicker when the timeButton is clicked
		timeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar now = Calendar.getInstance();
				TimePickerDialog tpd = TimePickerDialog.newInstance(
						CancelFragment.this,
						now.get(Calendar.HOUR_OF_DAY),
						now.get(Calendar.MINUTE),
						true
				);
				tpd.setThemeDark(false);
				tpd.vibrate(true);
				tpd.dismissOnPause(true);
				tpd.enableSeconds(true);
				if (false) {
					tpd.setAccentColor(Color.parseColor("#9C27B0"));
				}

				if (true) {
					tpd.setTimeInterval(2, 5, 10);
				}
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
				dpd.vibrate(true);
				dpd.dismissOnPause(true);
				dpd.showYearPickerFirst(false);
				if (false) {
					dpd.setAccentColor(Color.parseColor("#9C27B0"));
				}
				if (false) {
					dpd.setTitle("DatePicker Title");
				}
				if (false) {
					Calendar[] dates = new Calendar[13];
					for(int i = -6; i <= 6; i++) {
						Calendar date = Calendar.getInstance();
						date.add(Calendar.MONTH, i);
						dates[i+6] = date;
					}
					dpd.setSelectableDays(dates);
				}
				if (false) {
					Calendar[] dates = new Calendar[13];
					for(int i = -6; i <= 6; i++) {
						Calendar date = Calendar.getInstance();
						date.add(Calendar.WEEK_OF_YEAR, i);
						dates[i+6] = date;
					}
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
		String time = ""+hourString+":"+minuteString+":"+secondString+"";
		timeTextView.setText(time);
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
		dateTextView.setText(date);
	}
}
