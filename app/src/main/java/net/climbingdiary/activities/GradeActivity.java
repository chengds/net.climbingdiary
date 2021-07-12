package net.climbingdiary.activities;

import net.climbingdiary.R;
import net.climbingdiary.fragments.GradeFragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * GradeActivity is an ActionBarActivity that shows the list of sent and
 * attempted routes of a given grade selected from the parent
 * MainActivity/OverallStatsFragment.
 *
 * Created by ChengDS on 6/19/2015.
 */
public class GradeActivity extends AppCompatActivity {

    protected ActionBar actionBar;

    /*****************************************************************************************************
     *                                          LIFECYCLE METHODS
     *****************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve the selected information
        String place_type = getIntent().getStringExtra(MainActivity.EXTRA_PLACE_TYPE);
        String grade_value = getIntent().getStringExtra(MainActivity.EXTRA_GRADE_VALUE);
        int time_period = getIntent().getIntExtra(MainActivity.EXTRA_TIME_PERIOD, 0);

        // package some extra data in the bundle
        Bundle data = new Bundle();
        data.putString(MainActivity.EXTRA_PLACE_TYPE, place_type);
        data.putString(MainActivity.EXTRA_GRADE_VALUE, grade_value);
        data.putInt(MainActivity.EXTRA_TIME_PERIOD, time_period);

        // create layout
        setContentView(R.layout.activity_grade);

        // fragment management
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag1, GradeFragment.class, data, "grade_list")
                .commit();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.grade_details);
        }
    }

    /*****************************************************************************************************
     *                                          CALLBACKS
     *****************************************************************************************************/
    // Make the Up button behave like the Back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // make Up/Home the same as the Back button
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
