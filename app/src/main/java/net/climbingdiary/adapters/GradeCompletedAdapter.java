package net.climbingdiary.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.AscentTypes;
import net.climbingdiary.data.DiaryContract.Places;
import net.climbingdiary.data.DiaryContract.Routes;
import net.climbingdiary.utils.Graphics;

/**
 * Created by ChengDS on 6/20/2015.
 */
public class GradeCompletedAdapter extends BaseAdapter {

  public GradeCompletedAdapter(Context context, Cursor c, int flags, int layout) {
    super(context, c, flags, layout);
  }

  @Override
  public void bindView(View row, Context context, Cursor c) {
    // type of ascent when completed
    TextView text = (TextView) row.findViewById(R.id.status);
    String status = c.getString(c.getColumnIndex(AscentTypes.COLUMN_NAME));
    text.setText(status);
    Graphics.drawDot(text, status, new int[]{0, 0, 36, 12});
    // route name
    TextView text2 = (TextView) row.findViewById(R.id.route);
    text2.setText(c.getString(c.getColumnIndex(Routes.COLUMN_NAME)));
    // route place
    TextView text3 = (TextView) row.findViewById(R.id.place);
    text3.setText(c.getString(c.getColumnIndex(Places.COLUMN_NAME)));
  }
}
