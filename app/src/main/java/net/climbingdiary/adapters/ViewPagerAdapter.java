package net.climbingdiary.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// This class is used to handle a ViewPager2 containing fragments.
// The constructor receives the parent activity, the list of fragments and the list of tab names.
public class ViewPagerAdapter extends FragmentStateAdapter {

    private Fragment[] mFragments;
    public final String[] mTabNames;

    public ViewPagerAdapter(FragmentActivity fa, Fragment[] newFragments, String[] newNames) {
        //Pager constructor receives Activity instance
        super(fa);
        // save fragments and tab names
        mFragments = newFragments;
        mTabNames = newNames;
    }

    @Override
    public int getItemCount() {
        //Number of fragments displayed
        return mFragments.length;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments[position];
    }
}