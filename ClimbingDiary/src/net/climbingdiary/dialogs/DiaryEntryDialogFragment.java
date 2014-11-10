package net.climbingdiary.dialogs;

import java.sql.Date;

import net.climbingdiary.R;
import net.climbingdiary.activities.EntryActivity;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.data.DiaryDbHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Spinner;

@SuppressLint("InflateParams")
public class DiaryEntryDialogFragment extends DialogFragment {

  private DiaryDbHelper dbhelper;
  
  /*****************************************************************************************************
   *                                          VIEW HOLDER PATTERN
   *****************************************************************************************************/
  private class ViewHolder {
    public DatePicker dp;
    public Spinner ct;
    public AutoCompleteTextView pl;
  };
  private ViewHolder vh = new ViewHolder();
    
  public DiaryEntryDialogFragment(DiaryDbHelper dbhelper) {
    this.dbhelper = dbhelper;     // store the database helper
  }
  
  @Override
  @NonNull
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    setRetainInstance(true); // fix rotation bug
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();    // Get the layout inflater

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View view = inflater.inflate(R.layout.dialog_entry, null);
    builder.setTitle(R.string.add_entry)
      .setView(view)
      // Add action buttons
      .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          // add the new entry to the database
          int day = vh.dp.getDayOfMonth();
          int mon = vh.dp.getMonth();
          int yea = vh.dp.getYear() - 1900;
          long new_id = dbhelper.addEntry(new Date(yea,mon,day),
              vh.ct.getSelectedItem().toString(),
              vh.pl.getText().toString());
          
          // start the entry activity on the newly created entry
          Intent intent = new Intent(getActivity(), EntryActivity.class);
          intent.putExtra(MainActivity.EXTRA_ENTRY_ID, new_id);
          startActivity(intent);
        }
      })
      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          DiaryEntryDialogFragment.this.getDialog().cancel();
        }
      });
    
    // setup VH
    vh.ct = (Spinner) view.findViewById(R.id.entry_type);
    vh.pl = (AutoCompleteTextView) view.findViewById(R.id.entry_place);
    vh.dp = (DatePicker) view.findViewById(R.id.entry_date);

    // populate TYPE spinner
    String[] types = dbhelper.getAllTypes();
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        android.R.layout.simple_spinner_item,types);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    vh.ct.setAdapter(adapter);
    
    // link autosuggestions for PLACE textview
    String[] places = dbhelper.getAllPlaces();
    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
        android.R.layout.simple_spinner_item,places);
    vh.pl.setAdapter(adapter2);
    
    return builder.create();
  }
  
  // fix for rotation bug
  @Override
  public void onDestroyView() {
    if (getDialog() != null && getRetainInstance())
      getDialog().setOnDismissListener(null);
    super.onDestroyView();
  }
  
}
