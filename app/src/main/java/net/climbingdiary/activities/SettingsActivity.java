package net.climbingdiary.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import net.climbingdiary.R;
import net.climbingdiary.adapters.TabAdapter;
import net.climbingdiary.fragments.SettingsFragment;

/**
 * Where the user can change app settings.
 * Created by ChengDS on 5/14/2017.
 */

public class SettingsActivity extends TabbedActivity {
	/*****************************************************************************************************
	 *                                          CREATE/DESTROY
	 *****************************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// create the pager adapter and initialize the layout
		mAdapter = new TabAdapter(getSupportFragmentManager(),this,
				new Bundle(),new int[]{ R.string.general }) {
			@Override
			public Fragment getItem(int i) {
				Fragment settings = new SettingsFragment();
				settings.setArguments(data);
				return settings;
			}
		};
		super.onCreate(savedInstanceState);

		// set title
		actionBar.setTitle(R.string.action_settings);
	}
}
