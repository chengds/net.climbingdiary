package net.climbingdiary.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public abstract class TabAdapter extends FragmentPagerAdapter {

  private int[] sections;
  private Context context;
  protected Bundle data;
  
  public TabAdapter(FragmentManager fm, Context context, Bundle data, int[] sections) {
    super(fm);
    this.context = context;
    this.data = data;
    this.sections = sections;
  }
  
  @Override
  public int getCount() {
    return sections.length;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return context.getText(sections[position]);
  }
}
