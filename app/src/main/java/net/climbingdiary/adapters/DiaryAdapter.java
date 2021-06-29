package net.climbingdiary.adapters;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.ClimbingTypes;
import net.climbingdiary.data.DiaryContract.DiaryEntry;
import net.climbingdiary.data.DiaryContract.Places;
import net.climbingdiary.utils.Graphics;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder>  {

    private final BaseAdapter myAdapter;   // adapter that will be connected to the data cursor
    private final DateFormat df = DateFormat.getDateInstance();
    private final Context myContext;
    private final LayoutInflater myInflater;
    private final DiaryAdapter.OnItemClickListener myListener;

    public interface OnItemClickListener {
        void onItemClick(View item);
        boolean onItemLongClick(View item);
    }

    public BaseAdapter getBaseAdapter() { return myAdapter; }

    /**
     * Each row is a list of: date, type, place.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView date;
        final TextView type;
        final TextView place;
        final View myView;

        public ViewHolder(View view) {
            super(view);
            myView = view;

            // Keep a reference to elements in this row
            date = view.findViewById(R.id.date);
            type = view.findViewById(R.id.type);
            place = view.findViewById(R.id.place);
        }

        public void bind(final DiaryAdapter.OnItemClickListener listener) {
            myView.setOnClickListener(listener::onItemClick);
            myView.setOnLongClickListener(listener::onItemLongClick);
        }
    }


    public DiaryAdapter(Context context, DiaryAdapter.OnItemClickListener listener) {
        myContext = context;
        myInflater = LayoutInflater.from(context);
        myAdapter = new BaseAdapter(myContext, null, 0, R.layout.item_diary) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView text1 = (TextView) view.findViewById(R.id.date);                        // date
                long val = cursor.getLong(cursor.getColumnIndex(DiaryEntry.COLUMN_DATE));
                text1.setText(df.format(new Date(val)));
                val = cursor.getLong(cursor.getColumnIndex(DiaryEntry._ID));
                text1.setTag(String.valueOf(val));
                TextView text2 = (TextView) view.findViewById(R.id.type);                        // type
                text2.setText(cursor.getString(cursor.getColumnIndex(ClimbingTypes.COLUMN_DESCRIPTION)));
                TextView text3 = (TextView) view.findViewById(R.id.place);                       // place
                text3.setText(cursor.getString(cursor.getColumnIndex(Places.COLUMN_NAME)));
            }
        };
        myListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public DiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = myInflater.inflate(R.layout.item_diary, parent, false);
        return new DiaryAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull DiaryAdapter.ViewHolder viewHolder, final int position) {
        myAdapter.getCursor().moveToPosition(position);
        myAdapter.bindView(viewHolder.myView, myContext, myAdapter.getCursor());

        // add click listener
        viewHolder.bind(myListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (myAdapter.getCursor() == null) return 0;
        return myAdapter.getCursor().getCount();
    }
}
