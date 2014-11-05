package net.climbingdiary.activities;

import net.climbingdiary.R;
import net.climbingdiary.adapters.TabAdapter;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.data.DiaryContract.Routes;
import net.climbingdiary.fragments.PlaceRoutesFragment;
import net.climbingdiary.fragments.RouteDialogFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;

public class PlaceActivity extends TabbedActivity {

  private DiaryDbHelper dbhelper;   // reference to database helper
  private long place_id;            // ID of the climbing place
  private String place_name;
  
  /*****************************************************************************************************
   *                                          CREATE/DESTROY
   *****************************************************************************************************/
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    
    // Open/create the SQL database and create the managing adapter.
    dbhelper = DiaryDbHelper.getInstance(this);
    
    // retrieve the entry information to display in the title
    place_id = getIntent().getLongExtra(MainActivity.EXTRA_PLACE_ID,0);
    place_name = getIntent().getStringExtra(MainActivity.EXTRA_PLACE_NAME);

    // package some extra data in the bundle
    Bundle data = new Bundle();
    data.putLong(MainActivity.EXTRA_PLACE_ID, place_id);

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
    super.onCreate(savedInstanceState, R.layout.activity_pager);

    // set title
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle(place_name);
  }
  
  /*****************************************************************************************************
   *                                          CALLBACKS
   *****************************************************************************************************/
  public void addRoute(View view) {
    Routes.Data rinfo = new Routes.Data();
    rinfo._id = -1;
    rinfo.place_id = place_id;
    rinfo.place_name = place_name;
    DialogFragment newFragment =
        new RouteDialogFragment(dbhelper,rinfo,R.string.add_route,R.string.add);
    newFragment.show(getSupportFragmentManager(), "route_entry");    
  }
}
