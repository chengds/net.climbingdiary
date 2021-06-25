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

import androidx.recyclerview.widget.RecyclerView;

import net.climbingdiary.R;

import java.util.ArrayList;
import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

    private Context myContext;
    private LayoutInflater myInflater;
    private List<ArrayList<String>> myData;             // Dataset associated with this adapter

    /**
     * Each row is a list of: grade, boxes, num sent, num tried.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView grade;
        private final LinearLayout boxes;
        private final TextView sent;
        private final TextView tried;

        // Create a new row
        public ViewHolder(Context context, View view) {
            super(view);

            // Define click listener for the ViewHolder's View

            // Keep a reference to elements in this row
            grade = view.findViewById(R.id.grade);
            boxes = view.findViewById(R.id.boxes);
            sent = view.findViewById(R.id.sent);
            tried = view.findViewById(R.id.tried);

            updateBoxes(context);
        }

        // Get parts of the row
        public LinearLayout getBoxes() {
            return boxes;
        }
        public TextView getGrade() {
            return grade;
        }
        public TextView getSent() {
            return sent;
        }
        public TextView getTried() {
            return tried;
        }

        // Update number of boxes
        public void updateBoxes(Context context) {
            // Calculate size of box
            int width = context.getResources().getDisplayMetrics().widthPixels;
            int side = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30f,
                    context.getResources().getDisplayMetrics());
            int fixed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,108f,
                    context.getResources().getDisplayMetrics());

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
                    box.setBackgroundColor(Color.DKGRAY);
                    boxes.addView(box, side, side);
                }
            } else {
                // Remove excess boxes
                while (num > boxes.getChildCount()) boxes.removeViewAt(num);
            }
        }
    }

    // Initialize the dataset
    public StatsAdapter(Context context, List<ArrayList<String>> dataSet) {
        myContext = context;
        myInflater = LayoutInflater.from(context);
        myData = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = myInflater.inflate(R.layout.item_stats, parent, false);

        return new ViewHolder(myContext, view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ArrayList<String> list = myData.get(position);
        viewHolder.getGrade().setText(list.get(0));
        viewHolder.getSent().setText("0");
        viewHolder.getTried().setText("1");

        // Re-calculate number of boxes
        viewHolder.updateBoxes(myContext);

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.getBoxes().setText(localDataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myData.size();
    }
}
