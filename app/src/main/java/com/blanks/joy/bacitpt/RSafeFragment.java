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
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class RSafeFragment extends Fragment implements TimePickerDialog.OnTimeSetListener{

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

	public RSafeFragment() {
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
		frag = inflater.inflate(R.layout.fragment_rsafe, container, false);
		//Bundle activityBundle = getArguments();
		timeTextView = (TextView)frag.findViewById(R.id.time_textview);
		dateTextView = (TextView)frag.findViewById(R.id.date_textview);
		timeButton = (Button)frag.findViewById(R.id.time_button);
		dateButton = (Button)frag.findViewById(R.id.date_button);

		dateButton.setEnabled(false);
		dateButton.setText("Drop Date");
		dateTextView.setText("TODAY");
		date = new Date();
		mCallback.setDate(new SimpleDateFormat("MM/dd/yyyy").format(date));
		if(timeSlots[1] != null){
			time = timeSlots[1];
			timeTextView.setText(time);
			mCallback.setTime(time.replace(":",""));
		}

		rosterType = "D";//drop only
		((Switch)frag.findViewById(R.id.rosterType)).setChecked(false);
		((Switch)frag.findViewById(R.id.rosterType)).setEnabled(false);
		mCallback.setRosterType(rosterType);

		// Show a timepicker when the timeButton is clicked
		timeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] timeSplit = time != null ? time.split(":") : null;
				Calendar now = Calendar.getInstance();
				TimePickerDialog tpd = TimePickerDialog.newInstance(
						RSafeFragment.this,
						(timeSplit != null && timeSplit[0] != null ? Integer.parseInt(timeSplit[0]) : now.get(Calendar.HOUR_OF_DAY)),
						(timeSplit != null && timeSplit[1] != null ? Integer.parseInt(timeSplit[1]) : now.get(Calendar.MINUTE)),
						true
				);
				//tpd.setThemeDark(true);
				tpd.vibrate(false);
				tpd.dismissOnPause(true);
				tpd.enableSeconds(false);
				tpd.setTimeInterval(1, 5, 60);
				tpd.setTitle("Select a time for Drop");
				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {Log.d("TimePicker", "Dialog was cancelled");
					}
				});
				tpd.show(activity.getFragmentManager(), "Timepickerdialog");
			}
		});

		return frag;
	}


	@Override
	public void onResume() {
		super.onResume();
		TimePickerDialog tpd = (TimePickerDialog) activity.getFragmentManager().findFragmentByTag("Timepickerdialog");
		if(tpd != null) tpd.setOnTimeSetListener(this);

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

}
