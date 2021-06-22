package net.climbingdiary.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.AscentsAdapter;
import net.climbingdiary.data.DiaryContract.Ascents;
import net.climbingdiary.dialogs.AscentDialogFragment;
import net.climbingdiary.dialogs.DeleteAscentDialogFragment;

public class AscentsFragment extends LoaderFragment
       implements OnClickListener {
  
  private long entry_id;                         // ID of the climbing day entry
  private long place_id;                         // ID of the climbing place
  
  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
    // retrieve data from arguments bundle
    Bundle data = getArguments();
    if (data == null) {
      throw new Error("Unable to create ascents fragment: null bundle.");
    }
    this.entry_id = data.getLong(MainActivity.EXTRA_ENTRY_ID);
    this.place_id = data.getLong(MainActivity.EXTRA_PLACE_ID);
    
    // create the layout, a list of diary entries
    View rootView = inflater.inflate(R.layout.fragment_ascents, container, false);

    // retrieve desired grade display setting
    boolean value = dbhelper.getSetting("useFrenchGrades").equals("on");

    // connect the list view with the custom adapter
    final ListView ascents = (ListView) rootView.findViewById(R.id.ascents_list);
    mAdapter = new AscentsAdapter(getActivity(), null, 0, R.layout.item_ascents);
    ((AscentsAdapter)mAdapter).useFrenchGrades(value);
    ascents.setAdapter(mAdapter);
    
    // attach a context menu to the ListView
    registerForContextMenu(ascents);
    
    // setup callback for button
    FloatingActionButton addAscent = rootView.findViewById(R.id.add_ascent);
    addAscent.setOnClickListener(this);

    // Prepare the data loaders
    initLoader(MainActivity.LOADER_ASCENTS);
    
    return rootView;
  }

  /*****************************************************************************************************
   *                                          CONTEXT MENU
   *****************************************************************************************************/
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    MenuInflater inflater = getActivity().getMenuInflater();
    inflater.inflate(R.menu.context_menu_ascent, menu);
  }
  
  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    
    switch (item.getItemId()) {
    
      case R.id.edit:                                     // edit ascent details
        Ascents.Data ainfo = new Ascents.Data();
        ainfo._id = info.id;
        ainfo.entry_id = entry_id;
        ainfo.place_id = place_id;
        DialogFragment newFragment =
            new AscentDialogFragment(dbhelper,ainfo,R.string.edit_ascent,R.string.update);
        newFragment.show(getActivity().getSupportFragmentManager(), "ascent_entry");    
        return true;
        
      case R.id.delete:                                   // delete route
        DialogFragment deleteDialog = new DeleteAscentDialogFragment(dbhelper, info.id);
        deleteDialog.show(getActivity().getSupportFragmentManager(), "delete_ascent");
        return true;
        
      default:
        return super.onContextItemSelected(item);
    }

  }
  
  /*****************************************************************************************************
   *                                          DATA RETRIEVAL
   *****************************************************************************************************/
  @Override
  public Cursor dataRetrieval() {
    return dbhelper.getAscents(entry_id);
  }
  
  /*****************************************************************************************************
   *                                          CALLBACKS
   *****************************************************************************************************/
  public void onClick(View v) {
    if (v.getId() == R.id.add_ascent) {
      Ascents.Data ainfo = new Ascents.Data();
      ainfo.entry_id = entry_id;
      ainfo.place_id = place_id;
      DialogFragment newFragment = new AscentDialogFragment(dbhelper,ainfo,R.string.add_ascent,R.string.add);
      newFragment.show(getActivity().getSupportFragmentManager(), "ascent_entry");
    }
  }
}
