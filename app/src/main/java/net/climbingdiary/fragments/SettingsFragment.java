package net.climbingdiary.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryDbHelper;

/**
 * Created by ChengDS on 5/15/2017.
 */

public class SettingsFragment extends Fragment {
	/*****************************************************************************************************
	 *                                          LIFECYCLE METHODS
	 *****************************************************************************************************/
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// create the layout
		View rootView = inflater.inflate(R.layout.fragment_settings_general, container, false);

		// retrieve the settings values
		DiaryDbHelper dbhelper = DiaryDbHelper.getInstance(getActivity());

		// display the correct values
		final SwitchCompat useFrench = (SwitchCompat) rootView.findViewById(R.id.useFrenchGrades);
		String value = dbhelper.getSetting("useFrenchGrades");
		useFrench.setChecked(value.equals("on"));

		// set up callbacks
		useFrench.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				DiaryDbHelper dbhelper = DiaryDbHelper.getInstance(getActivity());
				if (isChecked) {
					dbhelper.updateSetting("useFrenchGrades", "on");
				} else {
					dbhelper.updateSetting("useFrenchGrades", "off");
				}
			}
		});

		return rootView;
	}
}
