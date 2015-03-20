package net.climbingdiary.dialogs;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryContract.Ascents;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.data.DiaryContract.AscentTypes;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("InflateParams")
public class AscentDialogFragment extends DialogFragment {
  
  private DiaryDbHelper dbhelper;   // database
  private Ascents.Data info;
  private int resTitle;
  private int resAccept;
  
  /*****************************************************************************************************
   *                                          VIEW HOLDER PATTERN
   *****************************************************************************************************/
  private class ViewHolder {
    public AutoCompleteTextView route;
    public Spinner asct;
    public EditText notes;
  }
  private ViewHolder vh = new ViewHolder();
  
  /*****************************************************************************************************
   *                                          METHODS
   *****************************************************************************************************/
  public AscentDialogFragment(DiaryDbHelper dbhelper, Ascents.Data info,
      int resTitle, int resAccept) {
    this.dbhelper = dbhelper;     // store the database helper and ID
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
    View view = inflater.inflate(R.layout.dialog_ascent, null);
    builder.setTitle(resTitle)
      .setView(view)
      // Add action buttons
      .setPositiveButton(resAccept, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          // add the new ascent to the database
          //long res = dbhelper.addAscent(
          //    info.entry_id, info.place_id,
          //    vh.route.getText().toString(),
          //    vh.asct.getSelectedItemId(),
          //    vh.notes.getText().toString());
          info.route_name = vh.route.getText().toString();
          info.type_id = vh.asct.getSelectedItemId();
          info.notes = vh.notes.getText().toString();
          long res = dbhelper.updateAscent(info);
          
          if (res > 0) { // prompt the user with a dialog to detail the route just added
            Routes.Data rinfo = new Routes.Data();
            rinfo._id = res;
            rinfo.place_id = info.place_id;
            DialogFragment newFragment = 
                new RouteDialogFragment(dbhelper,rinfo,R.string.edit_route,R.string.update);
            newFragment.show(getActivity().getSupportFragmentManager(), "route_entry");
            dbhelper.getSource().notifyChanged();
          }
        }
      })
      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          AscentDialogFragment.this.getDialog().cancel();
        }
      });
    
    // setup VH
    vh.route = (AutoCompleteTextView) view.findViewById(R.id.route_name);
    vh.asct = (Spinner) view.findViewById(R.id.ascent_type);
    vh.notes = (EditText) view.findViewById(R.id.ascent_notes);
    
    // populate TYPE spinner
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
        android.R.layout.simple_spinner_item, dbhelper.getAscentTypes(),
        new String[]{ AscentTypes.COLUMN_NAME }, new int[]{ android.R.id.text1 }, 0);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    vh.asct.setAdapter(adapter);
    
    // link autosuggestions for route name TextView
    String[] names = dbhelper.getAllRoutes(info.place_id);
    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
        android.R.layout.simple_spinner_item,names);
    vh.route.setAdapter(adapter2);
    
    if (info._id > 0) {
      // retrieve the entry info for editing
      info = dbhelper.getAscent(info._id);
      
      // show current details
      vh.route.setText(info.route_name);
      vh.asct.setSelection((int)info.type_id - 1);
      vh.notes.setText(info.notes);
    }
    
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
