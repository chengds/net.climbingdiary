package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.data.DiaryContract.Grades;
import net.climbingdiary.data.DiaryContract.Routes;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class RouteDialogFragment extends DialogFragment {

  private DiaryDbHelper dbhelper;   // database and entry ID of the climbing day
  private Routes.Data info;
  private int resTitle;
  private int resAccept;

  /*****************************************************************************************************
   *                                          VIEW HOLDER PATTERN
   *****************************************************************************************************/
  private class ViewHolder {
    public TextView name;
    public TextView place;
    public Spinner grade;
    public EditText notes;
  };
  private ViewHolder vh = new ViewHolder();
  
  /*****************************************************************************************************
   *                                          METHODS
   *****************************************************************************************************/
  public RouteDialogFragment(DiaryDbHelper dbhelper, Routes.Data info,
      int resTitle, int resAccept) {
    this.dbhelper = dbhelper;
    this.info = info;
    this.resTitle = resTitle;
    this.resAccept = resAccept;
  }
  
  @Override
  @NonNull
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    setRetainInstance(true); // fix rotation bug
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();    // Get the layout inflater
    
    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View view = inflater.inflate(R.layout.dialog_route, null);
    builder.setTitle(resTitle)
      .setView(view)
      // Add action buttons
      .setPositiveButton(resAccept, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          // add/update the route info
          info.name = vh.name.getText().toString();
          info.grade_id = vh.grade.getSelectedItemId();
          info.notes = vh.notes.getText().toString();
          
          // update the database entry
          dbhelper.updateRoute(info);
        }
      })
      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          RouteDialogFragment.this.getDialog().cancel();
        }
      });
    
    // setup VH
    vh.name = (EditText) view.findViewById(R.id.route_name);
    vh.place = (TextView) view.findViewById(R.id.place_name);
    vh.grade = (Spinner) view.findViewById(R.id.route_grade);
    vh.notes = (EditText) view.findViewById(R.id.route_notes);
    
    // populate grades spinner
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
        android.R.layout.simple_spinner_item, dbhelper.getGrades(),
        new String[]{ Grades.COLUMN_GRADE_YDS, Grades.COLUMN_GRADE_FR },
        new int[]{ android.R.id.text1, android.R.id.text2 }, 0);
    adapter.setDropDownViewResource(R.layout.modified_list_item_2);
    vh.grade.setAdapter(adapter);
    
    if (info._id > 0) {
      // retrieve the entry info for editing
      info = dbhelper.getRoute(info._id);
      
      // show current details
      vh.name.setText(info.name);
      vh.grade.setSelection((int)info.grade_id - 1);
      vh.notes.setText(info.notes);
    }
    
    // show place of the route
    vh.place.setText(info.place_name);
    

    return builder.create();
  }

  //fix for rotation bug
  @Override
  public void onDestroyView() {
    if (getDialog() != null && getRetainInstance())
      getDialog().setOnDismissListener(null);
    super.onDestroyView();
  }
  
}
