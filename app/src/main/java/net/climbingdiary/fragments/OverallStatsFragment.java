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
    
    // retrieve the pyramids
    DiaryDbHelper dbhelper = DiaryDbHelper.getInstance(getActivity());
    ArrayList<ArrayList<String>> pyramid1 = dbhelper.getPyramid("Crag");
    ArrayList<ArrayList<String>> pyramid2 = dbhelper.getPyramid("Wall");
    
    // connect the lists and the adapters
    final ListView list1 = (ListView) rootView.findViewById(R.id.crag_pyramid);
    final ListView list2 = (ListView) rootView.findViewById(R.id.wall_pyramid);
    list1.setAdapter(new PyramidAdapter(getActivity(), R.layout.item_pyramid, pyramid1));
    list2.setAdapter(new PyramidAdapter(getActivity(), R.layout.item_pyramid, pyramid2));
    
    return rootView;
  }
  
}
