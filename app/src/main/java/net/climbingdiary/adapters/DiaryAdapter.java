package net.climbingdiary.adapters;

import java.sql.Date;
import java.text.DateFormat;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.ClimbingTypes;
import net.climbingdiary.data.DiaryContract.DiaryEntry;
import net.climbingdiary.data.DiaryContract.Places;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class DiaryAdapter extends BaseAdapter {
    private final DateFormat df = DateFormat.getDateInstance();

    public DiaryAdapter(Context context, Cursor c, int flags, int layout) {
        super(context, c, flags, layout);
    }

    // Fills the given row with info from the database cursor
    @Override
    public void bindView(View row, Context context, Cursor c) {
        TextView text1 = (TextView) row.findViewById(R.id.date);                        // date
        long val = c.getLong(c.getColumnIndex(DiaryEntry.COLUMN_DATE));
        text1.setText(df.format(new Date(val)));
        TextView text2 = (TextView) row.findViewById(R.id.type);                        // type
        text2.setText(c.getString(c.getColumnIndex(ClimbingTypes.COLUMN_DESCRIPTION)));
        TextView text3 = (TextView) row.findViewById(R.id.place);                       // place
        text3.setText(c.getString(c.getColumnIndex(Places.COLUMN_NAME)));

        // create and attach a badge showing number of ascents (TOO SLOW)
        /*TextView text4 = (TextView) row.findViewById(R.id.num);
        int numAscents = c.getInt(4);
        if (numAscents > 0) {
            text4.setText(String.valueOf(numAscents));
        } else {
            text4.setText("");
        }*/
    }
}
