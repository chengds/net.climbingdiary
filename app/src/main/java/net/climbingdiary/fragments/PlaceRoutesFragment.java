package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.activities.RouteActivity;
import net.climbingdiary.adapters.PlaceRoutesAdapter;
import net.climbingdiary.data.DiaryContract.Routes;
import net.climbingdiary.dialogs.DeleteRouteDialogFragment;
import net.climbingdiary.dialogs.RouteDialogFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class PlaceRoutesFragment extends LoaderFragment
        implements OnClickListener {

    private long place_id;                        // ID of the climbing place
    private String place_name;                    // name of the climbing place
    private Context myContext;

    public void onRouteSelected(Context parent, long id) {
        Intent intent = new Intent(parent, RouteActivity.class);
        intent.putExtra(MainActivity.EXTRA_ROUTE_ID, id);
        intent.putExtra(MainActivity.EXTRA_PLACE_ID, place_id);
        intent.putExtra(MainActivity.EXTRA_PLACE_NAME, place_name);
        Routes.Data info = dbhelper.getRoute(id);
        intent.putExtra(MainActivity.EXTRA_ROUTE_NAME, info.name);
        startActivity(intent);
    }

    /*****************************************************************************************************
     *                                          LIFECYCLE METHODS
     *****************************************************************************************************/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // retrieve data from arguments bundle
        Bundle data = getArguments();
        if (data == null) throw new Error("Unable to create PlaceRoutes fragment: null bundle.");
        this.place_id = data.getLong(MainActivity.EXTRA_PLACE_ID);
        this.place_name = data.getString(MainActivity.EXTRA_PLACE_NAME);

        // create the layout, a list of routes
        View rootView = inflater.inflate(R.layout.fragment_routes, container, false);

        // retrieve desired grade display setting
        boolean value = dbhelper.getSetting("useFrenchGrades").equals("on");

        // connect the list view with the custom adapter
        final ListView routes = (ListView) rootView.findViewById(R.id.routes_list);
        mAdapter = new PlaceRoutesAdapter(getActivity(), null, 0, R.layout.item_routes);
        ((PlaceRoutesAdapter)mAdapter).useFrenchGrades(value);
        routes.setAdapter(mAdapter);

        // setup callback for route selection
        routes.setOnItemClickListener((parent, view, pos, id) -> onRouteSelected(myContext, id));

        // attach a context menu to the ListView
        registerForContextMenu(routes);

        // setup callback for button
        rootView.findViewById(R.id.add_route).setOnClickListener(this);

        // Prepare the data loaders
        initLoader(MainActivity.LOADER_PLACEROUTES);

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

    /*****************************************************************************************************
     *                                          CONTEXT MENU
     *****************************************************************************************************/
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_route, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        if (id == R.id.edit) {                                     // edit route details
            Routes.Data rinfo = new Routes.Data();
            rinfo._id = info.id;
            rinfo.place_id = place_id;
            DialogFragment newFragment =
                    new RouteDialogFragment(dbhelper, rinfo, R.string.edit_route, R.string.update);
            newFragment.show(getActivity().getSupportFragmentManager(), "route_entry");
            return true;
        } else if (id == R.id.delete) {                                   // delete route
            DialogFragment deleteDialog = new DeleteRouteDialogFragment(dbhelper, info.id);
            deleteDialog.show(getActivity().getSupportFragmentManager(), "delete_route");
            return true;
        } else {
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

    /*****************************************************************************************************
     *                                          CALLBACKS
     *****************************************************************************************************/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_route) {
            Routes.Data rinfo = new Routes.Data();
            rinfo._id = -1;
            rinfo.place_id = place_id;
            rinfo.place_name = place_name;
            DialogFragment newFragment =
                new RouteDialogFragment(dbhelper,rinfo,R.string.add_route,R.string.add);
            newFragment.show(getActivity().getSupportFragmentManager(), "route_entry");
        }
    }

}
