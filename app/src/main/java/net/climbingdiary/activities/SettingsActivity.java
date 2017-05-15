package net.climbingdiary.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

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
		super.onCreate(savedInstanceState, R.layout.activity_pager);

		// set title
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.action_settings);

		// Specify that the Home button should show an "Up" caret, indicating that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
}
