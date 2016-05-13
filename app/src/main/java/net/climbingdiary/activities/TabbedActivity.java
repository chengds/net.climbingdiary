package net.climbingdiary.activities;

import net.climbingdiary.R;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.MenuItem;

public class TabbedActivity extends AppCompatActivity implements TabListener {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
   * sections of the activity. We use a {@link android.support.v4.app.FragmentPagerAdapter}
   * derivative, which will keep every loaded fragment in memory. If this becomes too memory
   * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  protected FragmentPagerAdapter mAdapter;
  
  /**
   * The {@link ViewPager} that will display the three primary sections of the app, one at a
   * time.
   */
  protected ViewPager mViewPager;                  // tabs stuff
  
  /*****************************************************************************************************
   *                                          CREATE/DESTROY
   *****************************************************************************************************/
  protected void onCreate(Bundle savedInstanceState, int layout) {
    super.onCreate(savedInstanceState);
    
    setContentView(layout);   // setup the layout to display
    
    // Set up the action bar. Specify that we will be displaying tabs in the action bar.
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
  
    // Set up the ViewPager, attaching the adapter and setting up a listener for when the
    // user swipes between sections.
    mViewPager = (ViewPager) findViewById(R.id.pager1);
    mViewPager.setAdapter(mAdapter);
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
          @Override
          public void onPageSelected(int position) {
            // When swiping between different app sections, select the corresponding tab.
            actionBar.setSelectedNavigationItem(position);
          }
        });

    // For each of the sections in the app, add a tab to the action bar.
    for (int i = 0; i < mAdapter.getCount(); i++) {
      // Create a tab with text corresponding to the page title defined by the adapter.
      // Also specify this Activity object, which implements the TabListener interface, as the
      // listener for when this tab is selected.
      actionBar.addTab(actionBar.newTab()
          .setText(mAdapter.getPageTitle(i)).setTabListener(this));
    }
  }
  
  /**********************************************************************************************************
   * {@link ActionBar.TabListener} stuff.
   */
  @Override
  public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
  }

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
    // When the given tab is selected, switch to the corresponding page in the ViewPager.
    mViewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
  }

  // Make the Up button behave like the Back button
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // make Up/Home the same as the Back button
        finish();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
