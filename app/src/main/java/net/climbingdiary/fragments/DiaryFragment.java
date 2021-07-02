package net.climbingdiary.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import net.climbingdiary.R;
import net.climbingdiary.activities.EntryActivity;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.DiaryAdapter;
import net.climbingdiary.data.DiaryDbHelper;

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

    // Prepare the data loaders
    initLoader(MainActivity.LOADER_DIARY);
    
    return rootView;
  }
  
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

  /**********************************************************************************************************
   * A {@link DialogFragment} that ask for confirmation of entry removal.
   */
  public static class RemoveEntry extends DialogFragment {
    DiaryDbHelper dbhelper;
    long entryId;

    public RemoveEntry(DiaryDbHelper dbhelper, long entryId) {
      this.dbhelper = dbhelper;
      this.entryId = entryId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder
          .setMessage(R.string.dialog_remove_entry)
          .setPositiveButton(R.string.dialog_yes,
                  (dialog, id) -> dbhelper.removeEntry(entryId))
          .setNegativeButton(R.string.cancel, null);

      // Create the AlertDialog object and return it
      return builder.create();
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
