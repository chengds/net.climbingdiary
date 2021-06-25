package net.climbingdiary.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.climbingdiary.R;
import net.climbingdiary.activities.EntryActivity;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.DiaryAdapter;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.dialogs.DiaryEntryDialogFragment;

import java.util.Objects;

public class DiaryFragment extends LoaderFragment {

    private Context myContext;

    public void onEntrySelected(Context parent, long id) {
        // pass the id of the clicked item to the new activity
        Intent intent = new Intent(parent, EntryActivity.class);
        intent.putExtra(MainActivity.EXTRA_ENTRY_ID, id);
        startActivity(intent);
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

    // retrieve fragment manager
    FragmentManager fm = requireActivity().getSupportFragmentManager();
    
    // set up long click and click callbacks
    entries.setOnItemLongClickListener((parent, view, position, id) -> {
      new RemoveEntry(dbhelper,id).show(fm, "remove_entry");
      return true;
    });
    entries.setOnItemClickListener((parent, view, position, id) -> onEntrySelected(myContext, id));

    // setup callback for add new entry button
    FloatingActionButton addEntry = rootView.findViewById(R.id.add_entry);
    addEntry.setOnClickListener(view ->
      new DiaryEntryDialogFragment(dbhelper).show(fm, "diary_entry")
    );

    // Prepare the data loaders
    initLoader(MainActivity.LOADER_DIARY);
    
    return rootView;
  }
  
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
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
}
