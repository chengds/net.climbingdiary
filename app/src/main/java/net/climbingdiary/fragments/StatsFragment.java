package net.climbingdiary.fragments;

import java.util.ArrayList;
import java.util.List;

import net.climbingdiary.R;
import net.climbingdiary.activities.GradeActivity;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.StatsAdapter;
import net.climbingdiary.data.DiaryDbHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatsFragment extends Fragment {

    private Context myContext;
    private final int dateModifier;                       // Specifies entries of all time, last year, last month

    public StatsFragment(int modifier) {
        dateModifier = modifier;
    }

    public void onGradeSelected(Context context, String type, String grade) {
        // pass the id and type of the clicked grade to the new activity
        Intent intent = new Intent(context, GradeActivity.class);
        intent.putExtra(MainActivity.EXTRA_PLACE_TYPE, type);
        intent.putExtra(MainActivity.EXTRA_GRADE_VALUE, grade);
        startActivity(intent);
    }

    /*****************************************************************************************************
    *                                          LIFECYCLE METHODS
    *****************************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // create the layout, a set of climbing pyramids
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        // get the db
        DiaryDbHelper dbhelper = DiaryDbHelper.getInstance(getActivity());

        // retrieve all the pyramids with their respective headers
        ArrayList<ArrayList<String>> allPyramid = new ArrayList<>();
        ArrayList<ArrayList<String>> pyramid = dbhelper.getPyramid("Crag", dateModifier);
        if (pyramid != null) {
            allPyramid.add(new ArrayList<>(List.of("", "Crag Pyramid", "Sent", "Tried")));
            allPyramid.addAll(pyramid);
        }
        pyramid = dbhelper.getPyramid("Trad", dateModifier);
        if (pyramid != null) {
            allPyramid.add(new ArrayList<>(List.of("", "Trad Pyramid", "Sent", "Tried")));
            allPyramid.addAll(pyramid);
        }
        pyramid = dbhelper.getPyramid("Wall", dateModifier);
        if (pyramid != null) {
            allPyramid.add(new ArrayList<>(List.of("", "Wall Pyramid", "Sent", "Tried")));
            allPyramid.addAll(pyramid);
        }
        pyramid = dbhelper.getPyramid("Gym", dateModifier);
        if (pyramid != null) {
            allPyramid.add(new ArrayList<>(List.of("", "Gym Pyramid", "Sent", "Tried")));
            allPyramid.addAll(pyramid);
        }

        // populate the list
        RecyclerView list = rootView.findViewById(R.id.pyramid);
        StatsAdapter adapter = new StatsAdapter(getActivity(), allPyramid, view -> {
            TextView grade = view.findViewById(R.id.grade);
            String gradeVal = grade.getText().toString();
            String type = (String) grade.getTag();
            if (!gradeVal.equals("")) onGradeSelected(myContext, type, gradeVal);
        });
        list.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }
}
