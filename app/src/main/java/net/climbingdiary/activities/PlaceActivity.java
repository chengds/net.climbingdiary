package net.climbingdiary.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import net.climbingdiary.R;
import net.climbingdiary.adapters.TabAdapter;
import net.climbingdiary.fragments.PlaceRoutesFragment;

public class PlaceActivity extends TabbedActivity {

  private long place_id;            // ID of the climbing place
  private String place_name;        // name of the climbing place
  
  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    
    // retrieve the entry information to display in the title
    place_id = getIntent().getLongExtra(MainActivity.EXTRA_PLACE_ID,0);
    place_name = getIntent().getStringExtra(MainActivity.EXTRA_PLACE_NAME);

    // package some extra data in the bundle
    Bundle data = new Bundle();
    data.putLong(MainActivity.EXTRA_PLACE_ID, place_id);
    data.putString(MainActivity.EXTRA_PLACE_NAME, place_name);

    // create the pager adapter and initialize the layout
    mAdapter = new TabAdapter(getSupportFragmentManager(), this, data,
        new int[] { R.string.section_routes }) {
          @Override
          public Fragment getItem(int i) {
            Fragment proutes = new PlaceRoutesFragment();
            proutes.setArguments(data);
            return proutes; 
          }
        };
    super.onCreate(savedInstanceState);

    // set title
    actionBar.setTitle(place_name);
  }

}
