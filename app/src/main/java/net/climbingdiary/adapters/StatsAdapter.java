package net.climbingdiary.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.climbingdiary.R;
import net.climbingdiary.utils.Graphics;

import java.util.ArrayList;
import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

    private final Context myContext;
    private final LayoutInflater myInflater;
    private final List<ArrayList<String>> myData;             // Dataset associated with this adapter
    private final OnItemClickListener myListener;

    public interface OnItemClickListener {
        void onItemClick(View item);
    }

    /**
     * Each row is a list of: grade, boxes, num sent, num tried.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView grade;
        private final LinearLayout boxes;
        private final TextView sent;
        private final TextView tried;
        private final View myView;

        // Create a new row
        public ViewHolder(Context context, View view) {
            super(view);
            myView = view;

            // Keep a reference to elements in this row
            grade = view.findViewById(R.id.grade);
            boxes = view.findViewById(R.id.boxes);
            sent = view.findViewById(R.id.sent);
            tried = view.findViewById(R.id.tried);

            updateBoxes(context, null);
        }

        // Get parts of the row
        //public LinearLayout getBoxes() { return boxes; }
        // public TextView getGrade() { return grade; }
        //public TextView getSent() { return sent; }
        //public TextView getTried() { return tried; }

        // Update number of boxes
        public void updateBoxes(Context context, ArrayList<String> list) {
            if (list != null) {
                grade.setText(list.get(0));
                grade.setTag(list.get(1));
            }

            // Calculate size of box
            int width = context.getResources().getDisplayMetrics().widthPixels;
            int side = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30f,
                    context.getResources().getDisplayMetrics());
            int fixed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,108f,
                    context.getResources().getDisplayMetrics());

            // if there is only one box, remove it as it was used for a header
            if (boxes.getChildCount() == 1) boxes.removeAllViews();

            // Create the maximum number of boxes
            int num = (width - fixed) / side;
            if (num > boxes.getChildCount()) {
                // Add missing boxes
                for (int i=boxes.getChildCount(); i<num; i++) {
                    TextView box = new TextView(context);
                    box.setText(String.valueOf(i+1));
                    box.setTextSize(12);
                    box.setTextColor(Color.BLACK);
                    box.setGravity(Gravity.CENTER);
                    box.setPadding(2, 2, 2, 2);
                    boxes.addView(box, side, side);
                }
            } else {
                // Remove excess boxes
                while (num > boxes.getChildCount()) boxes.removeViewAt(num);
            }

            // appropriately color each box
            for (int i=0; i<num; i++) {
                if (list != null && i < list.size()-2) {
                    boxes.getChildAt(i).setBackgroundColor(Graphics.getColor(context, list.get(i+2)));
                } else {
                    boxes.getChildAt(i).setBackgroundColor(Color.DKGRAY);
                }
            }

            // find the number of completed routes
            int completed = 0, uncompleted = 0;
            if (list != null) {
                while (completed < list.size() - 2 && !list.get(completed + 2).equalsIgnoreCase("uncompleted")) {
                    completed++;
                }
                uncompleted = list.size() - 2 - completed;
            }

            // setup sent and tried text views
            sent.setText(String.valueOf(completed));
            sent.setTextSize(12);
            sent.setTextColor(Color.WHITE);
            sent.setPadding(4,0,4,0);
            tried.setText(String.valueOf(uncompleted));
            tried.setTextSize(12);
            tried.setTextColor(Color.WHITE);
            tried.setPadding(4,0,4,0);
        }

        public void setAsHeader(Context context, ArrayList<String> list) {
            grade.setText("");
            boxes.removeAllViews();
            TextView name = new TextView(context);
            name.setText(list.get(1));
            name.setTextSize(14);
            name.setTextColor(context.getResources().getColor(R.color.header));
            name.setGravity(Gravity.START);
            name.setBackgroundColor(Color.BLACK);
            boxes.addView(name);
            sent.setText(list.get(2));
            sent.setTextSize(14);
            sent.setTextColor(context.getResources().getColor(R.color.header));
            sent.setPadding(0,0,0,0);
            tried.setText(list.get(3));
            tried.setTextSize(14);
            tried.setTextColor(context.getResources().getColor(R.color.header));
            tried.setPadding(0,0,0,0);
        }

        public void bind(final OnItemClickListener listener) {
            myView.setOnClickListener(listener::onItemClick);
        }
    }

    // Initialize the dataset
    public StatsAdapter(Context context, List<ArrayList<String>> dataSet, OnItemClickListener listener) {
        myContext = context;
        myInflater = LayoutInflater.from(context);
        myData = dataSet;
        myListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = myInflater.inflate(R.layout.item_stats, parent, false);
        return new ViewHolder(myContext, view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        ArrayList<String> list = myData.get(position);

        if (list.get(0).equals("")) {
            // this ia a header
            viewHolder.setAsHeader(myContext, list);
        } else {
            // update boxes
            viewHolder.updateBoxes(myContext, list);
        }

        // add click listener
        viewHolder.bind(myListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myData.size();
    }
}
