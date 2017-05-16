package net.climbingdiary.adapters;

import java.util.ArrayList;
import java.util.List;

import net.climbingdiary.R;
import net.climbingdiary.utils.Graphics;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PyramidAdapter extends ArrayAdapter<ArrayList<String>> {
  private Context context;
  private LayoutInflater inflater;
  private int layout;
  private List<ArrayList<String>> pyramid;

  public PyramidAdapter(Context context, int layout, List<ArrayList<String>> objects) {
    super(context, layout, objects);
    this.context = context;
    this.inflater = LayoutInflater.from(context);
    this.layout = layout;
    this.pyramid = objects;
  }

  @Override
  public View getView(int position, View v, ViewGroup parent) {
    // retrieve/create row layout
    LinearLayout row;
    if (v == null) {
      row = (LinearLayout) inflater.inflate(layout, parent, false);
    } else {
      row = (LinearLayout) v;
    }
    
    // get grade and list of ascents
    ArrayList<String> list = pyramid.get(position);
    
    // set grade text
    TextView grade = (TextView) row.findViewById(R.id.grade);
    grade.setText(list.get(0));

    // convert square size 30sp into pixels
    int side = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30f,
            context.getResources().getDisplayMetrics());

    // set grid of colored squares
    for (int i=0; i<8; i++) {
      // create/retrieve the i-th square
      TextView image;
      if (v == null) {
        image = new TextView(context);
        image.setText(String.valueOf(i+1));
        image.setTextSize(12);
        image.setTextColor(Color.BLACK);
        image.setGravity(Gravity.CENTER);
        image.setPadding(2, 2, 2, 2);
        row.addView(image, side, side);
      } else {
        image = (TextView) row.getChildAt(i+1);
      }
      
      if (i < list.size()-1) {
        image.setBackgroundColor(Graphics.getColor(context, list.get(i+1)));
      } else {
        image.setBackgroundColor(Color.DKGRAY);
      }
    }

    // find the number of completed routes
    int completed = 0;
    while (completed < list.size()-1 && !list.get(completed+1).equalsIgnoreCase("uncompleted")) {
      completed++;
    }

    int sent = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,34f,
        context.getResources().getDisplayMetrics());

    // add count of completed routes
    TextView count;
    if (v == null) {
      count = new TextView(context);
      row.addView(count, sent, side);
    } else {
      count = (TextView) row.getChildAt(9);
    }
    count.setText(String.valueOf(completed));
    count.setTextSize(12);
    count.setTextColor(Color.WHITE);
    count.setGravity(Gravity.CENTER);

    // add count of uncompleted routes
    TextView ucount;
    if (v == null) {
      ucount = new TextView(context);
      row.addView(ucount, sent, side);
    } else {
      ucount = (TextView) row.getChildAt(10);
    }
    ucount.setText(String.valueOf(list.size()-1 - completed));
    ucount.setTextSize(12);
    ucount.setTextColor(Color.GRAY);
    ucount.setGravity(Gravity.CENTER);

    return row;
  }

}
