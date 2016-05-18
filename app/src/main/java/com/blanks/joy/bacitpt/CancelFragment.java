package com.blanks.joy.bacitpt;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.blanks.joy.bacitpt.interfaces.FragmentCommute;


/**
 * A simple {@link Fragment} subclass.
 */
public class CancelFragment extends Fragment {

	private FragmentCommute mCallback;
	String rosterType;
	View frag;
	public CancelFragment() {
		// Required empty public constructor
	}
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		try {
			mCallback = (FragmentCommute) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement IFragmentToActivity");
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
		return frag;
	}


}
