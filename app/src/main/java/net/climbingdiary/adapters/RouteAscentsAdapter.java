package net.climbingdiary.adapters;

import java.sql.Date;
import java.text.DateFormat;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.AscentTypes;
import net.climbingdiary.data.DiaryContract.Ascents;
import net.climbingdiary.data.DiaryContract.DiaryEntry;
import net.climbingdiary.utils.Graphics;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class RouteAscentsAdapter extends BaseAdapter {
    private final DateFormat df = DateFormat.getDateInstance();

    public RouteAscentsAdapter(Context context, Cursor c, int flags, int layout) {
        super(context, c, flags, layout);
    }
  
    @Override
    public void bindView(View row, Context context, Cursor c) {
        // ascent date
        TextView text1 = row.findViewById(R.id.date);                        // date
        long val = c.getLong(c.getColumnIndex(DiaryEntry.COLUMN_DATE));
        text1.setText(df.format(new Date(val)));
        // ascent type
        String type = c.getString(c.getColumnIndex(AscentTypes.COLUMN_NAME));
        TextView text2 = row.findViewById(R.id.type);
        text2.setText(type);
        text2.setBackgroundColor(Graphics.getColor(context, type));
        // ascent notes
        TextView text4 = row.findViewById(R.id.notes);
        text4.setText(c.getString(c.getColumnIndex(Ascents.COLUMN_NOTES)));
    }
}
