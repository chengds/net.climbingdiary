package net.climbingdiary.adapters;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.AscentTypes;
import net.climbingdiary.data.DiaryContract.Ascents;
import net.climbingdiary.data.DiaryContract.Grades;
import net.climbingdiary.data.DiaryContract.Routes;
import net.climbingdiary.utils.Graphics;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class AscentsAdapter extends BaseAdapter {

  protected boolean useFrenchGrades = false;

  public AscentsAdapter(Context context, Cursor c, int flags, int layout) {
    super(context, c, flags, layout);
  }

  public void useFrenchGrades(boolean value) {
    useFrenchGrades = value;
  }

  @Override
  public void bindView(View row, Context context, Cursor c) {
    // ascent type
    String type = c.getString(c.getColumnIndex(AscentTypes.COLUMN_NAME));
    TextView text = (TextView) row.findViewById(R.id.ascent_type);
    text.setText(type);
    
    // add colored dot to the background of the TextView
    Graphics.drawDot(text, type, new int[]{0,0,42,12});
    
    // route grade
    TextView text2 = (TextView) row.findViewById(R.id.route_grade);
    String grade;
    if (useFrenchGrades) {
      grade = c.getString(c.getColumnIndex(Grades.COLUMN_GRADE_FR));
    } else {
      grade = c.getString(c.getColumnIndex(Grades.COLUMN_GRADE_YDS));
    }
    text2.setText(grade);
    // route name
    TextView text3 = (TextView) row.findViewById(R.id.route_name);
    text3.setText(c.getString(c.getColumnIndex(Routes.COLUMN_NAME)));
    // ascent notes
    TextView text4 = (TextView) row.findViewById(R.id.ascent_notes);
    text4.setText(c.getString(c.getColumnIndex(Ascents.COLUMN_NOTES)));
  }
}
