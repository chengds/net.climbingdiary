package net.climbingdiary.fragments;

import java.util.ArrayList;

import net.climbingdiary.R;
import net.climbingdiary.adapters.PyramidAdapter;
import net.climbingdiary.data.DiaryDbHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class OverallStatsFragment extends Fragment {

  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // create the layout, a set of climbing pyramids
    View rootView = inflater.inflate(R.layout.fragment_overallstats, container, false);
    
    // retrieve the pyramid
    DiaryDbHelper dbhelper = DiaryDbHelper.getInstance(getActivity());
    ArrayList<ArrayList<String>> pyramid = dbhelper.getPyramid("Crag");
    
    // connect the list and the adapter
    final ListView list = (ListView) rootView.findViewById(R.id.crag_pyramid);
    list.setAdapter(new PyramidAdapter(getActivity(), R.layout.item_pyramid, pyramid));
    
    return rootView;
  }
  
}
