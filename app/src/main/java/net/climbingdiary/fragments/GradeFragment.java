package net.climbingdiary.fragments;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.GradeCompletedAdapter;
import net.climbingdiary.data.DiaryContract.Grades;

/**
 * GradeFragment shows the routes sent/attempted at a given grade.
 *
 * Created by ChengDS on 6/20/2015.
 */
public class GradeFragment extends LoaderFragment {

  private String place_type;
  private Grades.Data info;

  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    // retrieve place type and grade value from arguments bundle
    Bundle data = getArguments();
    if (data == null) {
      throw new Error("Unable to create RouteFragment: null bundle.");
    }

    // retrieve  details
    place_type  = data.getString(MainActivity.EXTRA_PLACE_TYPE);
    String value = data.getString(MainActivity.EXTRA_GRADE_VALUE);
    if (dbhelper.getSetting("useFrenchGrades").equals("on")) {
      info = dbhelper.getGradeFR(value);
    } else {
      info = dbhelper.getGradeYDS(value);
    }

    // create the layout
    View rootView = inflater.inflate(R.layout.fragment_grade_details, container, false);

    // fill in the info
    TextView text1 = (TextView) rootView.findViewById(R.id.type);
    text1.setText(place_type);
    TextView text2 = (TextView) rootView.findViewById(R.id.grade);
    text2.setText(info.yds + " - " + info.french);

    // connect the list view with the custom diary adapter
    final ListView completed = (ListView) rootView.findViewById(R.id.completed);
    mAdapter = new GradeCompletedAdapter(getActivity(), null, 0, R.layout.item_grade_routes);
    completed.setAdapter(mAdapter);

    // Prepare the data loaders
    initLoader(MainActivity.LOADER_GRADE_COMPLETED);

    return rootView;
  }

  /*****************************************************************************************************
   *                                          DATA RETRIEVAL
   *****************************************************************************************************/
  @Override
  public Cursor dataRetrieval() {
    return dbhelper.getCompleted(info._id, place_type);
  }
}
