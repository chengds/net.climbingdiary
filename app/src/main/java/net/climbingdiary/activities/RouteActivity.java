package net.climbingdiary.activities;

import net.climbingdiary.R;
import net.climbingdiary.fragments.RouteFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * RouteActivity is an ActionBarActivity that shows the details of a route
 * selected from the parent PlaceActivity/PlaceRoutesFragment.
 */
public class RouteActivity extends AppCompatActivity {

  private long route_id;            // ID of the route
  private long place_id;            // ID of the climbing place
  private String place_name;        // name of the climbing place

  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // retrieve the entry information to display in the title
    route_id = getIntent().getLongExtra(MainActivity.EXTRA_ROUTE_ID,0);
    place_id = getIntent().getLongExtra(MainActivity.EXTRA_PLACE_ID,0);
    place_name = getIntent().getStringExtra(MainActivity.EXTRA_PLACE_NAME);

    // package some extra data in the bundle
    Bundle data = new Bundle();
    data.putLong(MainActivity.EXTRA_ROUTE_ID, route_id);
    data.putLong(MainActivity.EXTRA_PLACE_ID, place_id);
    data.putString(MainActivity.EXTRA_PLACE_NAME, place_name);

    // create layout
    setContentView(R.layout.activity_route);
    
    // retrieve fragment management
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    
    // add the route fragment
    RouteFragment frag = new RouteFragment();
    frag.setArguments(data);
    transaction.add(R.id.frag1, frag);
    transaction.commit();

    // Specify that the Home button should show an "Up" caret, indicating that touching the
    // button will take the user one step up in the application's hierarchy.
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle(R.string.route_details);
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
