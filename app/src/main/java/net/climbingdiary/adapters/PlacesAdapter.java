package net.climbingdiary.adapters;

import net.climbingdiary.R;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class PlacesAdapter extends BaseAdapter {

  public PlacesAdapter(Context context, Cursor c, int flags, int layout) {
    super(context, c, flags, layout);
  }

  @Override
  public void bindView(View row, Context context, Cursor c) {
    TextView name = (TextView) row.findViewById(R.id.place);      // name
    name.setText(c.getString(1));
    TextView routes = (TextView) row.findViewById(R.id.routes);   // number of routes
    routes.setText(String.valueOf(c.getLong(3)));
    TextView visits = (TextView) row.findViewById(R.id.visits);   // number of visits
    visits.setText(String.valueOf(c.getLong(2)));
  }
}
