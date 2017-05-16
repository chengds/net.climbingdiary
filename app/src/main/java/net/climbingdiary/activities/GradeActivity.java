package net.climbingdiary.activities;

import net.climbingdiary.R;
import net.climbingdiary.fragments.GradeFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * GradeActivity is an ActionBarActivity that shows the list of sent and
 * attempted routes of a given grade selected from the parent
 * MainActivity/OverallStatsFragment.
 *
 * Created by ChengDS on 6/19/2015.
 */
public class GradeActivity extends AppCompatActivity {

  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // retrieve the selected information
    String place_type = getIntent().getStringExtra(MainActivity.EXTRA_PLACE_TYPE);
    String grade_value = getIntent().getStringExtra(MainActivity.EXTRA_GRADE_VALUE);

    // package some extra data in the bundle
    Bundle data = new Bundle();
    data.putString(MainActivity.EXTRA_PLACE_TYPE, place_type);
    data.putString(MainActivity.EXTRA_GRADE_VALUE, grade_value);

    // create layout
    setContentView(R.layout.activity_grade);

    // retrieve fragment management
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    // add the grade details fragment
    GradeFragment frag = new GradeFragment();
    frag.setArguments(data);
    transaction.add(R.id.frag1, frag);
    transaction.commit();

    // Specify that the Home button should show an "Up" caret, indicating that touching the
    // button will take the user one step up in the application's hierarchy.
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle(R.string.grade_details);
  }

  /*****************************************************************************************************
   *                                          CALLBACKS
   *****************************************************************************************************/
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
