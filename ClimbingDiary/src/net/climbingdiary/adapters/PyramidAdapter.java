package net.climbingdiary.adapters;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.Grades;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class PyramidAdapter extends BaseAdapter {

  public PyramidAdapter(Context context, Cursor c, int flags, int layout) {
    super(context, c, flags, layout);
  }

  @Override
  public void bindView(View row, Context context, Cursor c) {
    TextView text2 = (TextView) row.findViewById(R.id.grade);         // route grade
    String grade = c.getString(c.getColumnIndex(Grades.COLUMN_GRADE_YDS));
    text2.setText(grade);
    
    String ascents = c.getString(c.getColumnIndex("ascents"));
    if (ascents == null) ascents = "";
    Log.v(grade, ascents);  
  /*  
    GridView grid = (GridView) row.findViewById(R.id.ascents);         // ascents
    grid.
    text.setText(ascents);*/
    
  }

}
