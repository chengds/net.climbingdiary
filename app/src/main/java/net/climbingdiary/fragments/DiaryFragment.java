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
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.climbingdiary.R;
import net.climbingdiary.activities.EntryActivity;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.DiaryAdapter;
import net.climbingdiary.adapters.StatsAdapter;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.dialogs.DiaryEntryDialogFragment;

import java.util.Objects;

public class DiaryFragment extends LoaderFragment {

    private Context myContext;
    private DiaryAdapter myDiary;

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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // create the layout, a list of diary entries
        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        // retrieve fragment manager
        FragmentManager fm = requireActivity().getSupportFragmentManager();

        // connect the list view with the custom diary adapter
        final RecyclerView entries = rootView.findViewById(R.id.diary_entries);
        DiaryFragment father = this;
        myDiary = new DiaryAdapter(getActivity(), new DiaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View item) {
                TextView date = item.findViewById(R.id.date);
                String tag = (String) date.getTag();
                if (!tag.equals("")) onEntrySelected(myContext, Integer.parseInt(tag));
            }

            @Override
            public boolean onItemLongClick(View item) {
                TextView date = item.findViewById(R.id.date);
                String tag = (String) date.getTag();
                new RemoveEntry(dbhelper, Integer.parseInt(tag)).show(fm, "remove_entry");
                return true;
            }
        });
        mAdapter = myDiary.getBaseAdapter();
        entries.setAdapter(myDiary);

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
