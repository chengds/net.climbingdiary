package net.climbingdiary.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.fragments.DiaryFragment;
import net.climbingdiary.fragments.StatsFragment;
import net.climbingdiary.fragments.PlacesFragment;
import net.climbingdiary.fragments.TabbedStatsFragment;

public class MainActivity extends AppCompatActivity
       implements DiaryFragment.OnEntrySelectedListener,
                  PlacesFragment.OnPlaceSelectedListener,
                  StatsFragment.OnGradeSelectedListener {

  protected ActionBar actionBar;
  protected BottomNavigationView navigation;

  protected Fragment fragment1;
  protected Fragment fragment2;
  protected Fragment fragment3;
  protected FragmentManager fm;
  protected Fragment active;

  // Global identifiers
  public final static String EXTRA_ENTRY_ID = "net.climbingdiary.EXTRA_ENTRY_ID";
  public final static String EXTRA_PLACE_ID = "net.climbingdiary.EXTRA_PLACE_ID";
  public final static String EXTRA_PLACE_NAME = "net.climbingdiary.EXTRA_PLACE_NAME";
  public final static String EXTRA_ROUTE_ID = "net.climbingdiary.EXTRA_ROUTE_ID";
  public final static String EXTRA_GRADE_VALUE = "net.climbingdiary.EXTRA_GRADE_VALUE";
  public final static String EXTRA_PLACE_TYPE = "net.climbingdiary.EXTRA_PLACE_TYPE";

  public final static int LOADER_DIARY = 0;           // IDs of data loaders
  public final static int LOADER_PLACES = 1;
  public final static int LOADER_ASCENTS = 2;
  public final static int LOADER_PLACEROUTES = 3;
  public final static int LOADER_ROUTEASCENTS = 4;
  public final static int LOADER_GRADE_COMPLETED = 5;

  // these objects will handle the information database
  private static DiaryDbHelper dbhelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    // Open/create the SQL database and create the managing adapter.
    dbhelper = DiaryDbHelper.getInstance(this);

    // package some extra data in the bundle
    Bundle data = new Bundle();

    // cancel the launch image before showing the main activity
    setTheme(R.style.AppTheme);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Create the permanent fragments only when starting from scratch
    if (savedInstanceState == null) {
        fragment1 = new DiaryFragment();
        fragment2 = new PlacesFragment();
        fragment3 = new TabbedStatsFragment();
    }

    // hide all the fragments except the first
    fm = getSupportFragmentManager();
    fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
    fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
    fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
    active = fragment1;

    // setup the actionbar
    if ((actionBar = getSupportActionBar()) != null) {
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    // setup bottom navigation bar
    navigation = findViewById(R.id.bottom_navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
          = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
      int id = item.getItemId();
      if (id == R.id.diary) {
        fm.beginTransaction().hide(active).show(fragment1).commit();
        active = fragment1;
        return true;
      } else if (id == R.id.places) {
        fm.beginTransaction().hide(active).show(fragment2).commit();
        active = fragment2;
        return true;
      } else if (id == R.id.stats) {
          fm.beginTransaction().hide(active).show(fragment3).commit();
          active = fragment3;
          return true;
      }
      return false;
    }
  };

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

  @Override
  public void onPlaceSelected(long id, String name) {
    // pass the id and name of the clicked place to the new activity
    Intent intent = new Intent(this, PlaceActivity.class);
    intent.putExtra(MainActivity.EXTRA_PLACE_ID, id);
    intent.putExtra(MainActivity.EXTRA_PLACE_NAME, name);
    startActivity(intent);
  }

  @Override
  public void onGradeSelected(String type, String grade) {
    // pass the id and type of the clicked grade to the new activity
    Intent intent = new Intent(this, GradeActivity.class);
    intent.putExtra(MainActivity.EXTRA_PLACE_TYPE, type);
    intent.putExtra(MainActivity.EXTRA_GRADE_VALUE, grade);
    startActivity(intent);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() ==  R.id.action_settings) {
          // open the settings page
          Intent intent = new Intent(this, SettingsActivity.class);
          startActivity(intent);
          return true;
      } else {
          return super.onOptionsItemSelected(item);
      }
  }
}
