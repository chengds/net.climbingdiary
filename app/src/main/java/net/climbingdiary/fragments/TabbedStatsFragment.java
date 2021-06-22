package net.climbingdiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.climbingdiary.R;
import net.climbingdiary.adapters.ViewPagerAdapter;


public class TabbedStatsFragment extends TabbedFragment {

    /*****************************************************************************************************
     *                                          LIFECYCLE METHODS
     *****************************************************************************************************/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // create three views for the stats
        mAdapter = new ViewPagerAdapter(
                getActivity(),
                new Fragment[] {
                        new StatsFragment(0),
                        new StatsFragment(1),
                        new StatsFragment(2),
                },
                new String[] {
                        getString(R.string.all_time),
                        getString(R.string.last_year),
                        getString(R.string.last_month),
                });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
