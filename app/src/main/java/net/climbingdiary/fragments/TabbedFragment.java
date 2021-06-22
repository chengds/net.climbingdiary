package net.climbingdiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.climbingdiary.R;
import net.climbingdiary.adapters.ViewPagerAdapter;

public class TabbedFragment extends Fragment {

    protected TabLayout tabLayout;
    protected ViewPager2 viewPager;
    protected ViewPagerAdapter mAdapter;

    /*****************************************************************************************************
     *                                          LIFECYCLE METHODS
     *****************************************************************************************************/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // check adapter is configured
        if (mAdapter == null) {
            throw new ClassCastException(this.toString()
                    + " must configure mAdapter");
        }

        // create the layout, a set of climbing pyramids
        View rootView = inflater.inflate(R.layout.fragment_pager, container, false);

        // connect the pager, adapter, tab layout
        viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(mAdapter);
        tabLayout = rootView.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(mAdapter.mTabNames[position])
        ).attach();
        return rootView;
    }
}
