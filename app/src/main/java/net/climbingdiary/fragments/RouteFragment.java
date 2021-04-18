package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.RouteAscentsAdapter;
import net.climbingdiary.data.DiaryContract.Routes;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class RouteFragment extends LoaderFragment {

  private Routes.Data info;         // info about the selected route

  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {

    // retrieve route ID from arguments bundle
    Bundle data = getArguments();
    if (data == null) {
      throw new Error("Unable to create RouteFragment: null bundle.");
    }

    // retrieve route details
    long id = data.getLong(MainActivity.EXTRA_ROUTE_ID);
    info = dbhelper.getRoute(id);
    
    // create the layout
    View rootView = inflater.inflate(R.layout.fragment_route_details, container, false);
    
    // fill in the info
    TextView text1 = (TextView) rootView.findViewById(R.id.name);
    text1.setText(info.name);
    TextView text2 = (TextView) rootView.findViewById(R.id.place);
    text2.setText(info.place_name);
    TextView text3 = (TextView) rootView.findViewById(R.id.grade);
    text3.setText(info.grade_yds + " - " + info.grade_fr);
    TextView text4 = (TextView) rootView.findViewById(R.id.notes);
    text4.setText(info.notes);

    // connect the list view with the custom diary adapter
    final ListView ascents = (ListView) rootView.findViewById(R.id.ascents);
    mAdapter = new RouteAscentsAdapter(getActivity(), null, 0, R.layout.item_route_ascents);
    ascents.setAdapter(mAdapter);
    
    // Prepare the data loaders
    initLoader(MainActivity.LOADER_ROUTEASCENTS);
    
    return rootView;
  }

  /*****************************************************************************************************
   *                                          DATA RETRIEVAL
   *****************************************************************************************************/
  @Override
  public Cursor dataRetrieval() {
    return dbhelper.getRouteAscents(info._id);
  }

}
