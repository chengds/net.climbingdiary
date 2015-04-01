package net.climbingdiary.activities;

import net.climbingdiary.R;
import net.climbingdiary.adapters.TabAdapter;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.fragments.DiaryFragment;
import net.climbingdiary.fragments.OverallStatsFragment;
import net.climbingdiary.fragments.PlacesFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

public class MainActivity extends TabbedActivity
       implements DiaryFragment.OnEntrySelectedListener {
  
  // Global identifiers
  public final static String EXTRA_ENTRY_ID = "net.climbingdiary.EXTRA_ENTRY_ID";
  public final static String EXTRA_PLACE_ID = "net.climbingdiary.EXTRA_PLACE_ID";
  public final static String EXTRA_PLACE_NAME = "net.climbingdiary.EXTRA_PLACE_NAME";
  public final static String EXTRA_ROUTE_ID = "net.climbingdiary.EXTRA_ROUTE_ID";

  public final static int LOADER_DIARY = 0;           // IDs of data loaders
  public final static int LOADER_PLACES = 1;
  public final static int LOADER_ASCENTS = 2;
  public final static int LOADER_PLACEROUTES = 3;
  public final static int LOADER_ROUTEASCENTS = 4;
  public final static int LOADER_CRAGPYRAMID = 5;
  
  // these objects will handle the information database
  private static DiaryDbHelper dbhelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    // Open/create the SQL database and create the managing adapter.
    dbhelper = DiaryDbHelper.getInstance(this);
    
    // package some extra data in the bundle
    Bundle data = new Bundle();
    
    // create the pager adapter and initialize the layout
    mAdapter = new TabAdapter(getSupportFragmentManager(),this,
        data,new int[]{ R.string.section_diary, R.string.section_places, R.string.section_stats }) {
          @Override
          public Fragment getItem(int i) {
            switch (i) {
            case 0:
              // the first section lists the diary entries
              return new DiaryFragment();
              
            case 1:
              // the second section lists all the places
              return new PlacesFragment();
            
            case 2:
            default:
              // the third section shows overall stats
              return new OverallStatsFragment();
            }
          }
        };
    super.onCreate(savedInstanceState, R.layout.activity_main);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  /*
   * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
   * action bar item clicks here. The action bar will // automatically handle
   * clicks on the Home/Up button, so long // as you specify a parent activity
   * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
   * R.id.action_settings) { return true; } return
   * super.onOptionsItemSelected(item); }
   */

  /*****************************************************************************************************
   *                                          CALLBACKS
   *****************************************************************************************************/
  @Override
  public void onEntrySelected(long id) {
    // pass the id of the clicked item to the new activity
    Intent intent = new Intent(this, EntryActivity.class);
    intent.putExtra(MainActivity.EXTRA_ENTRY_ID, id);
    startActivity(intent);
  }

}