package net.climbingdiary.adapters;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.AscentTypes;
import net.climbingdiary.data.DiaryContract.Grades;
import net.climbingdiary.data.DiaryContract.Routes;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.widget.TextView;

public class PlaceRoutesAdapter extends BaseAdapter {

  public PlaceRoutesAdapter(Context context, Cursor c, int flags, int layout) {
    super(context, c, flags, layout);
  }

  @Override
  public void bindView(View row, Context context, Cursor c) {
    
    TextView text2 = (TextView) row.findViewById(R.id.route_grade);         // route grade
    String grade = c.getString(c.getColumnIndex(Grades.COLUMN_GRADE_YDS));
    text2.setText(grade);
    TextView text3 = (TextView) row.findViewById(R.id.route_name);          // route name
    text3.setText(c.getString(c.getColumnIndex(Routes.COLUMN_NAME)));
    TextView text4 = (TextView) row.findViewById(R.id.ascents);             // number of ascents
    text4.setText(String.valueOf(c.getLong(c.getColumnIndex("ascents"))));
    TextView text5 = (TextView) row.findViewById(R.id.route_notes);         // route notes
    text5.setText(c.getString(c.getColumnIndex(Routes.COLUMN_NOTES)));
    
    TextView text = (TextView) row.findViewById(R.id.status);               // status
    String status = c.getString(c.getColumnIndex("status"));
    String best = AscentTypes.getBest(status);
    text.setText(best);
    setDots(text,best);
  }
  
  public void setDots(View view, String type) {
    ShapeDrawable dot = new ShapeDrawable(new OvalShape());
    
    if (type.equalsIgnoreCase("redpoint")) {
      dot.getPaint().setColor(Color.RED);
    } else if (type.equalsIgnoreCase("onsight")) {
      dot.getPaint().setColor(Color.parseColor("#FFD700"));
    } else if (type.equalsIgnoreCase("flash")) {
      dot.getPaint().setColor(Color.parseColor("#EE7600"));
    } else {
      dot.getPaint().setColor(Color.BLACK);
    }
    
    LayerDrawable d = new LayerDrawable(new Drawable[]{dot});
    d.setLayerInset(0, 0, 0, 36, 12);
    
    view.setBackgroundDrawable(d);
  }
}
