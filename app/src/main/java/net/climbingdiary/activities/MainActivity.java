package net.climbingdiary.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.dialogs.DiaryEntryDialogFragment;
import net.climbingdiary.fragments.DiaryFragment;
import net.climbingdiary.fragments.PlacesFragment;
import net.climbingdiary.fragments.TabbedStatsFragment;

public class MainActivity
        extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener
{

    // Local
    protected BottomNavigationView navigation;
    protected FragmentManager fm;
    protected DiaryDbHelper dbhelper;

    // Global identifiers
    public final static String EXTRA_ENTRY_ID = "net.climbingdiary.EXTRA_ENTRY_ID";
    public final static String EXTRA_PLACE_ID = "net.climbingdiary.EXTRA_PLACE_ID";
    public final static String EXTRA_PLACE_NAME = "net.climbingdiary.EXTRA_PLACE_NAME";
    public final static String EXTRA_ROUTE_ID = "net.climbingdiary.EXTRA_ROUTE_ID";
    public final static String EXTRA_ROUTE_NAME = "net.climbingdiary.EXTRA_ROUTE_NAME";
    public final static String EXTRA_GRADE_VALUE = "net.climbingdiary.EXTRA_GRADE_VALUE";
    public final static String EXTRA_PLACE_TYPE = "net.climbingdiary.EXTRA_PLACE_TYPE";
    public final static String EXTRA_TIME_PERIOD = "net.climbingdiary.EXTRA_TIME_PERIOD";

    // IDs of data loaders
    public final static int LOADER_DIARY = 0;
    public final static int LOADER_PLACES = 1;
    public final static int LOADER_ASCENTS = 2;
    public final static int LOADER_PLACEROUTES = 3;
    public final static int LOADER_ROUTEASCENTS = 4;
    public final static int LOADER_GRADE_COMPLETED = 5;

    /*****************************************************************************************************
     *                                          CREATE/DESTROY
     *****************************************************************************************************/
    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Open/create the SQL database and create the managing adapter.
        dbhelper = DiaryDbHelper.getInstance(this);

        // cancel the launch image before showing the main activity
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // setup bottom navigation bar
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null)
        {
            // start the default fragment without adding it to the stack
            fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_main, DiaryFragment.class, null, "diary")
                    .commit();

            // setup callback for add new entry button
            FloatingActionButton addEntry = findViewById(R.id.add_entry);
            addEntry.setOnClickListener(view -> new DiaryEntryDialogFragment(dbhelper).show(fm, "diary_entry"));
        }
    }

    /*****************************************************************************************************
     *                                          CALLBACKS
     *****************************************************************************************************/
    private void refreshView() {
        Fragment fragment;
        String tag;
        if ((fragment = fm.findFragmentById(R.id.content_main)) == null) return;
        if ((tag = fragment.getTag()) == null) return;

        // refresh stats fragment to correctly show boxes or grades settings
        if (tag.equals("stats")) {
            fm.popBackStack();
            navigation.setSelectedItemId(R.id.stats);
        }
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.diary) {
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_main, DiaryFragment.class, null, "diary")
                    .addToBackStack("diary")
                    .commit();

            // setup callback for add new entry button
            ((FloatingActionButton)findViewById(R.id.add_entry)).show();

        } else if (id == R.id.places) {
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_main, PlacesFragment.class, null, "places")
                    .addToBackStack("places")
                    .commit();

            // Hide FAB
            ((FloatingActionButton)findViewById(R.id.add_entry)).hide();

        } else if (id == R.id.stats) {
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_main, TabbedStatsFragment.class, null, "stats")
                    .addToBackStack("stats")
                    .commit();

            // Hide FAB
            ((FloatingActionButton)findViewById(R.id.add_entry)).hide();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // makes sure the correct item is checked in the bottom navigation bar
        Fragment fragment = fm.findFragmentById(R.id.content_main);
        if (fragment == null) return;
        String tag = fragment.getTag();
        if (tag == null) return;
        switch (tag) {
            case "diary":
                navigation.getMenu().findItem(R.id.diary).setChecked(true);
                break;
            case "places":
                navigation.getMenu().findItem(R.id.places).setChecked(true);
                break;
            case "stats":
                navigation.getMenu().findItem(R.id.stats).setChecked(true);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }
}
