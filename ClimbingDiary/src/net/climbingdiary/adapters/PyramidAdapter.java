package net.climbingdiary.adapters;

import java.util.ArrayList;
import java.util.List;

import net.climbingdiary.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class PyramidAdapter extends ArrayAdapter<ArrayList<String>> {
  private LayoutInflater inflater;
  private int layout;
  private List<ArrayList<String>> pyramid;

  public PyramidAdapter(Context context, int layout, List<ArrayList<String>> objects) {
    super(context, layout, objects);
    this.inflater = LayoutInflater.from(context);
    this.layout = layout;
    this.pyramid = objects;
  }

  @Override
  public View getView(int position, View v, ViewGroup parent) {
    // list of ascents
    ArrayList<String> list = pyramid.get(position);
    
    if (v == null) {
      v = inflater.inflate(layout, parent, false);
    }
    TextView grade = (TextView) v.findViewById(R.id.grade);
    grade.setText(list.get(0));
    
    // grid of colored dots
    GridView ascents = (GridView) v.findViewById(R.id.ascents);
    ArrayAdapter<String> adapter = 
        new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
    ascents.setAdapter(adapter);
    
    return v;
  }

}
