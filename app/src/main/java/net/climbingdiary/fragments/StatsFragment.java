package net.climbingdiary.fragments;

import java.util.ArrayList;

import net.climbingdiary.R;
import net.climbingdiary.adapters.PyramidAdapter;
import net.climbingdiary.data.DiaryDbHelper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class StatsFragment extends Fragment {

    private OnGradeSelectedListener mCallback;    // Callback for diary entry selection

    /*****************************************************************************************************
    *                                          COMMUNICATION CALLBACK
    * The calling activity must implement this interface and deal with grade selection.
    * For example to show the details side-by-side on a tablet.
    *****************************************************************************************************/
    public interface OnGradeSelectedListener {
        void onGradeSelected(String type, String grade);
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

        // retrieve the pyramids
        DiaryDbHelper dbhelper = DiaryDbHelper.getInstance(getActivity());
        ArrayList<ArrayList<String>> pyramid1 = dbhelper.getPyramid("Crag");
        ArrayList<ArrayList<String>> pyramid2 = dbhelper.getPyramid("Wall");

        // connect the lists and the adapters
        final ListView list1 = rootView.findViewById(R.id.crag_pyramid);
        final ListView list2 = rootView.findViewById(R.id.wall_pyramid);
        list1.setAdapter(new PyramidAdapter(getActivity(), R.layout.item_pyramid, pyramid1));
        list2.setAdapter(new PyramidAdapter(getActivity(), R.layout.item_pyramid, pyramid2));

        // set up callbacks
        list1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // retrieve the grade text
                TextView grade = view.findViewById(R.id.grade);

                mCallback.onGradeSelected("Crag", grade.getText().toString());
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnGradeSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
              + " must implement OnGradeSelectedListener");
        }
    }
}
