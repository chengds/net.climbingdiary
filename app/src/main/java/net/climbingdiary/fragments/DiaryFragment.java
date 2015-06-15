package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.DiaryAdapter;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.dialogs.DiaryEntryDialogFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DiaryFragment extends LoaderFragment
       implements OnClickListener {

  private OnEntrySelectedListener mCallback;    // Callback for diary entry selection
  
  /*****************************************************************************************************
   *                                          COMMUNICATION CALLBACK
   * The calling activity must implement this interface and deal with diary entry selection.
   *****************************************************************************************************/
  public interface OnEntrySelectedListener {
    public void onEntrySelected(long id);
  }
  
  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
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
        mCallback.onEntrySelected(id);
      }
    });

    // setup callback for button
    final Button addEntry = (Button) rootView.findViewById(R.id.add_entry);
    addEntry.setOnClickListener(this);

    // Prepare the data loaders
    initLoader(MainActivity.LOADER_DIARY);
    
    return rootView;
  }
  
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mCallback = (OnEntrySelectedListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement OnEntrySelectedListener");
    }
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

  /*****************************************************************************************************
   *                                          CALLBACKS
   *****************************************************************************************************/
  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.add_entry) {
      DialogFragment newFragment = new DiaryEntryDialogFragment(dbhelper);
      newFragment.show(getActivity().getSupportFragmentManager(), "diary_entry");
    }
  }
}
