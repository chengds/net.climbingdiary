package net.climbingdiary.adapters;

import java.sql.Date;
import java.text.DateFormat;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.AscentTypes;
import net.climbingdiary.data.DiaryContract.Ascents;
import net.climbingdiary.data.DiaryContract.DiaryEntry;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class RouteAscentsAdapter extends BaseAdapter {
  private DateFormat df;

  public RouteAscentsAdapter(Context context, Cursor c, int flags, int layout) {
    super(context, c, flags, layout);
    df = DateFormat.getDateInstance();
  }
  
  @Override
  public void bindView(View row, Context context, Cursor c) {
    // ascent date
    TextView text1 = (TextView) row.findViewById(R.id.date);                        // date
    long val = c.getLong(c.getColumnIndex(DiaryEntry.COLUMN_DATE));
    text1.setText(df.format(new Date(val)));
    // ascent type
    TextView text2 = (TextView) row.findViewById(R.id.type);
    text2.setText(c.getString(c.getColumnIndex(AscentTypes.COLUMN_NAME)));
    // ascent notes
    TextView text4 = (TextView) row.findViewById(R.id.notes);
    text4.setText(c.getString(c.getColumnIndex(Ascents.COLUMN_NOTES)));
  }

}
