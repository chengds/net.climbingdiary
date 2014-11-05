package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.activities.EntryActivity;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.DiaryAdapter;
import net.climbingdiary.data.DiaryDbHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DiaryFragment extends LoaderFragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
    // create the layout, a list of diary entries
    View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

    // connect the list view with the custom diary adapter
    final ListView entries = (ListView) rootView.findViewById(R.id.diary_entries);
    mAdapter = new DiaryAdapter(getActivity(), null, 0, R.layout.item_diary);
    entries.setAdapter(mAdapter);
    
    // set up longclick and click callbacks
    entries.setOnItemLongClickListener(new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        new RemoveEntry(dbhelper,id).show(getActivity().getSupportFragmentManager(), "remove_entry");
        return false;
      }
    });
    entries.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // pass the id of the clicked item to the new activity
        Intent intent = new Intent(getActivity(), EntryActivity.class);
        intent.putExtra(MainActivity.EXTRA_ENTRY_ID, id);
        startActivity(intent);
      }
    });

    initLoader(MainActivity.LOADER_DIARY);
    return rootView;
  }
  
  /**********************************************************************************************************
   * A {@link DialogFragment} that ask for confirmation of entry removal.
   */
  public static class RemoveEntry extends DialogFragment {
    DiaryDbHelper dbhelper;
    long entryid;
    
    public RemoveEntry(DiaryDbHelper dbhelper, long entryid) {
      this.dbhelper = dbhelper;
      this.entryid = entryid;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      setRetainInstance(true); // fix rotation bug
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder
          .setMessage(R.string.dialog_remove_entry)
          .setPositiveButton(R.string.dialog_yes,
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  dbhelper.removeEntry(entryid);
                }
              })
          .setNegativeButton(R.string.cancel, null);
      // Create the AlertDialog object and return it
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
  
  /*****************************************************************************************************
   *                                          DATA RETRIEVAL
   *****************************************************************************************************/
  @Override
  public Cursor dataRetrieval() {
    return dbhelper.getEntries();
  }

}
