package net.climbingdiary.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import net.climbingdiary.R;

public class TabbedActivity extends AppCompatActivity {

  protected TabLayout tabLayout;
  protected ViewPager viewPager;
  protected FragmentPagerAdapter mAdapter;
  protected ActionBar actionBar;

  /*****************************************************************************************************
   *                                          CREATE/DESTROY
   *****************************************************************************************************/
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pager);   // default layout

    // connect the pager, adapter, tablayout
    viewPager = (ViewPager) findViewById(R.id.pager1);
    viewPager.setAdapter(mAdapter);
    tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
    tabLayout.setupWithViewPager(viewPager);

    // retrieve the actionbar
    actionBar = getSupportActionBar();

    // Specify that the Home button should show an "Up" caret, indicating that touching the
    // button will take the user one step up in the application's hierarchy.
    actionBar.setDisplayHomeAsUpEnabled(true);

  }

  public static String POSITION = "POSITION";

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }

    return(super.onOptionsItemSelected(item));
  }
}
