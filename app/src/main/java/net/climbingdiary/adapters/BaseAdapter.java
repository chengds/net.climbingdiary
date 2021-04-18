package net.climbingdiary.adapters;

import android.content.Context;
import android.database.Cursor;
import androidx.cursoradapter.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAdapter extends CursorAdapter {
  private LayoutInflater inflater;
  private int layout;

  public BaseAdapter(Context context, Cursor c, int flags, int layout) {
    super(context, c, flags);
    this.inflater = LayoutInflater.from(context);
    this.layout = layout;
  }

  @Override
  public View newView(Context context, Cursor c, ViewGroup parent) {
    return inflater.inflate(layout, parent, false);
  }
}
