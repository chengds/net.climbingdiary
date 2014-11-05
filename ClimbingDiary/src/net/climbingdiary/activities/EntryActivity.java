package net.climbingdiary.activities;

import net.climbingdiary.R;
import net.climbingdiary.adapters.TabAdapter;
import net.climbingdiary.data.DiaryContract.Ascents;
import net.climbingdiary.data.DiaryContract.DiaryEntry;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.fragments.AscentDialogFragment;
import net.climbingdiary.fragments.AscentsFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;

public class EntryActivity extends TabbedActivity {

  private DiaryDbHelper dbhelper;   // reference to database helper
  private long entry_id;            // ID of the climbing day entry
  private DiaryEntry.Data info;     // rest of the info
  
  /*****************************************************************************************************
   *                                          CREATE/DESTROY
   *****************************************************************************************************/
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    // Open/create the SQL database and create the managing adapter.
    dbhelper = DiaryDbHelper.getInstance(this);
    
    // retrieve the entry information to display in the title
    entry_id = getIntent().getLongExtra(MainActivity.EXTRA_ENTRY_ID,0);
    info = dbhelper.getEntry(entry_id);
    
    // package some extra data in the bundle
    Bundle data = new Bundle();
    data.putLong(MainActivity.EXTRA_ENTRY_ID, entry_id);
    data.putLong(MainActivity.EXTRA_PLACE_ID, info.place_id);
    data.putString(MainActivity.EXTRA_PLACE_NAME, info.place_name);
        
    // create the pager adapter and initialize the layout
    mAdapter = new TabAdapter(getSupportFragmentManager(),this,
        data,new int[]{ R.string.section_ascents }) {
          @Override
          public Fragment getItem(int i) {
            Fragment ascents = new AscentsFragment();
            ascents.setArguments(data);
            return ascents;
          }
        };
    super.onCreate(savedInstanceState, R.layout.activity_pager);
        
    // show place and date in the title
    final ActionBar actionBar = getSupportActionBar();
    if (info != null) {
      // set title and subtitle
      actionBar.setTitle(info.place_name);
      actionBar.setSubtitle(info.date + " - " + info.type_desc);
    }
  }
  
  /*****************************************************************************************************
   *                                          CALLBACKS
   *****************************************************************************************************/
  /*
   * Callback for the button "Add Ascent"
   */
  public void addAscent(View view) {
    Ascents.Data ainfo = new Ascents.Data();
    ainfo.entry_id = info._id;
    ainfo.place_id = info.place_id;
    DialogFragment newFragment = new AscentDialogFragment(dbhelper,ainfo,R.string.add_ascent,R.string.add);
    newFragment.show(getSupportFragmentManager(), "ascent_entry");
  }
  
}
