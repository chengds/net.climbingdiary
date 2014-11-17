package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.PyramidAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class OverallStatsFragment extends LoaderFragment {

  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // create the layout, a set of climbing pyramids
    View rootView = inflater.inflate(R.layout.fragment_overallstats, container, false);
    
    // connect the list view with the custom diary adapter
    final ListView pyramid = (ListView) rootView.findViewById(R.id.crag_pyramid);
    mAdapter = new PyramidAdapter(getActivity(), null, 0, R.layout.item_pyramid);
    pyramid.setAdapter(mAdapter);
    
    // Prepare the data loaders
    initLoader(MainActivity.LOADER_CRAGPYRAMID);
    
    return rootView;
  }
  
  /*****************************************************************************************************
   *                                          DATA RETRIEVAL
   *****************************************************************************************************/
  @Override
  public Cursor dataRetrieval() {
    return dbhelper.getPyramid("Crag");
  }

}
