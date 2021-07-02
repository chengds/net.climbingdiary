package net.climbingdiary.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.climbingdiary.R;
import net.climbingdiary.adapters.TabAdapter;
import net.climbingdiary.fragments.RouteFragment;

/**
 * RouteActivity is an ActionBarActivity that shows the details of a route
 * selected from the parent PlaceActivity/PlaceRoutesFragment.
 */
public class RouteActivity extends TabbedActivity {

    /*****************************************************************************************************
     *                                          LIFECYCLE METHODS
     *****************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // retrieve the entry information to display in the title
        long route_id = getIntent().getLongExtra(MainActivity.EXTRA_ROUTE_ID,0);
        long place_id = getIntent().getLongExtra(MainActivity.EXTRA_PLACE_ID,0);
        String place_name = getIntent().getStringExtra(MainActivity.EXTRA_PLACE_NAME);
        String route_name = getIntent().getStringExtra(MainActivity.EXTRA_ROUTE_NAME);

        // package some extra data in the bundle
        Bundle data = new Bundle();
        data.putLong(MainActivity.EXTRA_ROUTE_ID, route_id);
        data.putLong(MainActivity.EXTRA_PLACE_ID, place_id);
        data.putString(MainActivity.EXTRA_PLACE_NAME, place_name);
        data.putString(MainActivity.EXTRA_ROUTE_NAME, route_name);

        // create the content fragment
        mAdapter = new TabAdapter(getSupportFragmentManager(), this, data,
                new int[] { R.string.route_details }) {
            @NonNull
            @Override
            public Fragment getItem(int i) {
                Fragment route = new RouteFragment();
                route.setArguments(data);
                return route;
            }
        };
        super.onCreate(savedInstanceState);

        // set title
        actionBar.setTitle(route_name);
        actionBar.setSubtitle(place_name);
    }
}
