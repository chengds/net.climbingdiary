package net.climbingdiary.fragments;

import java.util.ArrayList;

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

        // retrieve and set the crag pyramid
        ArrayList<ArrayList<String>> pyramid = dbhelper.getPyramid("Crag", dateModifier);
        if (pyramid != null) {
            RecyclerView list = rootView.findViewById(R.id.crag_pyramid);
            StatsAdapter adapter = new StatsAdapter(getActivity(), pyramid, view -> {
                TextView grade = view.findViewById(R.id.grade);
                onGradeSelected(myContext, "Crag", grade.getText().toString());
            });
            list.setAdapter(adapter);
        }

        // retrieve and set the trad pyramid
        pyramid = dbhelper.getPyramid("Trad", dateModifier);
        if (pyramid != null) {
            RecyclerView list = rootView.findViewById(R.id.trad_pyramid);
            StatsAdapter adapter = new StatsAdapter(getActivity(), pyramid, view -> {
                TextView grade = view.findViewById(R.id.grade);
                onGradeSelected(myContext, "Trad", grade.getText().toString());
            });
            list.setAdapter(adapter);
        }

        // retrieve and set the wall pyramid
        pyramid = dbhelper.getPyramid("Wall", dateModifier);
        if (pyramid != null) {
            RecyclerView list = rootView.findViewById(R.id.wall_pyramid);
            StatsAdapter adapter = new StatsAdapter(getActivity(), pyramid, view -> {
                TextView grade = view.findViewById(R.id.grade);
                onGradeSelected(myContext, "Wall", grade.getText().toString());
            });
            list.setAdapter(adapter);
        }

        // retrieve and set the gym pyramid
        pyramid = dbhelper.getPyramid("Gym", dateModifier);
        if (pyramid != null) {
            RecyclerView list = rootView.findViewById(R.id.gym_pyramid);
            StatsAdapter adapter = new StatsAdapter(getActivity(), pyramid, view -> {
                TextView grade = view.findViewById(R.id.grade);
                onGradeSelected(myContext, "Gym", grade.getText().toString());
            });
            list.setAdapter(adapter);
        }

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }
}
