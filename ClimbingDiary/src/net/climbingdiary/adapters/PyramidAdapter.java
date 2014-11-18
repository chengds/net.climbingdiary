package net.climbingdiary.adapters;

import java.util.ArrayList;
import java.util.List;

import net.climbingdiary.R;
import net.climbingdiary.utils.Graphics;
import android.content.Context;
import android.graphics.Color;
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
//  private final static int[] imageId = new int[]
//      { R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.image5,
//        R.id.image6, R.id.image7, R.id.image8 }; 

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
    
    // list of ascents
    ArrayList<String> list = pyramid.get(position);
    
    // set grade text
    TextView grade = (TextView) row.findViewById(R.id.grade);
    grade.setText(list.get(0));
    
    // grid of colored squares
    for (int i=0; i<8; i++) {
      TextView image;
      if (v == null) {
        image = new TextView(context);
        image.setText(String.valueOf(i+1));
        image.setTextSize(12);
        image.setTextColor(Color.BLACK);
        image.setGravity(Gravity.CENTER);
        image.setPadding(2, 2, 2, 2);
        row.addView(image,28,28);
      } else {
        image = (TextView) row.getChildAt(i+1);
      }
      
      if (i < list.size()-1) {
        image.setBackgroundColor(Graphics.getColor(context, list.get(i+1)));
      } else {
        image.setBackgroundColor(Color.DKGRAY);
      }
    }
    
    return row;
  }

}
