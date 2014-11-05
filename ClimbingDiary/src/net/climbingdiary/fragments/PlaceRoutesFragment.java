package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.adapters.PlaceRoutesAdapter;
import net.climbingdiary.data.DiaryContract.Routes;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class PlaceRoutesFragment extends LoaderFragment {

  private long place_id;            // ID of the climbing place

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
    // retrieve data from arguments bundle
    Bundle data = getArguments();
    if (data == null) {
      throw new Error("Unable to create PlaceRoutes fragment: null bundle.");
    }
    this.place_id = getArguments().getLong(MainActivity.EXTRA_PLACE_ID);
   
    // create the layout, a list of routes
    View rootView = inflater.inflate(R.layout.fragment_routes, container, false);

    // connect the list view with the custom diary adapter
    final ListView routes = (ListView) rootView.findViewById(R.id.routes_list);
    mAdapter = new PlaceRoutesAdapter(getActivity(), null, 0, R.layout.item_routes);
    routes.setAdapter(mAdapter);
    
    // attach a context menu to the ListView
    registerForContextMenu(routes);
    
    // Prepare the data loaders
    initLoader(MainActivity.LOADER_PLACEROUTES);
    
    return rootView;
  }
  
  /*****************************************************************************************************
   *                                          CONTEXT MENU
   *****************************************************************************************************/
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    MenuInflater inflater = getActivity().getMenuInflater();
    inflater.inflate(R.menu.context_menu_route, menu);
  }
  
  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    
    switch (item.getItemId()) {
    
      case R.id.edit:                                     // edit route details
        Routes.Data rinfo = new Routes.Data();
        rinfo._id = info.id;
        rinfo.place_id = place_id;
        DialogFragment newFragment =
            new RouteDialogFragment(dbhelper,rinfo,R.string.edit_route,R.string.update);
        newFragment.show(getActivity().getSupportFragmentManager(), "route_entry");    
        return true;
        
      case R.id.delete:                                   // delete route
        DialogFragment deleteDialog = new DeleteRouteDialogFragment(dbhelper, info.id);
        deleteDialog.show(getActivity().getSupportFragmentManager(), "delete_route");
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
    return dbhelper.getRoutes(place_id);
  }
}
